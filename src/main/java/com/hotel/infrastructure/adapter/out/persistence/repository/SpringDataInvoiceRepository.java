package com.hotel.infrastructure.adapter.out.persistence.repository;

import com.hotel.infrastructure.adapter.out.persistence.entity.InvoiceJpaEntity;
import com.hotel.domain.model.MonthlyRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataInvoiceRepository extends JpaRepository<InvoiceJpaEntity, Long> {
	// Trích xuất Tháng/Năm từ ngày thanh toán và tính tổng tiền
    @Query("SELECT new com.hotel.domain.model.MonthlyRevenue(MONTH(i.paymentDate), YEAR(i.paymentDate), SUM(i.totalAmount)) " +
           "FROM InvoiceJpaEntity i " +
           "WHERE i.paymentDate IS NOT NULL " +
           "GROUP BY YEAR(i.paymentDate), MONTH(i.paymentDate) " +
           "ORDER BY YEAR(i.paymentDate), MONTH(i.paymentDate)")
    List<MonthlyRevenue> getMonthlyRevenue();
    Optional<InvoiceJpaEntity> findByBookingId(Long bookingId);
}