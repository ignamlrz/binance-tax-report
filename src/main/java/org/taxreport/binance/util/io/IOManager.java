package org.taxreport.binance.util.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.taxreport.binance.util.crypto.Encryptor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import static org.taxreport.binance.config.Credentials.API_KEY;
import static org.taxreport.binance.config.Credentials.SECRET_KEY;
import static org.taxreport.binance.config.MainConfig.ENCRYPT_DATA;

public class IOManager {
    private static final Logger log = LogManager.getLogger();
    private static final String EXT_BIN = ".bin";
    private static final String PATH_BIN = "bin";
    private static final String EXT_DATA = ".json";
    private static final String PATH_DATA = "data";
    public static final Path APP_DATA_PATH;

    static {
        APP_DATA_PATH = Paths.get("data", API_KEY.substring(0,8));
        if(APP_DATA_PATH.toFile().mkdirs()) {
            log.info("Created directory: " + APP_DATA_PATH.toAbsolutePath());
        }
    }

    /**
     * Method for write encrypted data on a file. The file will be encrypted using user's secret key.
     * @param filename Filename to read
     * @param plaintext Data to store encrypted
     */
    public static void write(String filename, String plaintext) {
        var file = calcFile(filename);
        try (DataOutputStream os = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))){
            if(ENCRYPT_DATA) {
                byte[] ciphertext = Encryptor.encryptAES128(plaintext, SECRET_KEY);
                os.write(ciphertext);
            } else {
                os.writeBytes(plaintext);
            }
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method for read a filename that is encrypted. This file should be encrypted with the same user's secret key.
     * If not same secret key or the file is not found, will return a null
     * @param filename Filename to read
     * @return the data decrypted if exists and was encrypted with secret key, if not return null
     */
    public static String read(String filename) {
        var file = calcFile(filename);
        try (DataInputStream is = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))){
            byte[] data = is.readAllBytes();
            if(ENCRYPT_DATA) {
                return new String(Encryptor.decryptAWS128(data, SECRET_KEY));
            } else {
                return new String(data);
            }
        } catch (FileNotFoundException | GeneralSecurityException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File calcFile(String filename) {
        String extension = (ENCRYPT_DATA) ? EXT_BIN : EXT_DATA;
        String typePath =  (ENCRYPT_DATA) ? PATH_BIN : PATH_DATA;
        File file = APP_DATA_PATH.resolve(typePath).resolve(filename + extension).toFile();
        if(APP_DATA_PATH.resolve(typePath).toFile().mkdirs()) {
            log.info("Path created: " + APP_DATA_PATH.resolve(typePath));
        }
        return file;
    }
}
