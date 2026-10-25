package edu.mycoin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class CryptoUtils {

    private CryptoUtils() {}

    public static byte[] sha256(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] doubleSha256(byte[] input) {
        return sha256(sha256(input));
    }

    public static String bytesToHex(byte[] bytes) {
        return HexFormat.of().formatHex(bytes);
    }

    public static byte[] hexToBytes(String hex) {
        return HexFormat.of().parseHex(hex);
    }
}
