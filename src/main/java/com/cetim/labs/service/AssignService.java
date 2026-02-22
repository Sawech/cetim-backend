package com.cetim.labs.service;

import org.springframework.stereotype.Service;
import com.cetim.labs.model.Assign;
import com.cetim.labs.repository.AssignRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AssignService {

    @Autowired
    private AssignRepository assignRepository;

    public Assign saveAssign(Assign assign) {
        return assignRepository.save(assign);
    }

    public Assign getAssignById(Long id) {
        return assignRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));
    }

    public Assign updateAssign(Long id, Assign incomingAssign) {
        Assign existingAssign = getAssignById(id);
        if (incomingAssign.isRead() != existingAssign.isRead()) {
            existingAssign.setRead(incomingAssign.isRead());
        }
        return assignRepository.save(existingAssign);
    }

    public List<Assign> getAllAssigns() {
        return assignRepository.findAll();
    }

    public void deleteAssign(Long id) {
        assignRepository.deleteById(id);
    }

    public Assign updateUser(Long id, Long userId) {
        // 1. Get the original element (but don't modify it)
        Assign existingAssign = assignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assign not found"));

        // 2. Create a NEW element with the same properties but updated content
        existingAssign.setUserId(userId);

        return assignRepository.save(existingAssign);
    }

    public Assign updateDate(Long id, LocalDateTime dateExec) {
        Assign existingAssign = assignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assign not found"));

        existingAssign.setDateExec(dateExec);
        return assignRepository.save(existingAssign);
    }
}
