CREATE INDEX idx_task_assigned_by_status
    ON task (ASSIGNED_BY, status);