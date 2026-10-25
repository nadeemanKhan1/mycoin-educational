package edu.mycoin;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleNode {
    private final int port;
    private final Blockchain blockchain;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    public SimpleNode(int port, Blockchain blockchain) {
        this.port = port;
        this.blockchain = blockchain;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Node listening on port " + port);
        pool.submit(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket client = serverSocket.accept();
                    pool.submit(() -> handleClient(client));
                } catch (IOException ignored) {}
            }
        });
    }

    private void handleClient(Socket socket) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            String command = (String) in.readObject();
            switch (command) {
                case "GET_HEIGHT" -> out.writeObject(blockchain.getChain().size());
                case "GET_LATEST" -> out.writeObject(blockchain.getLatestBlock());
                case "SUBMIT_BLOCK" -> {
                    Block block = (Block) in.readObject();
                    out.writeObject(blockchain.addBlock(block));
                }
                default -> out.writeObject("UNKNOWN");
            }
            out.flush();
        } catch (Exception ignored) {}
    }

    public void stop() throws IOException {
        if (serverSocket != null) serverSocket.close();
        pool.shutdownNow();
    }

    public static boolean submitBlock(String host, int port, Block block) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.writeObject("SUBMIT_BLOCK");
            out.writeObject(block);
            out.flush();
            return (Boolean) in.readObject();
        } catch (Exception e) {
            return false;
        }
    }
}
