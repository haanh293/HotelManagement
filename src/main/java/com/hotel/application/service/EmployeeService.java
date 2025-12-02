package com.hotel.application.service;

import com.hotel.application.port.in.EmployeeUseCase;
import com.hotel.application.port.out.EmployeeRepositoryPort;
import com.hotel.domain.model.Employee;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService implements EmployeeUseCase {

    private final EmployeeRepositoryPort employeeRepositoryPort;

    public EmployeeService(EmployeeRepositoryPort employeeRepositoryPort) {
        this.employeeRepositoryPort = employeeRepositoryPort;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepositoryPort.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepositoryPort.findAll();
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepositoryPort.deleteById(id);
    }
    @Override
    public Employee getEmployeeByUserId(Long userId) {
        return employeeRepositoryPort.getEmployeeByUserId(userId);
    }
}