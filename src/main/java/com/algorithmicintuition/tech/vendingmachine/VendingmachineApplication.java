package com.algorithmicintuition.tech.vendingmachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.algorithmicintuition.tech.vendingmachine.service.VendingMachineService;
import com.algorithmicintuition.tech.vendingmachine.types.Coin;
import com.algorithmicintuition.tech.vendingmachine.types.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

@SpringBootApplication
public class VendingmachineApplication implements CommandLineRunner {

	final static Scanner commandLine = new Scanner(System.in);
	private final DecimalFormat df = new DecimalFormat("0.00");

	private Consumer<String> exitEvent = (message) -> {

		System.out.println("\n\n==================================\n\n");
		System.out.println(message);
		System.out.println("\n\n==================================");
		System.exit(0);
	};

	@Autowired
	VendingMachineService vendingMachineService;

	private void outputHelpNoParams(String... args) {
		System.out.println("\n\n\n*************************************** Info **********************\n\n\n");
		System.out.println("Runner must have have command line argument 'run' or this method no-ops");
		System.out.println("\n\n   For example:   java -jar target/vendingmachine-0.0.1-SNAPSHOT.jar \"run\" \n");
		System.out.println(
				"\n\n\n*************************************** Goodbye No simulation was run **********************\n\n\n");
		System.out.println(args.toString());
	}

	private void printInventoryList() {

		this.vendingMachineService.listAllProducts().forEach((product) -> {
			StringBuffer buffer = new StringBuffer();

			buffer.append("    ");
			buffer.append(product.getId());
			buffer.append(") ");
			buffer.append(product.getName());
			buffer.append(" $");
			buffer.append(this.df.format(product.getCostInPennys()));
			buffer.append(" currently have: ");
			buffer.append(product.getInventoryCount());
			buffer.append(" in stock");

			System.out.println(buffer.toString());
		});

	}

	private void refundRemaningToUser() {
		double totalBeingRefunded = this.vendingMachineService.getTotalPennyCount();

		// Perform the refund.
		this.vendingMachineService.refund();

		String refundMessage = "All your funds are being refunded.  Total money returned: $"
				+ df.format(totalBeingRefunded) + " Money left in machine: $"
				+ this.df.format(this.vendingMachineService.getTotalPennyCount());

		this.exitEvent.accept(refundMessage);
	}

	
	private void buyInventoryList() {

		boolean run = true;
		
		while (run) {
			List<String> foundIds = new ArrayList<String>();

			this.vendingMachineService.findAvailableProducts().forEach((product) -> {
				StringBuffer buffer = new StringBuffer();

				buffer.append("    ");
				buffer.append(product.getId());
				buffer.append(") ");
				buffer.append(product.getName());
				buffer.append(" $");
				buffer.append(this.df.format(product.getCostInPennys()));
				buffer.append(" currently have: ");
				buffer.append(product.getInventoryCount());
				buffer.append(" in stock");

				System.out.println(buffer.toString());
				foundIds.add("" + product.getId());
			});

			System.out.println();

			System.out.print("Please enter r for refund, m to enter more money, or enter one of these product numbers (");

			foundIds.forEach((idValue) -> {
				System.out.print(" " + idValue);
			});

			System.out.print(" )\n\n:>");

			String productCommand = commandLine.next();
			
			System.out.println("======" + productCommand);
			
			if( productCommand.equals("r")) {
				this.refundRemaningToUser();
				run = false;
			} if( productCommand.equals("m")) {
				this.takeTheMoney();
			} else if( foundIds.contains(productCommand )) {
				System.out.println("User wants to buy product: " + productCommand);
				Product product = this.vendingMachineService.findProductById(Integer.valueOf(productCommand));
				try {
					this.vendingMachineService.buyProduct(product);
					System.out.println("Just dispensed your drink ymmy enjoy your can of: " + product.getName() + " cost was $" + this.df.format(product.getCostInPennys()));
					System.out.println("Your remaining money is: $" + this.df.format(this.vendingMachineService.getTotalPennyCount()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Error trying to buy product:" + e.getMessage());
				}
			} else {
				System.out.println("Sorry didn't recognize that please select only a valid procuct");
			}

		}

	}

	private void takeTheMoney() {

		boolean run = true;
		boolean refunded = false;
		boolean doneInserting = false;

		while (run) {
			System.out.println(
					"Please insert coins: \na: penny, \nb: nickle, \nc: dime, \nd: quarter, \ne: finished paying, \nf: refund money");
			System.out.print("\n\n:>");
			String userInput = commandLine.next();

			String coinType = "";

			switch (userInput) {
			case "a":
				this.vendingMachineService.addCoin(Coin.PENNY);
				coinType = Coin.PENNY.name();
				break;
			case "b":
				this.vendingMachineService.addCoin(Coin.NICKLE);
				coinType = Coin.NICKLE.name();
				break;
			case "c":
				this.vendingMachineService.addCoin(Coin.DIME);
				coinType = Coin.DIME.name();
				break;
			case "d":
				this.vendingMachineService.addCoin(Coin.QUARTER);
				coinType = Coin.QUARTER.name();
				break;
			case "e":
				run = false;
				doneInserting = false;
				break;
			case "f":
				run = false;
				refunded = true;
				break;
			default:
			}

			if (refunded == true) {
				this.refundRemaningToUser();

			} else if (doneInserting == false) {

				System.out.println("Lets see what you can buy with: $"
						+ this.df.format(this.vendingMachineService.getTotalPennyCount()));

			} else if (coinType.length() > 0 && run == true) {
				double total = this.vendingMachineService.getTotalPennyCount();
				System.out.println(
						"User enetred One " + coinType + " Total money for drinks is: $" + this.df.format(total));
			} else {
				System.out
						.println("*******Did not understand that.  I only understand a, b, c, d, e or f. as answers\n");
			}

		}

	}

	
	@Override
	public void run(String... args) throws Exception {

		if (args.length < 1 || !"run".equals(args[0])) {
			this.outputHelpNoParams(args);
			return;
		}

		System.out.println("\n\n\n************** START VENDING MACHINE SIMULATION **********************\n\n\n");

		// Prints out every product we have. How much it costs how many cans are in the
		// machine
		// inventory count
		System.out.println("List of products this machine can sell:\n");
		this.printInventoryList();

		// Take money from customer.
		System.out.println();
		this.takeTheMoney();

		// Show customer what they can by.
		System.out.println("\nHere's the what you can buy of the available inventory:\n");
		this.buyInventoryList();

		// All Done. Good bye
		System.out.println("\n\n\n************** END VENDING MACHINE SIMULATION **********************\n\n\n");
		commandLine.close();
	}

	/**
	 * Application Entry point pass "run" to make the vending machine simulation run
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(VendingmachineApplication.class, args);
	}

}
