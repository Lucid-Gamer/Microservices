package com.microservice.order_service.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.model.OrderLineItem;
import com.microservice.order_service.payload.OrderLineItemDTO;
import com.microservice.order_service.payload.OrderRequest;
import com.microservice.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

	private final OrderRepository orderRepository;
	
	@Override
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderLineItemList(
					orderRequest.getOrderLineItems()
					.stream()
					.map(orderLineItem -> mapFromDTO(orderLineItem))
					.collect(Collectors.toList())
				);
		orderRepository.save(order);
	}
	
	public OrderLineItem mapFromDTO(OrderLineItemDTO orderLineItemDTO) {
		return OrderLineItem.builder()
//				.id(orderLineItemDTO.getId())
				.price(orderLineItemDTO.getPrice())
				.skvCode(orderLineItemDTO.getSkvCode())
				.quantity(orderLineItemDTO.getQuantity())
				.build();
	}

}
