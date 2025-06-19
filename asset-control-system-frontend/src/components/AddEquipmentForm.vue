<template>
  <div class="add-equipment-form">
    <h3>Adicionar Novo Equipamento</h3>
    <form @submit.prevent="handleSubmit">
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
      <button type="submit">Salvar Equipamento</button>
    </form>
    <div v-if="message" :class="{ success: isSuccess, error: !isSuccess }">
      {{ message }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'AddEquipmentForm',
  data() {
    return {
      equipment: {
        assetNumber: '',
        type: '',
        brand: '',
        model: '',
        acquisitionDate: '', // Formato YYYY-MM-DD
        status: 'Disponível' // Valor padrão
      },
      message: '',
      isSuccess: false
    };
  },
  methods: {
    async handleSubmit() {
      this.message = ''; // Limpa mensagens anteriores
      try {
        if (!this.$root.$keycloak || !this.$root.$keycloak.token) {
          this.message = 'Erro: Usuário não autenticado ou token indisponível.';
          this.isSuccess = false;
          console.error('Keycloak token não encontrado em this.$root.$keycloak');
          return;
        }

        const token = this.$root.$keycloak.token;
        const apiUrl = 'http://localhost:8081/api/v1/equipments';

        const response = await fetch(apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(this.equipment)
        });

        if (response.ok) {
          const savedEquipment = await response.json();
          this.message = `Equipamento "${savedEquipment.assetNumber}" salvo com sucesso! ID: ${savedEquipment.id}`;
          this.isSuccess = true;
          // Limpar o formulário
          this.equipment = {
            assetNumber: '',
            type: '',
            brand: '',
            model: '',
            acquisitionDate: '',
            status: 'Disponível'
          };
          // Opcional: Emitir um evento para o componente pai atualizar a lista de equipamentos
          this.$emit('equipment-added');
        } else {
          const errorData = await response.json().catch(() => ({ message: `Erro ${response.status}: Falha ao salvar equipamento.` }));
          this.message = errorData.message || `Erro ${response.status}: ${JSON.stringify(errorData)}`;
          this.isSuccess = false;
        }
      } catch (error) {
        console.error('Erro ao submeter formulário:', error);
        this.message = 'Erro de rede ou ao processar a requisição.';
        this.isSuccess = false;
      }
    }
  }
}
</script>

<style scoped>
.add-equipment-form {
  border: 1px solid #ccc;
  padding: 20px;
  margin-top: 20px;
  border-radius: 8px;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
}
.add-equipment-form div {
  margin-bottom: 10px;
}
.add-equipment-form label {
  display: block;
  margin-bottom: 5px;
  text-align: left;
}
.add-equipment-form input,
.add-equipment-form select {
  width: calc(100% - 22px); /* Ajuste para padding e borda */
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.add-equipment-form button {
  padding: 10px 15px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.add-equipment-form button:hover {
  background-color: #3aa070;
}
.message {
  margin-top: 15px;
  padding: 10px;
  border-radius: 4px;
}
.success {
  background-color: #e6ffed;
  color: #28a745;
  border: 1px solid #c3e6cb;
}
.error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
</style>