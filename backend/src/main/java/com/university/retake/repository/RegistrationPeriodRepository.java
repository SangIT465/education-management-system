package com.university.retake.repository;

import com.university.retake.entity.RegistrationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, UUID> {

    @Query("SELECT rp FROM RegistrationPeriod rp " +
           "WHERE rp.isOpen = true " +
           "AND rp.isActive = true " +
           "ORDER BY rp.startTime ASC")
    List<RegistrationPeriod> findAllOpen();
}
