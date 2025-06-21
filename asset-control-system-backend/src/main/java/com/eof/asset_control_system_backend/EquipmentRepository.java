package com.eof.asset_control_system_backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByStatus(String status);
    List<Equipment> findByAllocatedToUserId(Long userId);

}