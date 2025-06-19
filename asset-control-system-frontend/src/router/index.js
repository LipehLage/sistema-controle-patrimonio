import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import EquipmentsPage from '../views/EquipmentsPage.vue';
import AddEquipmentPage from '../views/AddEquipmentPage';
import EditEquipmentPage from '../views/EditEquipmentPage.vue';
import UsersPage from '../views/UsersPage.vue';
import AddUserPage from '../views/AddUserPage.vue';
import EditUserPage from '../views/EditUserPage.vue';
import MovementsPage from '../views/MovementsPage.vue';
import RegisterMovementPage from '../views/RegisterMovementPage.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage
  },
  {
    path: '/equipments',
    name: 'Equipments',
    component: EquipmentsPage
  },
  {
    path: '/equipments/new',
    name: 'AddEquipment',
    component: AddEquipmentPage
  },
    {
    path: '/equipments/edit/:id',
    name: 'EditEquipment',
    component: EditEquipmentPage,
    props: true
  },
   {
    path: '/users',
    name: 'Users',
    component: UsersPage
  },
    {
    path: '/users/new',
    name: 'AddUser',
    component: AddUserPage
  },
    {
    path: '/users/edit/:id',
    name: 'EditUser',
    component: EditUserPage,
    props: true
  },
    { 
    path: '/movements',
    name: 'Movements',
    component: MovementsPage
  },
   {
    path: '/movements/new',
    name: 'RegisterMovement',
    component: RegisterMovementPage
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;