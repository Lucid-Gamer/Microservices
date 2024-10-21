package com.microservice.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.microservice.order_service.payload.InventoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.order_service.model.Order;
import com.microservice.order_service.model.OrderLineItem;
import com.microservice.order_service.payload.OrderLineItemDTO;
import com.microservice.order_service.payload.OrderRequest;
import com.microservice.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService{

	private final OrderRepository orderRepository;

	private final WebClient webClient;
	
	@Override
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderLineItemList(
					orderRequest.getOrderLineItems()
					.stream()
					.map(this::mapFromDTO)
					.collect(Collectors.toList())
				);
		//Call Inventory service and place order if product is in stock

		List<String> skvCodes =  order.getOrderLineItemList().stream()
				.map(OrderLineItem::getSkvCode)
				.toList();

		InventoryResponse[] inventoryResponseArray = webClient.get()
				.uri("http://localhost:8082/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skvCode",skvCodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();

		boolean allPrduuctsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);

		if (allPrduuctsInStock) {
			orderRepository.save(order);
		} else {
			throw new IllegalArgumentException("Product is not in stock. Please try again later");
		}
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
