package com.hotel.application.port.out;

import com.hotel.domain.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepositoryPort {
    Employee save(Employee employee);
    List<Employee> findAll();
    void deleteById(Long id);
    Employee getEmployeeByUserId(Long userId);
    Optional<Employee> findById(Long id);
}