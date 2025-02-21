package com.adarshgs.demo.services;

import com.adarshgs.demo.entities.Employee;
import com.adarshgs.demo.exceptions.ResourceNotFoundException;
import com.adarshgs.demo.repositories.EmployeeRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository empRepo;

    @Autowired
    public EmployeeService(EmployeeRepository empRepo) {
        this.empRepo = empRepo;
    }

    public List<Employee> getAllEmployees() {
        return empRepo.findAll();
    }

    @Transactional
    public void addEmployee(Employee employee) {
        empRepo.save(employee);
    }

    @Transactional
    public void updateEmployee(UUID id, Employee employee) {
        Employee emp = empRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Employee not found with the id: " + id
        ));

        emp.setEmail(employee.getEmail());
        emp.setName(employee.getName());
        emp.setSalary(employee.getSalary());

        empRepo.save(emp);
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        Employee emp = empRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Employee not found with the id: " + id
        ));

        empRepo.delete(emp);
    }
}
