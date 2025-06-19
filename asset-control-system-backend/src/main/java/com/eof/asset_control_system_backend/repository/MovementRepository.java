package com.eof.asset_control_system_backend.repository;

import com.eof.asset_control_system_backend.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

}