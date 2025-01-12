package com.nguyenminh.microservices.zwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    private String userName;
    private String oldPass;
    private String newPass;
}