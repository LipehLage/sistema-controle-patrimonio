package com.eof.asset_control_system_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public Equipment saveEquipment(Equipment equipment) {

        return equipmentRepository.save(equipment);
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    public List<Equipment> getEquipmentByStatus(String status) {
        return equipmentRepository.findByStatus(status);
    }

    public Optional<Equipment> updateEquipment(Long id, Equipment equipmentDetails) {
        Optional<Equipment> equipmentOptional = equipmentRepository.findById(id);

        if (equipmentOptional.isPresent()) {
            Equipment existingEquipment = equipmentOptional.get();


            existingEquipment.setAssetNumber(equipmentDetails.getAssetNumber());
            existingEquipment.setType(equipmentDetails.getType());
            existingEquipment.setBrand(equipmentDetails.getBrand());
            existingEquipment.setModel(equipmentDetails.getModel());
            existingEquipment.setAcquisitionDate(equipmentDetails.getAcquisitionDate());
            existingEquipment.setStatus(equipmentDetails.getStatus());

            return Optional.of(equipmentRepository.save(existingEquipment));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteEquipment(Long id) {

        if (equipmentRepository.existsById(id)) {
            equipmentRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public List<Equipment> getEquipmentAllocatedToUser(Long userId) {
        return equipmentRepository.findByAllocatedToUserId(userId);
    }

}