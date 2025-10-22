package org.example.backend.repository;

import org.example.backend.entity.MpsRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MpsRecordRepository extends JpaRepository<MpsRecord, Long> {
}
