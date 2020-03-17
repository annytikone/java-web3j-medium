package com.ethereum.account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

public class RestoreAccountBySeedsAndPassword {
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		/************* ------------ Restore Account ************------------ */
		System.out.println("Restoring Account by password and Seedphrase");
		System.out.println("Enter Wallet Password");
		String password = br.readLine();
		System.out.println("Enter Wallet SeedPhrase");
		String seedPhrase2 = br.readLine();

		Credentials restoreCredentials = WalletUtils.loadBip39Credentials(password, seedPhrase2);
		ECKeyPair restoredPrivateKey = restoreCredentials.getEcKeyPair();

		String restoredAccountAddress = restoreCredentials.getAddress();
		System.out.println("Account address: " + restoreCredentials.getAddress());
		System.out.println("Account Details:");
		System.out.println("Your New Account : " + restoreCredentials.getAddress());
		System.out.println("Mneminic Code: " + seedPhrase2);
		System.out.println("Private Key: " + restoredPrivateKey.getPrivateKey().toString(16));
		System.out.println("Public Key: " + restoredPrivateKey.getPublicKey().toString(16));
	}
}
