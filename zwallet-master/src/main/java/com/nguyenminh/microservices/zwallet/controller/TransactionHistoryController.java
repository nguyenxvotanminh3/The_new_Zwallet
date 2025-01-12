package com.nguyenminh.microservices.zwallet.controller;
import com.nguyenminh.microservices.zwallet.dto.TransactionHistoryResponse;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.PaginatedResponse;
import com.nguyenminh.microservices.zwallet.model.TransactionHistory;
import com.nguyenminh.microservices.zwallet.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
@CrossOrigin("*")
@RequiredArgsConstructor
public class TransactionHistoryController {
    @Autowired
    private TransactionHistoryService transactionHistoryService;

//    @GetMapping("/all")
//    @ResponseStatus(HttpStatus.OK)
//    public List<TransactionHistoryResponse> getAllTransactionHistory (){
//        return transactionHistoryService.getAllTransactionHistory();
//    }
    @GetMapping("/trans")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<TransactionHistoryResponse> getTransactionHistoryPagination (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam String userName
    ) throws UnsupportedEncodingException {
        return transactionHistoryService.getTransactionHistoryPagination(page,size,sort,userName);
    }



    @PostMapping("/trans/create/{name}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse createTransactionHistory (@PathVariable String name , @RequestBody TransactionHistory transactionHistory){
        return transactionHistoryService.createTransactionHistory(name,transactionHistory);
    }
}
