package edu.mycoin;

import java.util.List;

public class MyCoinNodeLauncher {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("--- MYCOIN EDUCATIONAL BLOCKCHAIN NODE ---");
        System.out.println("=========================================================\n");

        String dataDirectory = "./mycoin_data";
        int networkPort = 8333;

        try {
            BlockchainStorage storage = new BlockchainStorage(dataDirectory);
            Blockchain blockchain = new Blockchain();

            List<Block> historicalBlocks = storage.loadChain();
            if (!historicalBlocks.isEmpty()) {
                System.out.println("Loading " + historicalBlocks.size() + " blocks from disk...");
                for (Block b : historicalBlocks) {
                    if (b.previousHash.equals("0".repeat(64))) {
                        blockchain.addGenesis(b);
                    } else {
                        blockchain.addBlock(b);
                    }
                }
            } else {
                System.out.println("Creating Genesis Block...");
                Block genesis = new Block();
                genesis.previousHash = "0".repeat(64);
                genesis.timestamp = System.currentTimeMillis();
                genesis.difficultyTarget = 3;
                genesis.citySeed = 777;

                Transaction coinbaseTx = Transaction.createCoinbase(new Wallet().getAddress(), 50_00000000L);
                genesis.transactions.add(coinbaseTx);

                blockchain.addGenesis(genesis);
                storage.saveChain(blockchain.getChain());
                storage.saveUtxo(blockchain.getUtxoSet());
            }

            SimpleNode p2pNode = new SimpleNode(networkPort, blockchain);
            p2pNode.start();

            Wallet senderWallet = new Wallet();
            System.out.println("\nMiner Address: " + senderWallet.getAddress());

            Block candidateBlock = new Block();
            candidateBlock.previousHash = blockchain.getLatestBlock().hash;
            candidateBlock.timestamp = System.currentTimeMillis();
            candidateBlock.difficultyTarget = 3;

            Transaction minerRewardTx = Transaction.createCoinbase(senderWallet.getAddress(), 25_00000000L);
            candidateBlock.transactions.add(minerRewardTx);

            MiningEngine miner = new MiningEngine();
            boolean mineSuccess = miner.mineBlock(candidateBlock, 10_000_000);

            if (mineSuccess) {
                boolean blockAccepted = blockchain.addBlock(candidateBlock);
                if (blockAccepted) {
                    storage.saveChain(blockchain.getChain());
                    storage.saveUtxo(blockchain.getUtxoSet());
                    System.out.println("Block saved and propagated.");
                    SimpleNode.submitBlock("localhost", networkPort, candidateBlock);
                }
            }

            Thread.sleep(2000);
            p2pNode.stop();
            System.out.println("\nNode stopped cleanly.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
