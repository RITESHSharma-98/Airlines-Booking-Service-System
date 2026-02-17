package com.airlines.controller;

import com.airlines.model.Payment;
import com.airlines.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping
    public ResponseEntity<Payment> processPayment(
            @RequestParam Long bookingId,
            @RequestParam BigDecimal amount,
            @RequestParam Payment.PaymentMethod method) {
        Payment payment = paymentService.processPayment(bookingId, amount, method);
        return payment != null ? ResponseEntity.status(HttpStatus.CREATED).body(payment) : ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable String transactionId) {
        Optional<Payment> payment = paymentService.getPaymentByTransactionId(transactionId);
        return payment.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<Payment>> getPaymentsByBooking(@PathVariable Long bookingId) {
        List<Payment> payments = paymentService.getPaymentsByBooking(bookingId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable Payment.PaymentStatus status) {
        List<Payment> payments = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(payments);
    }
    
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Payment> confirmPayment(@PathVariable Long id) {
        Payment payment = paymentService.confirmPayment(id);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        Payment payment = paymentService.refundPayment(id);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/fail")
    public ResponseEntity<String> failPayment(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        boolean success = paymentService.failPayment(id, reason);
        return success ? ResponseEntity.ok("Payment failed successfully") : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}