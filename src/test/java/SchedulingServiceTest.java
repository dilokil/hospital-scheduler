
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.dilokil.hospital.model.Appointment;
import ru.dilokil.hospital.model.AppointmentStatus;
import ru.dilokil.hospital.model.Doctor;
import ru.dilokil.hospital.model.Patient;
import ru.dilokil.hospital.repository.AppointmentRepository;
import ru.dilokil.hospital.repository.DoctorRepository;
import ru.dilokil.hospital.repository.PatientRepository;
import ru.dilokil.hospital.sheduler.service.SchedulingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class SchedulingServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;

    private SchedulingService schedulingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        schedulingService = new SchedulingService(appointmentRepository, doctorRepository, patientRepository);
    }

    @Test
    void testGetAvailableIntervals() {
        String doctorId = "doctorId";
        LocalDate date = LocalDate.now();

        
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setWorkStartTime(LocalTime.of(9, 0));
        doctor.setWorkEndTime(LocalTime.of(17, 0));

        
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment(UUID.randomUUID().toString(), new Patient(), doctor,
                LocalDateTime.of(date, LocalTime.of(10, 0)),
                LocalDateTime.of(date, LocalTime.of(11, 0)), AppointmentStatus.SCHEDULED));
        appointments.add(new Appointment(UUID.randomUUID().toString(), new Patient(), doctor,
                LocalDateTime.of(date, LocalTime.of(13, 0)),
                LocalDateTime.of(date, LocalTime.of(14, 0)), AppointmentStatus.SCHEDULED));

        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndAppointmentStartTimeBetween(
                doctorId, date.atStartOfDay(), date.atTime(23, 59, 59))).thenReturn(appointments);

        List<LocalTime[]> availableIntervals = schedulingService.getAvailableIntervals(doctorId, date);

        assertEquals(3, availableIntervals.size());
        assertEquals(LocalTime.of(9, 0), availableIntervals.get(0)[0]);
        assertEquals(LocalTime.of(10, 0), availableIntervals.get(0)[1]);
        assertEquals(LocalTime.of(11, 0), availableIntervals.get(1)[0]);
        assertEquals(LocalTime.of(13, 0), availableIntervals.get(1)[1]);
    }

    @Test
    void testScheduleAppointment() {
        
        String doctorId = "doctorId";
        String patientId = "patientId";
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        Doctor doctor = new Doctor();
        doctor.setId(doctorId);

        Patient patient = new Patient();
        patient.setId(patientId);

        Appointment appointment = new Appointment();
        appointment.setId(UUID.randomUUID().toString());
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentStartTime(startTime);
        appointment.setAppointmentEndTime(endTime);

        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment savedAppointment = schedulingService.scheduleAppointment(doctorId, patientId, startTime, endTime);

        assertEquals(doctorId, savedAppointment.getDoctor().getId());
        assertEquals(patientId, savedAppointment.getPatient().getId());
        assertEquals(startTime, savedAppointment.getAppointmentStartTime());
        assertEquals(endTime, savedAppointment.getAppointmentEndTime());
    }

    @Test
    void testDeleteAppointment() {
        String appointmentId = "appointmentId";
        LocalDateTime now = LocalDateTime.now();

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setAppointmentStartTime(now.plusHours(1));

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        schedulingService.deleteAppointment(appointmentId);

        verify(appointmentRepository, times(1)).delete(appointment);
    }

    @Test
    void testRescheduleAppointment() {
        String appointmentId = "appointmentId";
        LocalDateTime newStartTime = LocalDateTime.now().plusHours(1);
        LocalDateTime newEndTime = newStartTime.plusHours(1);

        Doctor doctor = new Doctor();
        doctor.setId("doctorId");

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setAppointmentStartTime(newStartTime);
        appointment.setAppointmentEndTime(newEndTime);
        appointment.setDoctor(doctor);

        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));
        when(appointmentRepository.findByDoctorIdAndAppointmentStartTimeBetween(
                any(), any(), any())).thenReturn(new ArrayList<>());
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        Appointment rescheduledAppointment = schedulingService.rescheduleAppointment(appointmentId, newStartTime, newEndTime);

        assertEquals(newStartTime, rescheduledAppointment.getAppointmentStartTime());
        assertEquals(newEndTime, rescheduledAppointment.getAppointmentEndTime());
    }
}

