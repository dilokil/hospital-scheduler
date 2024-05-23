package ru.dilokil.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dilokil.hospital.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {
}

