package com.airlines.repository;

import com.airlines.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    List<Payment> findByBookingId(Long bookingId);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.booking.id = :bookingId ORDER BY p.createdAt DESC")
    List<Payment> findPaymentsByBookingId(@Param("bookingId") Long bookingId);
}