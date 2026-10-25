package edu.mycoin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public static class Input implements Serializable {
        private static final long serialVersionUID = 1L;
        public String prevTxId;
        public int outputIndex;
        public String signature;
        public String publicKey;
    }

    public static class Output implements Serializable {
        private static final long serialVersionUID = 1L;
        public long amount;
        public String address;
    }

    public String txId;
    public List<Input> inputs = new ArrayList<>();
    public List<Output> outputs = new ArrayList<>();
    public long lockTime = 0;

    public boolean isCoinbase() {
        return inputs.isEmpty();
    }

    public static Transaction createCoinbase(String minerAddress, long reward) {
        Transaction tx = new Transaction();
        tx.txId = UUID.randomUUID().toString().replace("-", "");
        Output out = new Output();
        out.amount = reward;
        out.address = minerAddress;
        tx.outputs.add(out);
        return tx;
    }
}
