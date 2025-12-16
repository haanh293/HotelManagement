package com.hotel.infrastructure.adapter.out.persistence;

import com.hotel.application.port.out.EmployeeRepositoryPort;
import com.hotel.domain.model.Employee;
import com.hotel.infrastructure.adapter.out.persistence.entity.EmployeeJpaEntity;
import com.hotel.infrastructure.adapter.out.persistence.repository.SpringDataEmployeeRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmployeePersistenceAdapter implements EmployeeRepositoryPort {

    private final SpringDataEmployeeRepository springDataEmployeeRepository;

    public EmployeePersistenceAdapter(SpringDataEmployeeRepository springDataEmployeeRepository) {
        this.springDataEmployeeRepository = springDataEmployeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        EmployeeJpaEntity entity = new EmployeeJpaEntity();
        entity.setId(employee.getId());
        entity.setFullName(employee.getFullName());
        entity.setPosition(employee.getPosition());
        entity.setPhoneNumber(employee.getPhoneNumber());
        entity.setSalary(employee.getSalary());
        
        entity.setUserId(employee.getUserId());

        EmployeeJpaEntity saved = springDataEmployeeRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public List<Employee> findAll() {
        return springDataEmployeeRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        springDataEmployeeRepository.deleteById(id);
    }
    @Override
    public Employee getEmployeeByUserId(Long userId) {
        return springDataEmployeeRepository.findByUserId(userId)
                .map(this::mapToDomain)
                .orElse(null);
    }
    @Override
    public Optional<Employee> findById(Long id) {
        return springDataEmployeeRepository.findById(id)
                .map(this::mapToDomain);
    }

    private Employee mapToDomain(EmployeeJpaEntity entity) {
        Employee emp = new Employee();
        emp.setId(entity.getId());
        emp.setFullName(entity.getFullName());
        emp.setPosition(entity.getPosition());
        emp.setPhoneNumber(entity.getPhoneNumber());
        emp.setSalary(entity.getSalary());
        emp.setUserId(entity.getUserId());
        return emp;
    }
}