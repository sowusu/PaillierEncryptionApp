package com.hw3.pallierapp;

import paillierp.Paillier;
import paillierp.key.KeyGen;
import paillierp.key.PaillierKey;
import paillierp.key.PaillierPrivateKey;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class PallierAppFunctions {
	
	public void keyGenTask(String pubkeyFileName, String privKeyFileName){
		long seed = System.currentTimeMillis();
		int numberOfBitsInKey = 2048;

		/* Generate public and private keys */
		PaillierPrivateKey privKeyObject = KeyGen.PaillierKey(numberOfBitsInKey, seed);
		BigInteger pubKey = privKeyObject.getN();
		BigInteger privKey = privKeyObject.getD();
		
		/*Write keys to files*/
		try{
			writeKeyToFile(pubKey, pubkeyFileName);
			writeKeyToFile(privKey, privKeyFileName);
		}
		catch (IOException io)
		{		
			System.err.println("Error: " + io.getMessage());
		}
	}
	
	public void process(String pubkeyFileName, String encryptedFileName, String outputFileName) throws IOException{
		long seed = System.currentTimeMillis();
		/*Read in public key */
		ArrayList<String> fileLines = null;
		String pubkey = null;
		try{
			pubkey = readKeyFromFile(pubkeyFileName);
			fileLines = readFileLines(encryptedFileName);
		}
		catch (FileNotFoundException fe){
			System.err.println("Error: " + fe.getMessage());
		}
		
		if (fileLines == null || pubkey == null){
			System.out.println("Error: No Encrypted File or Public Key File");
		}
		else{
			BigInteger publicKey = new BigInteger(pubkey);
			PaillierKey pkeyObj = new PaillierKey(publicKey, seed);
			Paillier adder = new Paillier(pkeyObj);
			long ct = fileLines.size();
			BigInteger plain_count = BigInteger.valueOf(ct);
			BigInteger enc_count = adder.encrypt(plain_count);
			BigInteger enc_sum = adder.encryptzero();
			BigInteger enc_sq_sum = adder.encryptzero();
			for (String line: fileLines){
				String[] pair = line.split(",");
				enc_sum = adder.add(enc_sum, new BigInteger(pair[0]));
				enc_sq_sum = adder.add(enc_sq_sum, new BigInteger(pair[1]));
			}
			
			FileWriter fileWriter = new FileWriter(outputFileName);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.println(enc_sum.toString());
		    printWriter.println(enc_sq_sum.toString());
		    printWriter.println(enc_count.toString());
		    printWriter.close();
		}
		
		
	}
	
	
	public void encrypt(String pubkeyFileName, String inputFileName, String outputFileName) throws IOException{
		long seed = System.currentTimeMillis();
		/*Read in public key */
		String pubkey = null;
		try{
			pubkey = readKeyFromFile(pubkeyFileName);
		}
		catch (FileNotFoundException fe){
			System.err.println("Error: " + fe.getMessage());
		}
		
		if (pubkey == null){
			System.out.println("Error: No Public key");
		}
		else{
			BigInteger publicKey = new BigInteger(pubkey);
			
			Paillier encryptor = new Paillier(new PaillierKey(publicKey, seed));
			
			FileWriter fileWriter = new FileWriter(outputFileName);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
			
			File infileobj = new File(inputFileName);
			Scanner reader = new Scanner(infileobj);
			while(reader.hasNextLine()){
				String plainLineNumber = reader.nextLine();
				BigInteger m = new BigInteger(plainLineNumber);
				BigInteger e = encryptor.encrypt(m);
				BigInteger epow2 = encryptor.encrypt(m.multiply(m));
				printWriter.println(e.toString() + "," + epow2.toString());
			}
			
			
			printWriter.close();
			reader.close();
			
		}
		
	}
	
	public void decrypt(String privkeyFileName, String encryptedFileName, String decryptedFileName, String pubkeyFileName) throws IOException{
		long seed = System.currentTimeMillis();
		/*Read in public key */
		String privkey = null;
		String pubkey = null;
		try{
			privkey = readKeyFromFile(privkeyFileName);
			pubkey = readKeyFromFile(pubkeyFileName);
		}
		catch (FileNotFoundException fe){
			System.err.println("Error: " + fe.getMessage());
		}
		
		if (privkey == null || pubkey == null){
			System.out.println("Error: At least one of either public or private key is missing");
		}
		else{
			BigInteger privateKey = new BigInteger(privkey);
			BigInteger publicKey = new BigInteger(pubkey);
			
			Paillier decryptor = new Paillier(new PaillierPrivateKey(publicKey, privateKey, seed));
			
			FileWriter fileWriter = new FileWriter(decryptedFileName);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
			
			File infileobj = new File(encryptedFileName);
			Scanner reader = new Scanner(infileobj);
			while(reader.hasNextLine()){
				String encLine = reader.nextLine();
				String[] pair = encLine.split(",");
				BigInteger m = decryptor.decrypt(new BigInteger(pair[0]));
				BigInteger mpow2 = decryptor.decrypt(new BigInteger(pair[1]));

				printWriter.println(m.toString() + "," + mpow2.toString());
			}
			
			
			printWriter.close();
			reader.close();
			
		}
		
	}
	
	private ArrayList<String> readFileLines(String fileName) throws FileNotFoundException{
		File fobj = new File(fileName);
		Scanner reader = new Scanner(fobj);
		ArrayList<String> fileLines = new ArrayList<String>();
		while(reader.hasNextLine()){
			fileLines.add(reader.nextLine());
		}
		reader.close();
		return fileLines;
	}
	
	private void writeKeyToFile(BigInteger key, String fileName) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		writer.write(key.toString());
		
		writer.close();
	}
	
	private String readKeyFromFile(String keyFilename) throws FileNotFoundException {
		File fobj = new File(keyFilename);
		Scanner reader = new Scanner(fobj);
		String key = "";
		while(reader.hasNextLine()){
			key += reader.nextLine();
		}
		reader.close();
		return key;
	}

}
