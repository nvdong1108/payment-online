package com.demo.controller.blockchain;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import java.io.File;

public class KeystoreDecryptor {
    public static void main(String[] args) {
        String keystorePath = "D:\\DONGNV\\git\\payment-online\\wallet\\UTC--2025-03-04T03-36-11.320171700Z--25b09f84819e53573b5c59201ad259040e2c3325.json"; // Đường dẫn tới file keystore JSON
        String password = "Vandong123"; // Mật khẩu của bạn (cần nhập đúng)

        try {
            Credentials credentials = WalletUtils.loadCredentials(password, new File(keystorePath));
            String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);
            System.out.println("🔑 Private Key: " + privateKey);
        } catch (Exception e) {
            System.out.println("❌ Giải mã thất bại: " + e.getMessage());
        }
    }
}
// 4b0537c2dae73398ff090f87515965ccfe4c9e23deec8b7a65ca8cdc006d89d2
