package org.taxreport.binance.util.crypto;

import org.junit.Test;

import java.security.GeneralSecurityException;

import static junit.framework.TestCase.assertEquals;

public class EncryptorTests {

    @Test
    public void encryptDecrypt() throws GeneralSecurityException {
        var plaintext = "Plain Text";
        var aad = "1a2b3c4d5e6f7g8h";
        var ciphertext = Encryptor.encryptAES128(plaintext, aad);
        var result = Encryptor.decryptAWS128(ciphertext, aad);
        assertEquals(plaintext, new String(result));
    }

    @Test(expected = GeneralSecurityException.class)
    public void encryptDecryptJsonWithDifferentAad() throws GeneralSecurityException {
        String json = "{\n" +
                "    \"code\": \"000000\",\n" +
                "    \"message\": \"success\",\n" +
                "    \"total\": 28,\n" +
                "    \"success\": true\n" +
                "}";

        var aad1 = "1a2b3c4d5e6f7g8h";
        var aad2 = "a1b2c3d4e5f6g7h8";
        var ciphertext = Encryptor.encryptAES128(json, aad1);
        Encryptor.decryptAWS128(ciphertext, aad2);
    }

    @Test
    public void encryptDecryptJson() throws GeneralSecurityException {
        String json = "{\n" +
                "    \"code\": \"000000\",\n" +
                "    \"message\": \"success\",\n" +
                "    \"total\": 28,\n" +
                "    \"success\": true\n" +
                "}";

        var aad = "1a2b3c4d5e6f7g8h";
        var ciphertext = Encryptor.encryptAES128(json, aad);
        var result = Encryptor.decryptAWS128(ciphertext, aad);
        assertEquals(json, new String(result));
    }
}
