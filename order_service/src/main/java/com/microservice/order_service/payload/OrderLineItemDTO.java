package com.microservice.order_service.payload;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemDTO {
	private Long id; 
	private String skvCode;
	private BigDecimal price;
	private Integer quantity;
}
