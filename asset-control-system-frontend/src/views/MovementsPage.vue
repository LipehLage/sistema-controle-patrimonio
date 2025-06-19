<template>
  <v-container>
    <div class="header d-flex justify-space-between align-center">
      <h1>Histórico de Movimentações</h1>
      <v-btn color="primary" to="/movements/new">
        Registrar Movimentação
      </v-btn>
    </div>

    <v-progress-circular
      v-if="isLoading"
      indeterminate
      color="primary"
    ></v-progress-circular>

    <div v-if="apiError" class="error-message">
      <p>Erro ao carregar movimentações: {{ apiError }}</p>
    </div>

    <v-data-table
      :headers="movementHeaders"
      :items="movements"
      :loading="isLoading"
      class="elevation-1"
      item-key="id"
      loading-text="Carregando dados..."
      no-data-text="Nenhuma movimentação registrada."
    >
      <template v-slot:item.movementDate="{ item }">
        <span>{{ formatDateTime(item.movementDate) }}</span>
      </template>

      <template v-slot:item.equipment="{ item }">
        <span>{{ item.equipment.assetNumber }} - {{ item.equipment.type }}</span>
      </template>

      <template v-slot:item.appUser="{ item }">
        <span>{{ item.appUser.name }}</span>
      </template>

      <template v-slot:item.actions="{ item }">
        <v-btn size="small" icon="mdi-file-pdf-box" color="amber" @click="downloadTerm(item.id)"></v-btn>
      </template>
    </v-data-table>

  </v-container>
</template>

<script>
export default {
  name: 'MovementsPage',
  data() {
    return {
      movements: [],
      isLoading: false,
      apiError: null,
      isAuthenticated: false,
      movementHeaders: [
        { title: 'ID', key: 'id', align: 'start' },
        { title: 'Data', key: 'movementDate' },
        { title: 'Tipo', key: 'type' },
        { title: 'Equipamento', key: 'equipment' },
        { title: 'Usuário Responsável', key: 'appUser' },
        { title: 'Motivo', key: 'reason', sortable: false },
        { title: 'Ações', key: 'actions', sortable: false }
      ]
    };
  },
  mounted() {
    if (this.$keycloak) {
      this.isAuthenticated = this.$keycloak.authenticated;
    }
    this.fetchMovements();
  },
  methods: {
    async fetchMovements() {
      // ... (código do fetchMovements continua o mesmo)
      this.isLoading = true;
      this.apiError = null;
      try {
        if (!this.isAuthenticated || !this.$keycloak.token) {
          throw new Error("Usuário não autenticado ou token indisponível.");
        }
        const token = this.$keycloak.token;
        const apiUrl = 'http://localhost:8081/api/v1/movements';
        const response = await fetch(apiUrl, {
          method: 'GET',
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.status === 401) return this.$keycloak.login();
        if (response.status === 204) {
          this.movements = [];
        } else if (response.ok) {
          this.movements = await response.json();
        } else {
          const errorData = await response.json().catch(() => ({ message: `Erro HTTP ${response.status}` }));
          throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }
      } catch (error) {
        console.error('Erro ao buscar movimentações:', error);
        this.apiError = error.message;
      } finally {
        this.isLoading = false;
      }
    },
    formatDateTime(dateTimeString) {
      if (!dateTimeString) return 'N/A';
      const options = {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
      };
      return new Date(dateTimeString).toLocaleString('pt-BR', options);
    },
    async downloadTerm(movementId) {
      try {
        if (!this.$keycloak || !this.$keycloak.token) throw new Error("Não autenticado");

        const token = this.$keycloak.token;
        const apiUrl = `http://localhost:8081/api/v1/movements/${movementId}/term`;

        const response = await fetch(apiUrl, {
          headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 401) return this.$keycloak.login();
        if (!response.ok) throw new Error('Falha ao gerar o termo.');

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `termo_movimentacao_${movementId}.pdf`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(url);

      } catch (error) {
        console.error('Erro ao baixar o termo:', error);
        alert('Não foi possível baixar o termo.');
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
</style>