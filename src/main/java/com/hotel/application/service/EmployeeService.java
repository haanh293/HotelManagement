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
    }@Override
    public Employee updateEmployee(Long id, Employee request) {
        // 1. Tìm nhân viên cũ
        Employee existingEmployee = employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên để cập nhật!"));

        // 2. Cập nhật các trường thông tin (Chỉ update những gì cần thiết)
        if (request.getFullName() != null) existingEmployee.setFullName(request.getFullName());
        if (request.getPosition() != null) existingEmployee.setPosition(request.getPosition());
        if (request.getPhoneNumber() != null) existingEmployee.setPhoneNumber(request.getPhoneNumber());
        if (request.getSalary() != null) existingEmployee.setSalary(request.getSalary());

        // Lưu ý: Không update userId để tránh mất liên kết tài khoản

        // 3. Lưu lại (Hàm save trong JPA nếu có ID sẽ tự hiểu là Update)
        return employeeRepositoryPort.save(existingEmployee);
    }
    
}