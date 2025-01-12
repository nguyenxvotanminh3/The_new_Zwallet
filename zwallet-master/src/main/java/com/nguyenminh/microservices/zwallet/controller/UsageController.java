package com.nguyenminh.microservices.zwallet.controller;

import com.nguyenminh.microservices.zwallet.dto.PercentUsageRequest;
import com.nguyenminh.microservices.zwallet.service.UsageCaculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v3")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UsageController {
    private final UsageCaculatorService usageCaculatorService;
    @PostMapping("/calculate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPercent (@RequestBody HashMap<String, String> userName){
     return usageCaculatorService.usageCaculate(userName.get("username"));
    }
    @PostMapping("/calculate-total")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPercentTotal (@RequestBody HashMap<String, String> userName){
        return usageCaculatorService.usageCaculateTotal(userName.get("username"));
    }
    @PostMapping("/future")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getFutureFund(@RequestBody HashMap<String, String> income,
                                           @RequestParam(defaultValue = "false") boolean save){
        return usageCaculatorService.getFutureFund(income.get("userName"),income.get("income"),save);
    }
}
