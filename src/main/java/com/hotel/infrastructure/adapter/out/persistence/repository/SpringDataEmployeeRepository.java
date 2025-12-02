package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.EmployeeJpaEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataEmployeeRepository extends JpaRepository<EmployeeJpaEntity, Long> {
	Optional<EmployeeJpaEntity> findByUserId(Long userId);
}