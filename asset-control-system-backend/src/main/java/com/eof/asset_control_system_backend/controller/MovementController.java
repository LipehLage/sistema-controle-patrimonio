package com.eof.asset_control_system_backend.controller;

import com.eof.asset_control_system_backend.model.Movement;
import com.eof.asset_control_system_backend.dto.MovementRequestPayload;
import com.eof.asset_control_system_backend.service.MovementService;
import com.eof.asset_control_system_backend.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movements")
public class MovementController {

    private final MovementService movementService;
    private final PdfGenerationService pdfGenerationService;

    @Autowired
    public MovementController(MovementService movementService, PdfGenerationService pdfGenerationService) {
        this.movementService = movementService;
        this.pdfGenerationService = pdfGenerationService;
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

    @GetMapping("/{id}/term")
    public ResponseEntity<byte[]> generateMovementTerm(@PathVariable Long id) {
        Optional<Movement> movementOptional = movementService.getMovementById(id);

        if (movementOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] pdfContents = pdfGenerationService.generateTermPdf(movementOptional.get());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "termo_movimentacao_" + id + ".pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}