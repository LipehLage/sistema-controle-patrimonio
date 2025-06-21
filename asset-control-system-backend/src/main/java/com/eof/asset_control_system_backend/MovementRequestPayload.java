package com.eof.asset_control_system_backend;

public record MovementRequestPayload(
        Long equipmentId,
        Long appUserId,
        String movementType,
        String reason
) {
}