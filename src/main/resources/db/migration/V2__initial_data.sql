-- Insert initial patients
insert into patient (id, name, age, gender, contact_info) values
('11111111-1111-1111-1111-111111111111', 'John Doe', 30, 'Male', 'john.doe@example.com'),
('22222222-2222-2222-2222-222222222222', 'Jane Smith', 28, 'Female', 'jane.smith@example.com');

-- Insert initial doctors
insert into doctor (id, name, specialization, contact_info, work_start_time, work_end_time) values
('33333333-3333-3333-3333-333333333333', 'Dr. Alice Brown', 'Cardiology', 'alice.brown@example.com', '08:00:00', '16:00:00'),
('44444444-4444-4444-4444-444444444444', 'Dr. Bob White', 'Neurology', 'bob.white@example.com', '09:00:00', '17:00:00');
