<template>
  <v-container>
    <div class="d-flex justify-space-between align-center" style="margin-bottom: 20px;">
      <h1 v-if="equipment">Editando Equipamento: {{ equipment.assetNumber }}</h1>
      <h1 v-else>Carregando Equipamento...</h1>
      <v-btn prepend-icon="mdi-arrow-left" to="/equipments">
        Voltar para a Lista
      </v-btn>
    </div>

    <v-progress-circular v-if="isLoading" indeterminate color="primary"></v-progress-circular>

    <div v-if="apiError" class="error-message">
      <p>{{ apiError }}</p>
    </div>

    <form v-if="equipment" @submit.prevent="handleSubmit">
      <div>
        <label for="assetNumber">Nº Patrimônio:</label>
        <input type="text" id="assetNumber" v-model="equipment.assetNumber" required />
      </div>
      <div>
        <label for="type">Tipo:</label>
        <input type="text" id="type" v-model="equipment.type" required />
      </div>
      <div>
        <label for="brand">Marca:</label>
        <input type="text" id="brand" v-model="equipment.brand" required />
      </div>
      <div>
        <label for="model">Modelo:</label>
        <input type="text" id="model" v-model="equipment.model" required />
      </div>
      <div>
        <label for="acquisitionDate">Data de Aquisição:</label>
        <input type="date" id="acquisitionDate" v-model="equipment.acquisitionDate" required />
      </div>
      <div>
        <label for="status">Status:</label>
        <select id="status" v-model="equipment.status" required>
          <option value="Disponível">Disponível</option>
          <option value="Em uso">Em uso</option>
          <option value="Aguardando reparo">Aguardando reparo</option>
          <option value="Descartado">Descartado</option>
        </select>
      </div>
      <button type="submit" style="margin-top: 20px;">Salvar Alterações</button>
    </form>

  </v-container>
</template>

<script>
export default {
  name: 'EditEquipmentPage',
  props: ['id'],
  data() {
    return {
      equipment: null, 
      isLoading: false,
      apiError: null,
    };
  },
  async created() {
    await this.fetchEquipmentDetails();
  },
  methods: {
    async fetchEquipmentDetails() {
      this.isLoading = true;
      this.apiError = null;
      try {
        if (!this.$root.$keycloak || !this.$root.$keycloak.token) throw new Error("Não autenticado");

        const token = this.$root.$keycloak.token;
        const apiUrl = `http://localhost:8081/api/v1/equipments/${this.id}`;
        const response = await fetch(apiUrl, {
          headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 401) return this.$root.$keycloak.login();
        if (!response.ok) throw new Error(`Erro ao buscar detalhes do equipamento: ${response.statusText}`);

        this.equipment = await response.json();
        // Formata a data para o campo input type="date" (AAAA-MM-DD)
        if (this.equipment.acquisitionDate) {
          this.equipment.acquisitionDate = this.equipment.acquisitionDate.split('T')[0];
        }
      } catch (error) {
        this.apiError = error.message;
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    },
    async handleSubmit() {
      this.isLoading = true;
      this.apiError = null;
      try {
        if (!this.$root.$keycloak || !this.$root.$keycloak.token) throw new Error("Não autenticado");

        const token = this.$root.$keycloak.token;
        const apiUrl = `http://localhost:8081/api/v1/equipments/${this.id}`;
        const response = await fetch(apiUrl, {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.equipment)
        });

        if (response.status === 401) return this.$root.$keycloak.login();
        if (!response.ok) throw new Error(`Erro ao atualizar equipamento: ${response.statusText}`);

        alert('Equipamento atualizado com sucesso!');
        this.$router.push('/equipments');
      } catch (error) {
        this.apiError = error.message;
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    }
  }
}
</script>

<style scoped>
form div {
  margin-bottom: 10px;
}
form label {
  display: block;
  margin-bottom: 5px;
  text-align: left;
}
form input,
form select {
  width: calc(100% - 22px);
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
form button {
  padding: 10px 15px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.error-message {
  color: red;
  margin-top: 20px;
}
</style>