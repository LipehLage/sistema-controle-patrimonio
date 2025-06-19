<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <v-toolbar-title>Sistema de Controle de Patrimônio</v-toolbar-title>
      <v-spacer></v-spacer>

      <v-btn text to="/">Home</v-btn>
      <v-btn text to="/equipments">Equipamentos</v-btn>
      <v-btn text to="/users">Usuários</v-btn>
      <v-btn text to="/movements">Movimentações</v-btn>

      <v-spacer></v-spacer> 
      <div v-if="isKeycloakReady && isAuthenticated" class="user-info">
        Olá, {{ userInfo.name || userInfo.preferred_username }}
        <v-btn @click="logoutKeycloak" icon>
          <v-icon>mdi-logout</v-icon>
        </v-btn>
      </div>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      isAuthenticated: false,
      isKeycloakReady: false,
      userInfo: {}
      // Remova apiResponse e apiError daqui, pois a listagem de equipamentos
      // será responsabilidade de outro componente/view.
    };
  },
  mounted() {
    this.isKeycloakReady = !!this.$keycloak; // Verifica se $keycloak existe
    if (this.isKeycloakReady) {
      this.isAuthenticated = this.$keycloak.authenticated;
      if (this.isAuthenticated) {
        this.$keycloak.loadUserInfo().then(info => {
          this.userInfo = info;
        }).catch(err => {
          console.error("Falha ao carregar informações do usuário:", err);
          this.userInfo = { preferred_username: 'usuário' };
        });
      }
    } else {
      console.error("Instância do Keycloak não encontrada em this.$keycloak em App.vue mounted()");
    }
  },
  methods: {
    logoutKeycloak() {
      if (this.isKeycloakReady) {
        const postLogoutRedirectUri = window.location.origin;
        this.$keycloak.logout({ redirectUri: postLogoutRedirectUri });
      } else {
        console.error("Keycloak não está pronto para logout.");
      }
    }
    // Remova o método callApiListEquipments daqui, ele irá para um componente específico.
    // Remova handleEquipmentAdded daqui também.
  }
}
</script>

<style>
/* Você pode adicionar estilos globais aqui ou em um arquivo CSS separado importado no main.js */
/* O Vuetify já traz muitos estilos, então talvez precise de pouco aqui inicialmente. */
.user-info {
  display: flex;
  align-items: center;
  gap: 10px; /* Espaço entre o nome e o botão de logout */
  margin-right: 16px;
}
</style>