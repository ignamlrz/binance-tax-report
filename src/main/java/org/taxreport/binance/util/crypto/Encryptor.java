package org.taxreport.binance.util.crypto;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

import static org.taxreport.binance.config.Credentials.API_KEY;
import static org.taxreport.binance.util.io.IOManager.APP_DATA_PATH;
public class Encryptor {
    private static final Logger log = LogManager.getLogger();
    private static final Aead aes128aead;
    private static final String keySetFilename = "keyset.json";

    static {
        Path keySetPath = APP_DATA_PATH.resolve(keySetFilename);
        KeysetHandle keySetHandle;

        try {
            AeadConfig.register();
            keySetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(keySetPath.toFile()));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            try {
                keySetHandle = KeysetHandle.generateNew(KeyTemplates.get("AES256_GCM"));
                CleartextKeysetHandle.write(keySetHandle, JsonKeysetWriter.withFile(keySetPath.toFile()));
                log.info("Created KeySet for user: " + API_KEY.substring(0,8));
            } catch (GeneralSecurityException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            aes128aead = keySetHandle.getPrimitive(Aead.class);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptAES128(String plaintext, String aad) throws GeneralSecurityException {
        return aes128aead.encrypt(plaintext.getBytes(), aad.getBytes());
    }

    public static byte[] decryptAWS128(byte[] ciphertext, String aad) throws GeneralSecurityException {
        return aes128aead.decrypt(ciphertext, aad.getBytes());
    }
}
