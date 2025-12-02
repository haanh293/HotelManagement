package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.GuestJpaEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataGuestRepository extends JpaRepository<GuestJpaEntity, Long> {
	Optional<GuestJpaEntity> findByUserId(Long userId);
}