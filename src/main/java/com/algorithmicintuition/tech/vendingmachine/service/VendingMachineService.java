package com.algorithmicintuition.tech.vendingmachine.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algorithmicintuition.tech.vendingmachine.types.Coin;
import com.algorithmicintuition.tech.vendingmachine.types.Product;

@Service
public class VendingMachineService {
	
	@Autowired
	ProductService productService;
	
	private double totalPennyCount = 0.00;
	
	public double getTotalPennyCount() {
		return this.totalPennyCount;
	}
	
	public void addCoin(Coin coin) {
		this.totalPennyCount += coin.getValue();
	}
	
	public void refund() {
		this.totalPennyCount = 0;
	}
	
	public void buyProduct(Product product) throws FileNotFoundException {
		if( this.totalPennyCount < product.getCostInPennys() || product.getInventoryCount() < 1 ) {
			throw new FileNotFoundException("Error this inventory is now empty");
		}
		
		this.totalPennyCount -= product.getCostInPennys();
		productService.updateProductInventory(product.getId(), product.getInventoryCount() - 1);
	}
	
	public List<Product> listAllProducts() {
		return this.productService.getProducts();
	}
	
	public List<Product> findAvailableProducts() {
		List<Product> allProducts = this.productService.getProducts();
		Stream<Product> productStream = allProducts.stream();
		List<Product> found = productStream.filter((product)-> this.totalPennyCount >= product.getCostInPennys() && product.getInventoryCount() > 0).toList();
		return found;
	}

	public Product findProductById(Integer productId) {
		return this.productService.findProductById(productId);
	}

}
