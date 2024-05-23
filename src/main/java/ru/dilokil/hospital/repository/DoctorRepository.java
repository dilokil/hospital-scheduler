package ru.dilokil.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dilokil.hospital.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {
}

