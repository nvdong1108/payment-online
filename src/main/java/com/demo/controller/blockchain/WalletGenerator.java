package com.demo.controller.blockchain;

import java.io.File;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.WalletUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WalletGenerator {
    public static void main(String[] args) {

        try {
            String walletDir = "D:\\DONGNV\\git\\payment-online\\wallet";
            File file = new File(walletDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String password = "Vandong123";

            Bip39Wallet wallet = WalletUtils.generateBip39Wallet(password, file);
            log.info("\nFile name: " + wallet.getFilename());
            log.info(" \nMnemonic: " + wallet.getMnemonic());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
}
