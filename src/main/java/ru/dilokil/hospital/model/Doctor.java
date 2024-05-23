package ru.dilokil.hospital.model;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    private String id;
    private String name;
    private String specialization;
    private String contactInfo;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;
}

