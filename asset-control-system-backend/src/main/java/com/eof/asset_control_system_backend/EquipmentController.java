package com.eof.asset_control_system_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        Equipment savedEquipment = equipmentService.saveEquipment(equipment);
        return new ResponseEntity<>(savedEquipment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> equipmentList = equipmentService.getAllEquipment();
        if (equipmentList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable Long id) {
        Optional<Equipment> equipmentOptional = equipmentService.getEquipmentById(id);

        if (equipmentOptional.isPresent()) {
            return ResponseEntity.ok(equipmentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipmentDetails) {
        Optional<Equipment> updatedEquipmentOptional = equipmentService.updateEquipment(id, equipmentDetails);

        if (updatedEquipmentOptional.isPresent()) {
            return ResponseEntity.ok(updatedEquipmentOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        boolean deleted = equipmentService.deleteEquipment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Equipment>> getAvailableEquipment() {
        List<Equipment> availableEquipment = equipmentService.getEquipmentByStatus("Disponível");
        if (availableEquipment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(availableEquipment);
    }
    @GetMapping("/awaiting-repair")
    public ResponseEntity<List<Equipment>> getAwaitingRepairEquipment() {
        List<Equipment> awaitingRepairEquipment = equipmentService.getEquipmentByStatus("Aguardando reparo");
        if (awaitingRepairEquipment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(awaitingRepairEquipment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Equipment>> getEquipmentByAllocatedUser(@PathVariable Long userId) {
        List<Equipment> equipmentList = equipmentService.getEquipmentAllocatedToUser(userId);
        if (equipmentList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(equipmentList);
    }
}