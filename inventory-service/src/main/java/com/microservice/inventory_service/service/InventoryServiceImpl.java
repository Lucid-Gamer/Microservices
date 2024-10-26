package com.microservice.inventory_service.service;

import com.microservice.inventory_service.dto.InventoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

	private final InventoryRepository inventoryRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skvCode) {
		return inventoryRepository.findBySkvCodeIn(skvCode).stream()
				.map(inventory ->
					InventoryResponse.builder()
							.skvCode(inventory.getSkvCode())
							.isInStock(inventory.getQuantity() > 0)
							.build()
				).toList();

	}

}
