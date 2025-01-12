package com.nguyenminh.microservices.zwallet.service;

import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.PaymentSchedule;
import com.nguyenminh.microservices.zwallet.model.UserModel;
import com.nguyenminh.microservices.zwallet.repository.PaymentScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;
    private final UserModelService userModelService;

    // Get all PaymentSchedules for a specific user
    public List<PaymentSchedule> getPaymentSchedulesByUserId(String userId) {
        return paymentScheduleRepository.findByUserId(userId);
    }

    public PaymentSchedule createOrUpdatePaymentSchedule(PaymentSchedule paymentSchedule) {
        // Tính số tiền trả trước dựa trên amountPerCycle và paymentAdvanceMonths
        double totalAdvancePayment = paymentSchedule.getAmountPerCycle() * paymentSchedule.getPaymentAdvanceMonths();

        // Đặt giá trị chính xác cho totalPaid
        paymentSchedule.setTotalPaid(totalAdvancePayment);

        // Tính toán ngày đến hạn tiếp theo (nextDueDate)
        calculateNextDueDate(paymentSchedule);

        // Cập nhật lịch sử thanh toán
        updatePaymentHistory(paymentSchedule, paymentSchedule.getStartDate(), totalAdvancePayment);

        // Lưu PaymentSchedule vào cơ sở dữ liệu
        return paymentScheduleRepository.save(paymentSchedule);
    }


    // Phương thức thêm khoản thanh toán mới vào PaymentSchedule
    public PaymentSchedule addPayment(String paymentScheduleId, double paymentAmount, Date paymentDate) {
        PaymentSchedule paymentSchedule = paymentScheduleRepository.findById(paymentScheduleId)
                .orElseThrow(() -> new RuntimeException("PaymentSchedule not found"));

        // Cập nhật paymentHistory và tổng số tiền đã trả
        updatePaymentHistory(paymentSchedule, paymentDate, paymentAmount);

        // Tính toán lại ngày đến hạn nếu không còn trả trước nữa
        if (paymentSchedule.getPaymentAdvanceMonths() == 0) {
            calculateNextDueDate(paymentSchedule);
        }

        // Lưu lại PaymentSchedule sau khi thay đổi
        return paymentScheduleRepository.save(paymentSchedule);
    }

    // Cập nhật paymentHistory và tổng số tiền đã trả
    private void updatePaymentHistory(PaymentSchedule paymentSchedule, Date paymentDate, double paymentAmount) {
        if (paymentSchedule.getPaymentHistory() == null) {
            paymentSchedule.setPaymentHistory(new ArrayList<>());
        }

        // Tạo bản ghi thanh toán
        Map<String, String> paymentRecord = new HashMap<>();
        paymentRecord.put(new SimpleDateFormat("yyyy-MM-dd").format(paymentDate), String.valueOf(paymentAmount));

        // Thêm bản ghi thanh toán vào paymentHistory
        paymentSchedule.getPaymentHistory().add(paymentRecord);
    }

    // Tính toán ngày đến hạn tiếp theo (nextDueDate)
    private void calculateNextDueDate(PaymentSchedule paymentSchedule) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(paymentSchedule.getStartDate());

        // Nếu có trả trước, cộng số tháng trả trước vào ngày bắt đầu thanh toán
        if (paymentSchedule.getPaymentAdvanceMonths() > 0) {
            calendar.add(Calendar.MONTH, paymentSchedule.getPaymentAdvanceMonths());
        }

        // Cập nhật nextDueDate
        paymentSchedule.setNextDueDate(calendar.getTime());
    }

    // Lấy tất cả PaymentSchedules
    public List<PaymentSchedule> getAllPaymentSchedules() {
        return paymentScheduleRepository.findAll();
    }

    // Lấy PaymentSchedule theo ID
    public Optional<PaymentSchedule> getPaymentScheduleById(String id) {
        return paymentScheduleRepository.findById(id);
    }

    // Xóa PaymentSchedule theo ID
    public void deletePaymentSchedule(String id) {
        paymentScheduleRepository.deleteById(id);
    }
    private Date calculateNextDueDate(Date startDate, int paymentAdvanceMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // Add the paymentAdvanceMonths to the start date
        calendar.add(Calendar.MONTH, paymentAdvanceMonths);

        // Return the calculated next due date
        return calendar.getTime();
    }
    public PaymentSchedule processPayment(String paymentScheduleId, double paymentAmount, Date paymentDate, int paymentAdvanceMonths) {
        // Tìm PaymentSchedule theo ID
        PaymentSchedule paymentSchedule = paymentScheduleRepository.findById(paymentScheduleId)
                .orElseThrow(() -> new RuntimeException("PaymentSchedule không tồn tại"));

        // Cập nhật totalPaid
        double updatedTotalPaid = paymentSchedule.getTotalPaid() + paymentAmount;
        paymentSchedule.setTotalPaid(updatedTotalPaid);

        // Cập nhật ngày đến hạn tiếp theo và số tháng trả trước
        if (paymentAdvanceMonths > 0) {
            paymentSchedule.setStartDate(paymentDate);
            paymentSchedule.setNextDueDate(calculateNextDueDate(paymentDate, paymentAdvanceMonths));
            paymentSchedule.setPaymentAdvanceMonths(paymentAdvanceMonths);
        } else {
            if (paymentAmount >= paymentSchedule.getAmountPerCycle()) {
                calculateNextDueDate(paymentSchedule);
            }
        }

        // Cập nhật lịch sử thanh toán
        updatePaymentHistory(paymentSchedule, paymentDate, paymentAmount);

        // Lưu lại PaymentSchedule
        return paymentScheduleRepository.save(paymentSchedule);
    }
}
