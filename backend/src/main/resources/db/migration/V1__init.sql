CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id VARCHAR(64) NOT NULL,
    type VARCHAR(32) NOT NULL,
    message VARCHAR(1024) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    priority_score INT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_notifications_student_read_created
    ON notifications (student_id, is_read, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_notifications_student_type_created
    ON notifications (student_id, type, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_notifications_priority
    ON notifications (student_id, priority_score DESC, created_at DESC);
