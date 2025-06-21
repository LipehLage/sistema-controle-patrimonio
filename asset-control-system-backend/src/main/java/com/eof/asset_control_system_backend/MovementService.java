package com.eof.asset_control_system_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final EquipmentRepository equipmentRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public MovementService(MovementRepository movementRepository,
                           EquipmentRepository equipmentRepository,
                           AppUserRepository appUserRepository) {
        this.movementRepository = movementRepository;
        this.equipmentRepository = equipmentRepository;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public Optional<Movement> registerMovement(Long equipmentId, Long appUserId, String movementType, String reason) {
        Optional<Equipment> equipmentOptional = equipmentRepository.findById(equipmentId);
        Optional<AppUser> appUserOptional = appUserRepository.findById(appUserId);

        if (equipmentOptional.isPresent() && appUserOptional.isPresent()) {
            Equipment equipment = equipmentOptional.get();
            AppUser appUser = appUserOptional.get();

            Movement movement = new Movement();
            movement.setEquipment(equipment);
            movement.setAppUser(appUser);
            movement.setMovementDate(LocalDateTime.now());
            movement.setType(movementType);
            movement.setReason(reason);

            if ("SAIDA_ALOCACAO".equalsIgnoreCase(movementType)) {
                equipment.setStatus("Em uso");
                equipment.setAllocatedToUser(appUser);
            } else if ("DEVOLUCAO".equalsIgnoreCase(movementType)) {
                equipment.setStatus("Disponível");
                equipment.setAllocatedToUser(null);
            } else if ("SAIDA_REPARO".equalsIgnoreCase(movementType)) {
                equipment.setStatus("Aguardando reparo");
                equipment.setAllocatedToUser(null);
            }

            equipmentRepository.save(equipment);
            Movement savedMovement = movementRepository.save(movement);
            return Optional.of(savedMovement);
        } else {
            return Optional.empty();
        }
    }

    public List<Movement> getAllMovements() {
        return movementRepository.findAll();
    }
}