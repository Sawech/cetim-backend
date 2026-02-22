package com.cetim.labs.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cetim.labs.dto.AssignDTO;
import com.cetim.labs.model.Assign;
import com.cetim.labs.service.AssignService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/assigns")
public class AssignController {

    @Autowired
    private AssignService assignService;

    // Store active emitters (in-memory, consider Redis for production)
    private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    // Notify user when an assignment is created (modify saveAssign)
    @PostMapping
    public ResponseEntity<Assign> saveAssign(@RequestBody AssignDTO assignDTO) {
        Assign assign = new Assign();
        assign.setService(assignDTO.getService());
        assign.setFicheDessaiId(assignDTO.getFicheDessaiId());
        assign.setUserId(assignDTO.getUserId());
        assign.setTestId(assignDTO.getTestId());
        assign.setAssignNumber(assignDTO.getAssignNumber());
        assign.setRead(assignDTO.isRead());
        Assign savedAssign = assignService.saveAssign(assign);

        // Notify the assigned user via SSE
        SseEmitter emitter = userEmitters.get(assignDTO.getUserId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("assignment")
                        .data(savedAssign)); // Send just the ID
            } catch (IOException e) {
                userEmitters.remove(assignDTO.getUserId());
            }
        }

        return ResponseEntity.ok(savedAssign);
    }

    @GetMapping
    public ResponseEntity<List<Assign>> getAllAssigns() {
        List<Assign> assign = assignService.getAllAssigns();
        return ResponseEntity.ok(assign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assign> getAssignById(@PathVariable Long id) {
        Assign assign = assignService.getAssignById(id);
        return ResponseEntity.ok(assign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Assign> updateAssign(@PathVariable Long id, @RequestBody AssignDTO assignDTO) {
        Assign assign = new Assign();
        assign.setTestId(assignDTO.getTestId());

        Assign updatedAssign = assignService.updateAssign(id, assign);
        return ResponseEntity.ok(updatedAssign);
    }

    @PutMapping("/update-date")
    public ResponseEntity<Assign> updateDate(@RequestBody Map<String, Object> requestBody) {
        try {
            Long id = Long.parseLong(requestBody.get("id").toString());
            String dateString = requestBody.get("dateExec").toString();

            // Parse the date string (format: "DD/MM/YYYY")
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            LocalDateTime dateTime = localDate.atStartOfDay();

            Assign assign = assignService.updateDate(id, dateTime);
            return ResponseEntity.ok(assign);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssign(@PathVariable Long id) {
        assignService.deleteAssign(id);
        return ResponseEntity.noContent().build();
    }

    // SSE endpoint for users to listen for assignments
    @GetMapping(path = "/stream/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamAssignments(@PathVariable Long userId) {
        SseEmitter emitter = new SseEmitter(60_000L); // Timeout: 60 seconds
        userEmitters.put(userId, emitter);

        // Cleanup on completion or error
        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onError((e) -> userEmitters.remove(userId));

        return emitter;
    }

    @PutMapping("/read/{assignId}")
    public ResponseEntity<Assign> markAsRead(@PathVariable Long assignId) {
        Assign assign = new Assign();
        assign.setRead(true);
        Assign updatedAssign = assignService.updateAssign(assignId, assign);
        return ResponseEntity.ok(updatedAssign);
    }

    @PutMapping("/update-user")
    public ResponseEntity<Assign> updateUser(@RequestBody Map<String, Object> requestBody) {
        try {
            Long id = Long.parseLong(requestBody.get("id").toString());
            Long userId = Long.parseLong(requestBody.get("userId").toString());

            Assign assign = assignService.updateUser(id, userId);
            return ResponseEntity.ok(assign);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
