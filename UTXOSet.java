package edu.mycoin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UTXOSet implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Transaction.Output> utxos = new HashMap<>();

    public void updateWithBlock(Block block) {
        for (Transaction tx : block.transactions) {
            if (!tx.isCoinbase()) {
                for (Transaction.Input in : tx.inputs) {
                    utxos.remove(in.prevTxId + ":" + in.outputIndex);
                }
            }
            for (int i = 0; i < tx.outputs.size(); i++) {
                utxos.put(tx.txId + ":" + i, tx.outputs.get(i));
            }
        }
    }

    public long getBalance(String address) {
        return utxos.values().stream()
                .filter(o -> o.address.equals(address))
                .mapToLong(o -> o.amount)
                .sum();
    }
}
