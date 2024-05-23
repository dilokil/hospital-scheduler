package ru.dilokil.hospital.service;

import org.springframework.stereotype.Service;
import ru.dilokil.hospital.model.Patient;
import ru.dilokil.hospital.repository.PatientRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient createPatient(Patient patient) {
        patient.setId(UUID.randomUUID().toString());
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(String id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
    }
}


