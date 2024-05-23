package ru.dilokil.hospital.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum AppointmentStatus {
    SCHEDULED(0),
    CANCELLED(1),
    COMPLETED(2),
    RESCHEDULED(3);

    private final int value;

    AppointmentStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AppointmentStatus fromValue(int value) {
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }

    @Converter(autoApply = true)
    public static class ConverterImpl implements AttributeConverter<AppointmentStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(AppointmentStatus status) {
            return status != null ? status.getValue() : null;
        }

        @Override
        public AppointmentStatus convertToEntityAttribute(Integer value) {
            return value != null ? AppointmentStatus.fromValue(value) : null;
        }
    }
}

