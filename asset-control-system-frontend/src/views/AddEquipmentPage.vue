<template>
  <v-container>
    <div class="d-flex justify-space-between align-center" style="margin-bottom: 20px;">
      <h1>Adicionar Novo Equipamento</h1>
      <v-btn prepend-icon="mdi-arrow-left" to="/equipments">
        Voltar para a Lista
      </v-btn>
    </div>
    <add-equipment-form @equipment-added="goToEquipmentsList" />
  </v-container>
</template>

<script>
import AddEquipmentForm from '@/components/AddEquipmentForm.vue';

export default {
  name: 'AddEquipmentPage',
  components: {
    AddEquipmentForm
  },
  methods: {
    goToEquipmentsList() {
      this.$router.push('/equipments');
    }
  }
}
</script>