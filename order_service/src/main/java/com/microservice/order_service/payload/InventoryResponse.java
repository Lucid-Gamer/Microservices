package com.microservice.order_service.payload;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InventoryResponse {
    private String skvCode;
    private Boolean isInStock;
}
