package com.nguyenminh.microservices.zwallet.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserResponsePlayShare {
    private List<PlayShareResponse> playShareResponses = new ArrayList<>();
}
