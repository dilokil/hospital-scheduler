package ru.dilokil.hospital.sheduler.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dilokil.hospital.model.Appointment;
import ru.dilokil.hospital.sheduler.service.SchedulingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/scheduling")
public class SchedulingController {

    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/available-intervals")
    public List<LocalTime[]> getAvailableIntervals(
            @RequestParam String doctorId,
            @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return schedulingService.getAvailableIntervals(doctorId, localDate);
    }

    @PostMapping("/schedule")
    public Appointment scheduleAppointment(
            @RequestParam String patientId,
            @RequestParam String doctorId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        return schedulingService.scheduleAppointment(patientId, doctorId, start, end);
    }

    @DeleteMapping("/cancel/{appointmentId}")
    public void deleteAppointment(@PathVariable String appointmentId) {
        schedulingService.deleteAppointment(appointmentId);
    }

    @PutMapping("/reschedule/{appointmentId}")
    public Appointment rescheduleAppointment(
            @PathVariable String appointmentId,
            @RequestParam String newStartTime,
            @RequestParam String newEndTime) {
        LocalDateTime newStart = LocalDateTime.parse(newStartTime);
        LocalDateTime newEnd = LocalDateTime.parse(newEndTime);
        return schedulingService.rescheduleAppointment(appointmentId, newStart, newEnd);
    }
}
