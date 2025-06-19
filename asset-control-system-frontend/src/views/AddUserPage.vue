<template>
  <v-container>
    <div class="header d-flex justify-space-between align-center">
      <h1>Adicionar Novo Usuário</h1>
      <v-btn prepend-icon="mdi-arrow-left" to="/users">
        Voltar para a Lista
      </v-btn>
    </div>

    <v-form @submit.prevent="handleSubmit" class="mt-4">
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
        Salvar Usuário
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
  name: 'AddUserPage',
  data() {
    return {
      user: {
        name: '',
        department: '',
        email: ''
      },
      message: '',
      isSuccess: false,
      isSaving: false,
      rules: {
        required: value => !!value || 'Campo obrigatório.',
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || 'E-mail inválido.';
        },
      }
    };
  },
  methods: {
    async handleSubmit() {
      this.message = '';
      this.isSuccess = false;
      this.isSaving = true;

      try {
        if (!this.$keycloak || !this.$keycloak.token) {
          throw new Error("Usuário não autenticado.");
        }

        const token = this.$keycloak.token;
        const apiUrl = 'http://localhost:8081/api/v1/users';

        const response = await fetch(apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(this.user)
        });

        if (response.status === 401) return this.$keycloak.login();

        if (response.ok) {
          const savedUser = await response.json();
          this.message = `Usuário "${savedUser.name}" salvo com sucesso!`;
          this.isSuccess = true;
          this.$router.push('/users'); // Redireciona imediatamente
        } else {
          const errorData = await response.json().catch(() => ({ message: 'Falha ao salvar usuário.' }));
          throw new Error(errorData.message || `Erro HTTP ${response.status}`);
        }

      } catch (error) {
        console.error('Erro ao submeter formulário de usuário:', error);
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
.error-message {
  color: red;
  margin-top: 20px;
}
</style>