package com.nguyenminh.microservices.zwallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagAndQuotesRequest {
    private String quotes;
    private String tag;
}
