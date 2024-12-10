package com.threadmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.threadmanager.service.QueueService;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
    private final QueueService queueService;

    @Autowired
    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @Operation(
        summary = "Get queue status",
        description = "Returns the current status of the queue"
    )
    @ApiResponse(responseCode = "200", description = "Queue status fetched successfully")
    @GetMapping("/status")
    public ResponseEntity<QueueStatus> getQueueStatus() {
        return ResponseEntity.ok(new QueueStatus(
            queueService.getQueueSize(),
            queueService.getMaxQueueSize(),
            queueService.getOccupancyRate()
        ));
    }

    @Operation(
        summary = "Clear queue",
        description = "Clears all messages from the queue"
    )
    @ApiResponse(responseCode = "200", description = "Queue cleared successfully")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearQueue() {
        queueService.clearQueue();
        return ResponseEntity.ok("Queue cleared successfully");
    }
}

@Data
@AllArgsConstructor
class QueueStatus {
    private int currentSize;
    private int maxSize;
    private double occupancyRate;
}