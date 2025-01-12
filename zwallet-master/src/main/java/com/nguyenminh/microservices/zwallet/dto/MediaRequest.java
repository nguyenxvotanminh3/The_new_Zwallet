package com.nguyenminh.microservices.zwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaRequest {
    private String name;
    private String content;
    private String type;
    private String updatedAt;
}

