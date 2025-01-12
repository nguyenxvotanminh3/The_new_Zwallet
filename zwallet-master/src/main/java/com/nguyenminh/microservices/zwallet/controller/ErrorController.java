package com.nguyenminh.microservices.zwallet.controller;

import com.nguyenminh.microservices.zwallet.model.Error;
import com.nguyenminh.microservices.zwallet.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/errors") // Base URL cho các endpoint của Error
public class ErrorController {

    @Autowired
    private ErrorService errorService; // Inject service bạn đã định nghĩa

    // Lấy danh sách tất cả các error
    @GetMapping
    public ResponseEntity<List<Error>> getAllErrors() {
        List<Error> errors = errorService.getAllList();
        return new ResponseEntity<>(errors, HttpStatus.OK); // Trả về danh sách lỗi với HTTP status 200 OK
    }

    // Tạo một error mới
    @PostMapping
    public ResponseEntity<Error> createError(@RequestBody Error error) {
        return errorService.createErrorSample(error); // Sử dụng service để tạo error và trả về kết quả
    }

    // Cập nhật trạng thái của error (mark as solved)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateError(@PathVariable String id) {
        return errorService.updateError(id); // Sử dụng service để cập nhật error và trả về kết quả
    }

    // Xóa error theo ID
    @DeleteMapping("/{id}")
    public String deleteError(@PathVariable String id) {
        return errorService.deleteErrorById(id); // Sử dụng service để xóa error và trả về kết quả
    }


}
