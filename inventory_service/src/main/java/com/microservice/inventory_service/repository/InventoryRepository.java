package com.microservice.inventory_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.inventory_service.model.Inventory;



public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	public boolean existsBySkvCode(String skvCode);
	
	public Optional<Inventory> findBySkvCode(String skvCode);
	
}
