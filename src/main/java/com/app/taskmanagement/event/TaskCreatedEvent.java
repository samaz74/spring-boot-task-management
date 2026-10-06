package com.app.taskmanagement.event;

import com.app.taskmanagement.model.Task;

public record TaskCreatedEvent(
        Task task
) {
}
