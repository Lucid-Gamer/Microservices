package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.InventoryResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

	private final static Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

	private final InventoryRepository inventoryRepository;
	
	@Override
	@Transactional(readOnly = true)
	@SneakyThrows
	public List<InventoryResponse> isInStock(List<String> skvCode) {
		logger.info("Wait started::::::::::::::::::::::::::::::::::::::::::::::");
//		Thread.sleep(1000);
		logger.info("Wait ended::::::::::::::::::::::::::::::::::::::::::::::");
		return inventoryRepository.findBySkvCodeIn(skvCode).stream()
				.map(inventory ->
					InventoryResponse.builder()
							.skvCode(inventory.getSkvCode())
							.isInStock(inventory.getQuantity() > 0)
							.build()
				).toList();

	}

}
