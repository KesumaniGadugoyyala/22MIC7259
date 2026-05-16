INSERT INTO notifications (id, student_id, type, message, is_read, priority_score, created_at)
VALUES
    (uuid_generate_v4(), 'S-1001', 'EVENT', 'Tech talk on cloud security at 4 PM in Auditorium A.', false, 1200, NOW() - INTERVAL '2 hours'),
    (uuid_generate_v4(), 'S-1001', 'RESULT', 'Semester 6 results are available in the portal.', false, 2200, NOW() - INTERVAL '1 day'),
    (uuid_generate_v4(), 'S-1001', 'PLACEMENT', 'Placement drive for Acme Corp starts tomorrow at 10 AM.', false, 3200, NOW() - INTERVAL '3 hours');
