import { createApp } from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './router'
import { loadFonts } from './plugins/webfontloader'
import { initKeycloak, keycloakInstance } from './services/keycloak'

loadFonts()

const keycloakOptions = {
    onLoad: 'login-required',
    checkLoginIframe: false
};

initKeycloak(keycloakOptions)
    .then((authenticated) => {
        console.log("Resultado da autenticação no main.js:", authenticated);
        
        const app = createApp(App);
        
        app.use(router);
        app.use(vuetify);
        
        app.config.globalProperties.$keycloak = keycloakInstance;
        
        app.mount('#app');
    })
    .catch((error) => {

        console.error("Keycloak initialization failed in main.js:", error);
        
        const errorApp = createApp({
            template: '<div><h1>Erro ao inicializar o sistema de autenticação</h1><p>Por favor, tente novamente mais tarde.</p></div>'
        });
        
        errorApp.use(vuetify); 
        errorApp.mount('#app');
    });