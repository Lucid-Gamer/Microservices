package com.microservice.inventory_service.dto;

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
