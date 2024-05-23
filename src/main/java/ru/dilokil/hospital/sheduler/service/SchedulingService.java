package ru.dilokil.hospital.sheduler.service;

import org.springframework.stereotype.Service;
import ru.dilokil.hospital.model.Appointment;
import ru.dilokil.hospital.model.AppointmentStatus;
import ru.dilokil.hospital.model.Doctor;
import ru.dilokil.hospital.model.Patient;
import ru.dilokil.hospital.repository.AppointmentRepository;
import ru.dilokil.hospital.repository.DoctorRepository;
import ru.dilokil.hospital.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SchedulingService {

    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    public SchedulingService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public List<LocalTime[]> getAvailableIntervals(String doctorId, LocalDate date) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if (!doctorOpt.isPresent()) {
            throw new IllegalArgumentException("Doctor not found");
        }
        Doctor doctor = doctorOpt.get();

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentStartTimeBetween(
                doctorId, date.atStartOfDay(), date.atTime(23, 59, 59));

        List<LocalTime[]> availableIntervals = new ArrayList<>();

        LocalTime currentTime = doctor.getWorkStartTime();
        for (Appointment appointment : appointments) {
            LocalTime start = appointment.getAppointmentStartTime().toLocalTime();
            if (currentTime.isBefore(start)) {
                availableIntervals.add(new LocalTime[]{currentTime, start});
            }
            currentTime = appointment.getAppointmentEndTime().toLocalTime();
        }
        if (currentTime.isBefore(doctor.getWorkEndTime())) {
            availableIntervals.add(new LocalTime[]{currentTime, doctor.getWorkEndTime()});
        }

        return availableIntervals;
    }

    public Appointment scheduleAppointment(String patientId, String doctorId, LocalDateTime startTime, LocalDateTime endTime) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (!doctorOpt.isPresent() || !patientOpt.isPresent()) {
            throw new IllegalArgumentException(String.format("Doctor{%s} or Patient{%s} not found", doctorId, patientId));
        }
        Doctor doctor = doctorOpt.get();

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentStartTimeBetween(
                doctorId, startTime.toLocalDate().atStartOfDay(), startTime.toLocalDate().atTime(23, 59, 59));

        for (Appointment appointment : appointments) {
            if (startTime.isBefore(appointment.getAppointmentEndTime()) && endTime.isAfter(appointment.getAppointmentStartTime())) {
                throw new IllegalArgumentException(String.format("Time slot[%s - %s] is already occupied", startTime, endTime));
            }
        }

        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID().toString());
        appointment.setPatient(patientOpt.get());
        appointment.setDoctor(doctor);
        appointment.setAppointmentStartTime(startTime);
        appointment.setAppointmentEndTime(endTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(String appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (!appointmentOpt.isPresent()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        Appointment appointment = appointmentOpt.get();
        if (appointment.getAppointmentStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot delete past or ongoing appointment");
        }
        appointmentRepository.delete(appointment);
    }

    public Appointment rescheduleAppointment(String appointmentId, LocalDateTime newStartTime, LocalDateTime newEndTime) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (!appointmentOpt.isPresent()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        Appointment appointment = appointmentOpt.get();

        if (newStartTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reschedule to a past time");
        }

        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentStartTimeBetween(
                appointment.getDoctor().getId(), newStartTime.toLocalDate().atStartOfDay(), newStartTime.toLocalDate().atTime(23, 59, 59));

        for (Appointment otherAppointment : appointments) {
            if (!otherAppointment.getId().equals(appointmentId) &&
                    newStartTime.isBefore(otherAppointment.getAppointmentEndTime()) &&
                    newEndTime.isAfter(otherAppointment.getAppointmentStartTime())) {
                throw new IllegalArgumentException("New time slot is already occupied");
            }
        }

        appointment.setAppointmentStartTime(newStartTime);
        appointment.setAppointmentEndTime(newEndTime);

        return appointmentRepository.save(appointment);
    }
}

