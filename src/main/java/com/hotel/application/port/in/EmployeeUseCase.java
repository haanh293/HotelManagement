package com.hotel.application.port.in;

import com.hotel.domain.model.Employee;
import java.util.List;

public interface EmployeeUseCase {
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();
    void deleteEmployee(Long id);
    Employee getEmployeeByUserId(Long userId);
}