<template>
  <v-container>
    <div class="header d-flex justify-space-between align-center">
      <h1>Gerenciamento de Equipamentos</h1>
      <v-btn v-if="isAuthenticated && $keycloak.hasRealmRole('ROLE_ADMIN')" color="primary" to="/equipments/new">
        Adicionar Equipamento
      </v-btn>
    </div>

    <div v-if="apiError" class="error-message">
      <p>Erro ao carregar equipamentos: {{ apiError }}</p>
    </div>

    <v-data-table
      :headers="equipmentHeaders"
      :items="equipments"
      :loading="isLoading"
      class="elevation-1"
      item-key="id"
      loading-text="Carregando dados..."
      no-data-text="Nenhum equipamento encontrado. Cadastre o primeiro!"
    >
      <template v-slot:item.allocatedToUser="{ item }">
        <span>{{ item.allocatedToUser ? item.allocatedToUser.name : 'N/A' }}</span>
      </template>
      
      <template v-if="isAuthenticated && $keycloak.hasRealmRole('ROLE_ADMIN')" v-slot:item.actions="{ item }">
        <v-btn size="small" icon="mdi-pencil" color="info" class="mr-2" :to="`/equipments/edit/${item.id}`"></v-btn>
        <v-btn size="small" icon="mdi-delete" color="error" @click="confirmDelete(item)"></v-btn>
      </template>
    </v-data-table>

  </v-container>
</template>

<script>
export default {
  name: 'EquipmentsPage',
  data() {
    return {
      equipments: [],
      isLoading: false,
      apiError: null,
      isAuthenticated: false,
      equipmentHeaders: [
        { title: 'ID', key: 'id', align: 'start', sortable: true },
        { title: 'Nº Patrimônio', key: 'assetNumber', align: 'start', sortable: true },
        { title: 'Tipo', key: 'type', align: 'start', sortable: true },
        { title: 'Marca', key: 'brand', align: 'start', sortable: true },
        { title: 'Modelo', key: 'model', align: 'start', sortable: true },
        { title: 'Status', key: 'status', align: 'start', sortable: true },
        { title: 'Alocado para', key: 'allocatedToUser', align: 'start', sortable: false },
      ]
    };
  },
  mounted() {
    if (this.$keycloak) {
        this.isAuthenticated = this.$keycloak.authenticated;
        if (this.isAuthenticated && this.$keycloak.hasRealmRole('ROLE_ADMIN')) {
          this.equipmentHeaders.push({ title: 'Ações', key: 'actions', align: 'start', sortable: false });
        }
    }
    this.fetchEquipments();
  },
  methods: {
    async fetchEquipments() {
      this.isLoading = true;
      this.apiError = null;
      try {
        if (!this.$keycloak || !this.$keycloak.token) {
          throw new Error("Usuário não autenticado ou token indisponível.");
        }
        const token = this.$keycloak.token;
        const apiUrl = 'http://localhost:8081/api/v1/equipments';
        const response = await fetch(apiUrl, {
          method: 'GET',
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.status === 401) {
          this.$keycloak.login();
          return;
        }
        if (response.status === 204) {
          this.equipments = [];
        } else if (response.ok) {
          this.equipments = await response.json();
        } else {
          const errorData = await response.json().catch(() => ({ message: `Erro HTTP ${response.status}` }));
          throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }
      } catch (error) {
        console.error('Erro ao buscar equipamentos:', error);
        this.apiError = error.message;
      } finally {
        this.isLoading = false;
      }
    },
    confirmDelete(equipment) {
      if (window.confirm(`Tem certeza que deseja deletar o equipamento "${equipment.assetNumber}"?`)) {
        this.deleteEquipment(equipment.id);
      }
    },
    async deleteEquipment(id) {
      this.apiError = null;
      try {
        if (!this.$keycloak || !this.$keycloak.token) {
          throw new Error("Usuário não autenticado ou token indisponível.");
        }
        const token = this.$keycloak.token;
        const apiUrl = `http://localhost:8081/api/v1/equipments/${id}`;
        const response = await fetch(apiUrl, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        if (response.status === 401) {
          this.$keycloak.login();
          return;
        }
        if (response.ok) {
          alert('Equipamento deletado com sucesso!');
          this.fetchEquipments();
        } else {
          const errorData = await response.json().catch(() => ({ message: `Erro HTTP ${response.status}` }));
          throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }
      } catch (error) {
        console.error('Erro ao deletar equipamento:', error);
        this.apiError = error.message;
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