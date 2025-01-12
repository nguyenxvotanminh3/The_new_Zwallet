package com.nguyenminh.microservices.zwallet.controller;

import com.nguyenminh.microservices.zwallet.dto.DebtResponse;
import com.nguyenminh.microservices.zwallet.dto.UserResponse;
import com.nguyenminh.microservices.zwallet.model.Debt;
import com.nguyenminh.microservices.zwallet.model.PaginatedResponse;
import com.nguyenminh.microservices.zwallet.service.DebtService;
import com.nguyenminh.microservices.zwallet.service.UserDebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;


@RestController
@RequestMapping("/api/v4")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService transactionHistoryService;
    private final UserDebtService userDebtService;

    @GetMapping("/debt")
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<DebtResponse> getDebtPagination (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam String userName
    ) throws UnsupportedEncodingException {
        return transactionHistoryService.getDebtPagination(page,size,sort,userName);
    }

    @PostMapping("/debt/create/{nameCreator}/{nameDebtor}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse createDebt(@PathVariable String nameCreator,@PathVariable String nameDebtor , @RequestBody Debt debt){
        return transactionHistoryService.createDebt(nameCreator,nameDebtor,debt);
    }

    @PostMapping("/debt/accept")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createDebt(@RequestParam String t){
        return userDebtService.acceptTheDebt(t);
    }


    @PutMapping("/debt/adjust/{nameDebtor}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String adjustDebt(@PathVariable String id, @PathVariable String nameDebtor , @RequestBody Debt debt){
        return transactionHistoryService.adjustDebt(id,nameDebtor,debt);
    }

    @DeleteMapping("/debt/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteDebt(@PathVariable String id){return userDebtService.deleteDebt(id);
    }
}
