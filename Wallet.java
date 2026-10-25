package edu.mycoin;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;

    private final KeyPair keyPair;
    private final String address;

    public Wallet() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
            gen.initialize(256);
            keyPair = gen.generateKeyPair();
            address = "M" + Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(CryptoUtils.sha256(keyPair.getPublic().getEncoded()))
                    .substring(0, 30);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAddress() { return address; }
    public PrivateKey getPrivateKey() { return keyPair.getPrivate(); }
    public PublicKey getPublicKey() { return keyPair.getPublic(); }
}
