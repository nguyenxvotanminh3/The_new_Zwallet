package com.nguyenminh.microservices.zwallet.controller;

import com.nguyenminh.microservices.zwallet.dto.PaymentRequest;
import com.nguyenminh.microservices.zwallet.model.PaymentSchedule;
import com.nguyenminh.microservices.zwallet.service.PaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v6/payment-schedules")
public class PaymentScheduleController {

    private final PaymentScheduleService paymentScheduleService;

    @Autowired
    public PaymentScheduleController(PaymentScheduleService paymentScheduleService) {
        this.paymentScheduleService = paymentScheduleService;
    }

    // Endpoint to create or update a PaymentSchedule
    @PostMapping
    public ResponseEntity<PaymentSchedule> createOrUpdatePaymentSchedule(@RequestBody PaymentSchedule paymentSchedule) {
        PaymentSchedule savedSchedule = paymentScheduleService.createOrUpdatePaymentSchedule(paymentSchedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    // Endpoint to get PaymentSchedule by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSchedule> getPaymentScheduleById(@PathVariable String id) {
        Optional<PaymentSchedule> paymentSchedule = paymentScheduleService.getPaymentScheduleById(id);
        return paymentSchedule.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<PaymentSchedule> getPaymentSchedulesByUserId(@PathVariable String userId) {
        return paymentScheduleService.getPaymentSchedulesByUserId(userId);
    }

//    // Endpoint to calculate next due date
//    @GetMapping("/{id}/next-due-date")
//    public ResponseEntity<Date> calculateNextDueDate(@PathVariable String id) {
//        Optional<PaymentSchedule> paymentScheduleOpt = paymentScheduleService.getPaymentScheduleById(id);
//        if (paymentScheduleOpt.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        PaymentSchedule paymentSchedule = paymentScheduleOpt.get();
//        Date nextDueDate = paymentScheduleService.calculateNextDueDate(paymentSchedule);
//        return ResponseEntity.ok(nextDueDate);
//    }

    // Endpoint to delete PaymentSchedule by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentSchedule(@PathVariable String id) {
        paymentScheduleService.deletePaymentSchedule(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to get all PaymentSchedules
    @GetMapping
    public ResponseEntity<List<PaymentSchedule>> getAllPaymentSchedules() {
        List<PaymentSchedule> schedules = paymentScheduleService.getAllPaymentSchedules();
        return ResponseEntity.ok(schedules);
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentSchedule> makePayment(@RequestBody PaymentRequest request) {
        PaymentSchedule updatedSchedule = paymentScheduleService.processPayment(
                request.getPaymentScheduleId(),
                request.getPaymentAmount(),
                request.getPaymentDate(),
                request.getPaymentAdvanceMonths()
        );
        return ResponseEntity.ok(updatedSchedule);
    }
}