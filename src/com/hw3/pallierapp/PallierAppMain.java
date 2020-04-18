package com.hw3.pallierapp;

import java.io.IOException;

public class PallierAppMain {

	public static void main(String[] args) {
		PallierAppFunctions paf = new PallierAppFunctions();
		switch (args[0]){
		case "-keygen":
			if (args.length == 5){
				paf.keyGenTask(args[2], args[4]);
			}
			else{
				System.out.println("Error: Wrong number of CLI parameters");
			}
			break;
		case "-encrypt":
			if (args.length == 7){
				try{
					paf.encrypt(args[2], args[4], args[6]);
				}
				catch(IOException io){
					System.err.println("Error: " + io.getMessage());
				}
			}
			else{
				System.out.println("Error: Wrong number of CLI parameters");
			}
			break;
		case "-decrypt":
			if (args.length == 9){
				try{
					paf.decrypt(args[2], args[4], args[6], args[8]);
				}
				catch(IOException io){
					System.err.println("Error: " + io.getMessage());
				}
			}
			else{
				System.out.println("Error: Wrong number of CLI parameters");
			}
			break;
		case "-process":
			if (args.length == 7){
				try{
					paf.process(args[2], args[4], args[6]);
				}
				catch(IOException io){
					System.err.println("Error: " + io.getMessage());
				}
				
			}
			else{
				System.out.println("Error: Wrong number of CLI parameters");
			}
			break;
		default:
			System.out.println("Error: Wrong first option provided. Try: -keygen, -encrypt, -decrypt or -process");
		}
	}
	

}
