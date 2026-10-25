package edu.mycoin;

import java.util.concurrent.ThreadLocalRandom;

public class MiningEngine {

    private final int minDistance = 1;
    private final int maxDistance = 50;

    public boolean mineBlock(Block block, long maxAttempts) {
        System.out.println("--- MyCoin Mining Engine Activated ---");
        long attempts = 0;

        while (attempts < maxAttempts) {
            attempts++;
            block.nonce = attempts;
            block.citySeed = ThreadLocalRandom.current().nextInt();
            block.calculateHash();

            int[] distances = extractDistancesFromHash(block.hash);
            long[] cityResults = CityUtils.calculateCityResults(distances);

            if (block.meetsDifficulty() && isCombinatorialValid(cityResults)) {
                System.out.printf("Block mined after %,d attempts%n", attempts);
                System.out.println("Hash: " + block.hash);
                for (int i = 0; i < CityUtils.TOTAL_CITIES; i++) {
                    System.out.printf("  %-14s → distance %2d → C = %d%n",
                            CityUtils.CITIES[i], distances[i], cityResults[i]);
                }
                return true;
            }
        }
        return false;
    }

    private int[] extractDistancesFromHash(String hashHex) {
        int[] distances = new int[CityUtils.TOTAL_CITIES];
        byte[] hashBytes = CryptoUtils.hexToBytes(hashHex);
        for (int i = 0; i < CityUtils.TOTAL_CITIES; i++) {
            int value = Byte.toUnsignedInt(hashBytes[i % hashBytes.length]);
            distances[i] = (value % (maxDistance - minDistance)) + minDistance;
        }
        return distances;
    }

    private boolean isCombinatorialValid(long[] results) {
        long sum = 0;
        for (long r : results) {
            if (r <= 0) return false;
            sum += r;
        }
        return sum % 2 == 0;
    }

    public static boolean isCombinatorialValidFromHash(String hashHex) {
        MiningEngine engine = new MiningEngine();
        int[] distances = engine.extractDistancesFromHash(hashHex);
        return engine.isCombinatorialValid(CityUtils.calculateCityResults(distances));
    }
}
