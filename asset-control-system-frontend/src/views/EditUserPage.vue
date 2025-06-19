<template>
  <v-container>
    <div class="d-flex justify-space-between align-center" style="margin-bottom: 20px;">
      <h1 v-if="user">Editando Usuário: {{ user.name }}</h1>
      <h1 v-else>Carregando Usuário...</h1>
      <v-btn prepend-icon="mdi-arrow-left" to="/users">
        Voltar para a Lista
      </v-btn>
    </div>

    <v-progress-circular v-if="isLoading" indeterminate color="primary"></v-progress-circular>

    <div v-if="apiError" class="error-message">
      <p>{{ apiError }}</p>
    </div>

    <v-form v-if="user" @submit.prevent="handleSubmit">
      <v-text-field
        v-model="user.name"
        label="Nome"
        :rules="[rules.required]"
        variant="outlined"
      ></v-text-field>

      <v-text-field
        v-model="user.department"
        label="Departamento"
        :rules="[rules.required]"
        variant="outlined"
      ></v-text-field>

      <v-text-field
        v-model="user.email"
        label="E-mail"
        type="email"
        :rules="[rules.required, rules.email]"
        variant="outlined"
      ></v-text-field>

      <v-btn type="submit" color="primary" :loading="isSaving">
        Salvar Alterações
      </v-btn>
    </v-form>

  </v-container>
</template>

<script>
export default {
  name: 'EditUserPage',
  props: ['id'],
  data() {
    return {
      user: null,
      isLoading: false,
      isSaving: false,
      apiError: null,
      rules: {
        required: value => !!value || 'Campo obrigatório.',
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || 'E-mail inválido.';
        },
      }
    };
  },
  async created() {
    await this.fetchUserDetails();
  },
  methods: {
    async fetchUserDetails() {
      this.isLoading = true;
      this.apiError = null;
      try {
        if (!this.$keycloak || !this.$keycloak.token) throw new Error("Não autenticado");

        const token = this.$keycloak.token;
        const apiUrl = `http://localhost:8081/api/v1/users/${this.id}`;
        const response = await fetch(apiUrl, {
          headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 401) return this.$keycloak.login();
        if (!response.ok) throw new Error(`Erro ao buscar detalhes do usuário: ${response.statusText}`);

        this.user = await response.json();
      } catch (error) {
        this.apiError = error.message;
        console.error(error);
      } finally {
        this.isLoading = false;
      }
    },
    async handleSubmit() {
      this.isSaving = true;
      this.apiError = null;
      try {
        if (!this.$keycloak || !this.$keycloak.token) throw new Error("Não autenticado");

        const token = this.$keycloak.token;
        const apiUrl = `http://localhost:8081/api/v1/users/${this.id}`;
        const response = await fetch(apiUrl, {
          method: 'PUT',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(this.user)
        });

        if (response.status === 401) return this.$keycloak.login();
        if (!response.ok) throw new Error(`Erro ao atualizar usuário: ${response.statusText}`);

        alert('Usuário atualizado com sucesso!');
        this.$router.push('/users');
      } catch (error) {
        this.apiError = error.message;
        console.error(error);
      } finally {
        this.isSaving = false;
      }
    }
  }
}
</script>

<style scoped>
.error-message {
  color: red;
  margin-top: 20px;
}
</style>