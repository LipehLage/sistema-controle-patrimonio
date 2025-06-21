package com.eof.asset_control_system_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movements")
public class MovementController {

    private final MovementService movementService;

    @Autowired
    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerMovement(@RequestBody MovementRequestPayload payload) {
        Optional<Movement> movementOptional = movementService.registerMovement(
                payload.equipmentId(),
                payload.appUserId(),
                payload.movementType(),
                payload.reason()
        );

        if (movementOptional.isPresent()) {
            return new ResponseEntity<>(movementOptional.get(), HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body("Failed to register movement. Equipment or User not found.");
        }
    }
    @GetMapping
    public ResponseEntity<List<Movement>> getAllMovements() {
        List<Movement> movements = movementService.getAllMovements();
        if (movements.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(movements);
    }

}