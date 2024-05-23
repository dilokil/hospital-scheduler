package ru.dilokil.hospital.service;

import org.springframework.stereotype.Service;
import ru.dilokil.hospital.model.Appointment;
import ru.dilokil.hospital.repository.AppointmentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(Appointment appointment) {
        appointment.setId(UUID.randomUUID().toString());
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(String id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public Appointment updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(String id) {
        appointmentRepository.deleteById(id);
    }
}

