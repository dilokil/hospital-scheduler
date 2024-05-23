package ru.dilokil.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dilokil.hospital.model.Appointment;
import ru.dilokil.hospital.service.AppointmentService;
import ru.dilokil.hospital.service.DoctorService;
import ru.dilokil.hospital.service.PatientService;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentController(AppointmentService appointmentService, DoctorService doctorService,
                                 PatientService patientService) {
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        appointment.setDoctor(doctorService.getDoctorById(appointment.getDoctor().getId()));
        appointment.setPatient(patientService.getPatientById(appointment.getPatient().getId()));
        return appointmentService.createAppointment(appointment);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable String id) {
        return appointmentService.getAppointmentById(id);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable String id, @RequestBody Appointment appointment) {
        appointment.setId(id);
        return appointmentService.updateAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable String id) {
        appointmentService.deleteAppointment(id);
    }
}

