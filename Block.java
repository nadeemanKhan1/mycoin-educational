package edu.mycoin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;

    public int version = 1;
    public String previousHash;
    public String merkleRoot = "0".repeat(64);
    public long timestamp;
    public long difficultyTarget;
    public long nonce;
    public int citySeed;

    public List<Transaction> transactions = new ArrayList<>();
    public String hash;

    public byte[] serializeHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append(version).append(previousHash).append(merkleRoot)
          .append(timestamp).append(difficultyTarget).append(nonce).append(citySeed);
        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    public void calculateHash() {
        this.hash = CryptoUtils.bytesToHex(CryptoUtils.doubleSha256(serializeHeader()));
    }

    public boolean meetsDifficulty() {
        int requiredZeros = (int) difficultyTarget;
        if (requiredZeros <= 0) return true;
        return hash != null && hash.startsWith("0".repeat(Math.min(requiredZeros, 16)));
    }
}
