package com.springbot.tttn.infrastructure.controller;

import com.springbot.tttn.domain.dtos.classes.ClassDto;
import com.springbot.tttn.domain.dtos.classes.DeleteClassesDto;
import com.springbot.tttn.domain.payloads.ResponseObject;
import com.springbot.tttn.domain.payloads.Result;
import com.springbot.tttn.domain.services.ClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/classes")
public class ClassController {
    @Autowired
    private ClassService classService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<Result> findAllClasses(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "className") String sortBy,
            @RequestParam(name = "className", defaultValue = "") String className,
            @RequestParam(name = "asc", defaultValue = "true") boolean asc
    ) {
        ResponseObject result = classService.findAll(pageNo, pageSize, sortBy, className, asc);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{className}")
    public ResponseEntity<Result> findClassById(@PathVariable String className) {
        ResponseObject result = classService.findClassByName(className);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Result> addClass(@Valid @RequestBody ClassDto body) {
        ResponseObject result = classService.save(body.toClass());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Result> updateClass(@Valid @RequestBody ClassDto newClass, @PathVariable Long id) {
        ResponseObject result = classService.update(newClass.toClass(), id);
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    @GetMapping("/select")
    public ResponseEntity<Result> findAllForSelect() {
        ResponseObject result = classService.findAllForSelect();
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Result> deleteClasses(@Valid @RequestBody DeleteClassesDto deleteClassesDto) {
        ResponseObject result = classService.deleteClasses(deleteClassesDto.getClassIds());
        return ResponseEntity.status(result.getStatusCode()).body(result.getResult());
    }
}
