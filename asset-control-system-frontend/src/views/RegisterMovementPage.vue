<template>
  <v-container>
    <div class="header d-flex justify-space-between align-center">
      <h1>Registrar Nova Movimentação</h1>
      <v-btn prepend-icon="mdi-arrow-left" to="/movements">
        Voltar para o Histórico
      </v-btn>
    </div>

    <v-progress-circular
      v-if="isLoading"
      indeterminate
      color="primary"
      class="my-4"
    ></v-progress-circular>

    <div v-if="apiError" class="error-message">
      <p>{{ apiError }}</p>
    </div>

    <v-form @submit.prevent="handleSubmit" v-if="!isLoading && !apiError">
      <v-select
        v-model="movement.equipmentId"
        :items="allEquipments"
        item-title="assetNumber"
        item-value="id"
        label="Equipamento (Nº Patrimônio)"
        :rules="[rules.required]"
        variant="outlined"
        no-data-text="Nenhum equipamento encontrado"
      ></v-select>

      <v-select
        v-model="movement.appUserId"
        :items="allUsers"
        item-title="name"
        item-value="id"
        label="Usuário Responsável"
        :rules="[rules.required]"
        variant="outlined"
        no-data-text="Nenhum usuário encontrado"
      ></v-select>

      <v-select
        v-model="movement.movementType"
        :items="movementTypes"
        label="Tipo de Movimentação"
        :rules="[rules.required]"
        variant="outlined"
      ></v-select>

      <v-textarea
        v-model="movement.reason"
        label="Motivo / Observação"
        variant="outlined"
        rows="3"
      ></v-textarea>

      <v-btn type="submit" color="primary" :loading="isSaving">
        Registrar Movimentação
      </v-btn>
    </v-form>

    <v-alert
      v-if="message"
      :type="isSuccess ? 'success' : 'error'"
      class="mt-4"
      closable
    >
      {{ message }}
    </v-alert>

  </v-container>
</template>

<script>
export default {
  name: 'RegisterMovementPage',
  data() {
    return {
      isLoading: true,
      isSaving: false,
      apiError: null,
      message: '',
      isSuccess: false,
      movement: {
        equipmentId: null,
        appUserId: null,
        movementType: 'SAIDA_ALOCACAO', // Valor padrão
        reason: ''
      },
      allEquipments: [],
      allUsers: [],
      movementTypes: ['SAIDA_ALOCACAO', 'DEVOLUCAO', 'SAIDA_REPARO'],
      rules: {
        required: value => !!value || 'Campo obrigatório.',
      }
    };
  },
  async created() {
    await this.fetchSelectableData();
  },
  methods: {
    async fetchSelectableData() {
      this.isLoading = true;
      this.apiError = null;
      try {
        if (!this.$keycloak || !this.$keycloak.token) throw new Error("Não autenticado");
        const token = this.$keycloak.token;

        const equipmentUrl = 'http://localhost:8081/api/v1/equipments';
        const usersUrl = 'http://localhost:8081/api/v1/users';

        const [equipmentResponse, usersResponse] = await Promise.all([
          fetch(equipmentUrl, { headers: { 'Authorization': `Bearer ${token}` } }),
          fetch(usersUrl, { headers: { 'Authorization': `Bearer ${token}` } })
        ]);

        if (equipmentResponse.status === 401 || usersResponse.status === 401) return this.$keycloak.login();
        if (!equipmentResponse.ok) throw new Error('Falha ao buscar equipamentos');
        if (!usersResponse.ok) throw new Error('Falha ao buscar usuários');

        if (equipmentResponse.status !== 204) {
          this.allEquipments = await equipmentResponse.json();
        }
        if (usersResponse.status !== 204) {
          this.allUsers = await usersResponse.json();
        }

      } catch (error) {
        console.error('Erro ao buscar dados para o formulário:', error);
        this.apiError = error.message;
      } finally {
        this.isLoading = false;
      }
    },
    async handleSubmit() {
      this.isSaving = true;
      this.message = '';
      this.isSuccess = false;
      try {
        if (!this.$keycloak || !this.$keycloak.token) throw new Error("Não autenticado.");

        const token = this.$keycloak.token;
        const apiUrl = 'http://localhost:8081/api/v1/movements/register';

        const response = await fetch(apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(this.movement)
        });

        if (response.status === 401) return this.$keycloak.login();

        if (response.ok) {
          this.message = 'Movimentação registrada com sucesso!';
          this.isSuccess = true;
          setTimeout(() => this.$router.push('/movements'), 2000);
        } else {
          const errorData = await response.json().catch(() => ({ message: 'Falha ao registrar movimentação.' }));
          throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }
      } catch (error) {
        console.error('Erro ao submeter movimentação:', error);
        this.message = error.message;
        this.isSuccess = false;
      } finally {
        this.isSaving = false;
      }
    }
  }
}
</script>

<style scoped>
.header {
  margin-bottom: 20px;
}
.error-message {
  color: red;
  margin-top: 20px;
}
.message {
  margin-top: 15px;
}
</style>