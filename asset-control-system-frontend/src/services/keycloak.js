import Keycloak from 'keycloak-js';


const keycloakInstance = new Keycloak({
    url: 'http://localhost:8080/', 
    realm: 'asset-control-realm',
    clientId: 'asset-control-frontend'
});

/**
 * Inicializa o Keycloak e retorna uma Promise.
 * A Promise é resolvida se a autenticação for bem-sucedida ou não for necessária.
 * A Promise é rejeitada se ocorrer um erro na inicialização ou autenticação.
 *
 * @param {object} options Opções de inicialização do Keycloak (ex: { onLoad: 'login-required' })
 * @returns Promise<boolean> Resolvida com 'true' se autenticado, 'false' caso contrário.
 */
const initKeycloak = (options) => {
    return new Promise((resolve, reject) => {
        keycloakInstance.init(options)
            .then((authenticated) => {
                console.log(`Keycloak: User is ${authenticated ? '' : 'NOT '}authenticated`);
                resolve(authenticated);
            })
            .catch((error) => {
                console.error('Keycloak: Failed to initialize', error);
                reject(error);
            });
    });
};

export { keycloakInstance, initKeycloak };