package ru.dilokil.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dilokil.hospital.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByDoctorIdAndAppointmentStartTimeBetween(
            String doctorId,
            LocalDateTime startInterval,
            LocalDateTime endDateTime
    );
}

