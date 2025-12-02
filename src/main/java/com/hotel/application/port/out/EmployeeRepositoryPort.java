package com.hotel.application.port.out;

import com.hotel.domain.model.Employee;
import java.util.List;

public interface EmployeeRepositoryPort {
    Employee save(Employee employee);
    List<Employee> findAll();
    void deleteById(Long id);
    Employee getEmployeeByUserId(Long userId);
}