package com.microservice.order_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.microservice.order_service.payload.InventoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	private final OrderRepository orderRepository;

	private final WebClient.Builder webClientBuilder;
	
	@Override
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		try {
			logger.info("Inside place order in OrderServiceImpl:::::::::::::::::::::::::::::::::::::::::::::::");
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

			InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
					.uri("http://inventory-service/api/inventory",
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
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
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
