package edu.mycoin;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class BlockchainStorage {
    private final Path dataDir;
    private final Path chainFile;

    public BlockchainStorage(String directory) throws IOException {
        this.dataDir = Paths.get(directory);
        Files.createDirectories(dataDir);
        this.chainFile = dataDir.resolve("chain.dat");
    }

    public void saveChain(List<Block> chain) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(chainFile)))) {
            oos.writeObject(new ArrayList<>(chain));
        }
        System.out.println("Chain saved (" + chain.size() + " blocks)");
    }

    @SuppressWarnings("unchecked")
    public List<Block> loadChain() throws IOException, ClassNotFoundException {
        if (!Files.exists(chainFile)) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(chainFile)))) {
            return (List<Block>) ois.readObject();
        }
    }

    public void saveUtxo(UTXOSet utxoSet) throws IOException {
        Path utxoFile = dataDir.resolve("utxo.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(utxoFile))) {
            oos.writeObject(utxoSet);
        }
    }
}
