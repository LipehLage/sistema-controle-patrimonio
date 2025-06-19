<template>
  <v-container>
    <div class="header d-flex justify-space-between align-center">
      <h1>Gerenciamento de Usuários</h1>
      <v-btn v-if="isAuthenticated && $keycloak.hasRealmRole('ROLE_ADMIN')" color="primary" to="/users/new">
        Adicionar Usuário
      </v-btn>
    </div>

    <div v-if="apiError" class="error-message">
      <p>Erro ao carregar usuários: {{ apiError }}</p>
    </div>

    <v-data-table
      :headers="userHeaders"
      :items="users"
      :loading="isLoading"
      class="elevation-1"
      item-key="id"
      loading-text="Carregando dados..."
      no-data-text="Nenhum usuário encontrado na aplicação."
    >
      <template v-slot:item.actions="{ item }">
        <v-btn size="small" icon="mdi-pencil" color="info" class="mr-2" :to="`/users/edit/${item.id}`"></v-btn>
        <v-btn size="small" icon="mdi-delete" color="error" @click="confirmDelete(item)"></v-btn>
      </template>
    </v-data-table>

  </v-container>
</template>

<script>
export default {
  name: 'UsersPage',
  data() {
    return {
      users: [],
      isLoading: false,
      apiError: null,
      isAuthenticated: false,
      userHeaders: [
        { title: 'ID', key: 'id', align: 'start' },
        { title: 'Nome', key: 'name', align: 'start' },
        { title: 'Departamento', key: 'department', align: 'start' },
        { title: 'E-mail', key: 'email', align: 'start' },
      ]
    };
  },
  mounted() {
    if (this.$keycloak) {
      this.isAuthenticated = this.$keycloak.authenticated;
      if (this.isAuthenticated && this.$keycloak.hasRealmRole('ROLE_ADMIN')) {
        this.userHeaders.push({ title: 'Ações', key: 'actions', align: 'start', sortable: false });
      }
    }
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      this.isLoading = true;
      this.apiError = null;
      try {
        if (!this.isAuthenticated || !this.$keycloak.token) {
          throw new Error("Usuário não autenticado ou token indisponível.");
        }
        const token = this.$keycloak.token;
        const apiUrl = 'http://localhost:8081/api/v1/users';
        const response = await fetch(apiUrl, {
          method: 'GET',
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.status === 401) return this.$keycloak.login();
        if (response.status === 204) {
          this.users = [];
        } else if (response.ok) {
          this.users = await response.json();
        } else {
          const errorData = await response.json().catch(() => ({ message: `Erro HTTP ${response.status}` }));
          throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }
      } catch (error) {
        console.error('Erro ao buscar usuários:', error);
        this.apiError = error.message;
      } finally {
        this.isLoading = false;
      }
    },
    confirmDelete(user) {
      if (window.confirm(`Tem certeza que deseja deletar o usuário "${user.name}"?`)) {
        this.deleteUser(user.id);
      }
    },
    async deleteUser(id) {
      this.apiError = null;
      try {
        if (!this.isAuthenticated || !this.$keycloak.token) {
          throw new Error("Usuário não autenticado.");
        }
        const token = this.$keycloak.token;
        const apiUrl = `http://localhost:8081/api/v1/users/${id}`;
        const response = await fetch(apiUrl, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.status === 401) return this.$keycloak.login();
        if (response.ok) {
          alert('Usuário deletado com sucesso!');
          this.fetchUsers();
        } else {
          const errorData = await response.json().catch(() => ({ message: `Erro HTTP ${response.status}` }));
          throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }
      } catch (error) {
        console.error('Erro ao deletar usuário:', error);
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
.user-table {
  margin-top: 20px;
}
.error-message {
  color: red;
  margin-top: 20px;
}
</style>