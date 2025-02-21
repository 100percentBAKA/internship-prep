package com.adarshgs.demo.controllers;

import com.adarshgs.demo.dtos.NormalResponse;
import com.adarshgs.demo.entities.Employee;
import com.adarshgs.demo.services.EmployeeService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
    private final EmployeeService empService;

    @Autowired
    public EmployeeController(EmployeeService empService) {
        this.empService = empService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(empService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<?> putEmployee(@Valid @RequestBody Employee emp) {
        // we need not use binding results
        // we are handling it globally

        empService.addEmployee(emp);

        return ResponseEntity.ok().body(new NormalResponse("Employee added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable UUID id, @Valid @RequestBody Employee employee) {
        empService.updateEmployee(id, employee);

        return ResponseEntity.ok().body(new NormalResponse("Employee updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable UUID id) {
        empService.deleteEmployee(id);

        return ResponseEntity.ok().body(new NormalResponse("Employee with id: " + id + " deleted successfully"));
    }

}
