package ru.dilokil.hospital.service;

import org.springframework.stereotype.Service;
import ru.dilokil.hospital.model.Doctor;
import ru.dilokil.hospital.repository.DoctorRepository;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {
    private DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor createDoctor(Doctor doctor) {
        doctor.setId(UUID.randomUUID().toString());
        return doctorRepository.save(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(String id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public Doctor updateDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }
}
