package edu.mycoin;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain = new ArrayList<>();
    private final UTXOSet utxoSet = new UTXOSet();

    public void addGenesis(Block genesis) {
        if (!chain.isEmpty()) throw new IllegalStateException("Genesis already exists");
        genesis.calculateHash();
        chain.add(genesis);
        utxoSet.updateWithBlock(genesis);
        System.out.println("Genesis block added: " + genesis.hash);
    }

    public boolean addBlock(Block newBlock) {
        if (chain.isEmpty()) return false;

        Block previous = getLatestBlock();
        if (!newBlock.previousHash.equals(previous.hash)) {
            System.err.println("Invalid previousHash");
            return false;
        }

        newBlock.calculateHash();

        if (!newBlock.meetsDifficulty()) {
            System.err.println("Difficulty not met");
            return false;
        }

        if (!MiningEngine.isCombinatorialValidFromHash(newBlock.hash)) {
            System.err.println("Combinatorial puzzle failed");
            return false;
        }

        chain.add(newBlock);
        utxoSet.updateWithBlock(newBlock);
        System.out.println("Block accepted: " + newBlock.hash);
        return true;
    }

    public Block getLatestBlock() { return chain.get(chain.size() - 1); }
    public List<Block> getChain() { return new ArrayList<>(chain); }
    public UTXOSet getUtxoSet() { return utxoSet; }
}
