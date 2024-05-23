package ru.dilokil.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dilokil.hospital.model.Doctor;
import ru.dilokil.hospital.service.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable String id) {
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable String id, @RequestBody Doctor doctor) {
        doctor.setId(id);
        return doctorService.updateDoctor(doctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable String id) {
        doctorService.deleteDoctor(id);
    }
}
