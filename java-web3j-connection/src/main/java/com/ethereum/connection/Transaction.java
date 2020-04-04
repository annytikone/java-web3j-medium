package com.eth.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Optional;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

public class Transaction {

	public static void main(String[] args) {

		System.out.println("Connecting to Ethereum ...");
		Web3j web3 = Web3j.build(new HttpService("HTTP://---:7545"));
		System.out.println("Successfuly connected to Ethereum");
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		try {
			String  privetKey= "b5a98d527f87dd59e5accc978d27ae3f0cd276be37a6710be500a64132f00fa5"; // Add a private key here
			// Decrypt private key into Credential object
			Credentials credentials = Credentials.create(privetKey);
			System.out.println("Account address: " + credentials.getAddress());
			System.out.println("Balance: "
					+ Convert.fromWei(web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
							.send().getBalance().toString(), Unit.ETHER));

			// Get the latest nonce of current account
			EthGetTransactionCount ethGetTransactionCount = web3
					.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
			BigInteger nonce = ethGetTransactionCount.getTransactionCount();

			// Recipient address
			String recipientAddress = "0x00029759591F96dB239b27B15EA7e37C58C42356";
			// Value to transfer (in wei)
			System.out.println("Enter Amount to be sent:");
			String amountToBeSent=br.readLine();
			BigInteger value = Convert.toWei(amountToBeSent, Unit.ETHER).toBigInteger();

			// Gas Parameter
			BigInteger gasLimit = BigInteger.valueOf(21000);
			BigInteger gasPrice = Convert.toWei("1", Unit.GWEI).toBigInteger();

			// Prepare the rawTransaction
			RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit,
					recipientAddress, value);

			// Sign the transaction
			byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
			String hexValue = Numeric.toHexString(signedMessage);

			// Send transaction
			EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
			String transactionHash = ethSendTransaction.getTransactionHash();
			System.out.println("transactionHash: " + transactionHash);

			// Wait for transaction to be mined
			Optional<TransactionReceipt> transactionReceipt = null;
			do {
				System.out.println("checking if transaction " + transactionHash + " is mined....");
				EthGetTransactionReceipt ethGetTransactionReceiptResp = web3.ethGetTransactionReceipt(transactionHash)
						.send();
				transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
				Thread.sleep(3000); // Wait for 3 sec
			} while (!transactionReceipt.isPresent());

			System.out.println("Transaction " + transactionHash + " was mined in block # "
					+ transactionReceipt.get().getBlockNumber());
			System.out.println("Balance: "
					+ Convert.fromWei(web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
							.send().getBalance().toString(), Unit.ETHER));

		} catch (IOException | InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}
}
