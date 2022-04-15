package com.algorithmicintuition.tech.vendingmachine.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.algorithmicintuition.tech.vendingmachine.types.Product;

@Service
public class ProductService {

	private List<Product> products = List.of(new Product(1, "Coke", .25, 10), 
											 new Product(2, "Pepsi", .35, 10), 
											 new Product(3, "Soda", .45, 10));
	public List<Product> getProducts() {
		return this.products;
	}
	
	public Product findProductById(int productId) {
		return this.products.stream()
        .filter(product -> product.getId() == productId)
        .findFirst().get();
		
	}
	public void updateProductInventory(int productId, int inventory) {
		
		try {
			Product found = this.findProductById(productId);
			found.setInventoryCount(inventory);
			
		} catch( Throwable e) {
			e.printStackTrace();
			System.err.println("Error occured saving updateProductInventory: " + e.getMessage() );
		}
		
	}

}
