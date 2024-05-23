create table patient (
    id varchar(255) primary key,
    name varchar(255) not null,
    age int not null,
    gender varchar(50) not null,
    contact_info varchar(255) not null
);

create table doctor (
    id varchar(255) primary key,
    name varchar(255) not null,
    specialization varchar(255) not null,
    contact_info varchar(255) not null,
    work_start_time time not null,
    work_end_time time not null
);

create table appointment (
    id varchar(255) primary key,
    patient_id varchar(255) not null,
    doctor_id varchar(255) not null,
    appointment_start_time timestamp not null,
    appointment_end_time timestamp not null,
    status int not null,
    constraint fk_appointments_patient foreign key (patient_id) references patients(id),
    constraint fk_appointments_doctor foreign key (doctor_id) references doctors(id)
);
