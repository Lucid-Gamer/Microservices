package com.microservice.inventory_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.inventory_service.model.Inventory;



public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	public boolean existsBySkvCode(String skvCode);
	
	public List<Inventory> findBySkvCodeIn(List<String> skvCode);
	
}
