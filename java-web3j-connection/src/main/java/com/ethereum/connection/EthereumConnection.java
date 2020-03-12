package com.ethereum.connection;

import java.io.IOException;
import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

public class EthereumConnection {

	public static void main(String[] args) throws Exception {

		System.out.println("Connecting to Ethereum ...");
		Web3j web3 = Web3j.build(new HttpService("HTTP://Your-Ip-Address:8286"));//RPC SERVER
		System.out.println("Successfuly connected to Ethereum");

		try {
			Web3ClientVersion clientVersion = web3.web3ClientVersion().send();
			System.out.println("Client version: " + clientVersion.getWeb3ClientVersion());

			EthGasPrice gasPrice = web3.ethGasPrice().send();
			System.out.println("Default Gas Price: "+gasPrice.getGasPrice());

			EthGetBalance ethGetBalance = web3
					.ethGetBalance("0xcF8B652b0173FBABE734f5F388C2da24a2359993", DefaultBlockParameterName.LATEST)
					.sendAsync().get();

			System.out.println("Balance: of Account '0xcF8B652b0173FBABE734f5F388C2da24a2359993' "
			+ ethGetBalance.getBalance());
			
			System.out.println("Balance in Ether format: "
			+Convert.fromWei(web3.ethGetBalance("0xcF8B652b0173FBABE734f5F388C2da24a2359993",
			DefaultBlockParameterName.LATEST).send().getBalance().toString(),Unit.ETHER));

		} catch (IOException ex) {
			throw new RuntimeException("Error whilst sending json-rpc requests", ex);
		}
	}
}
