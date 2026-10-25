package edu.mycoin;

public final class CityUtils {

    public static final String[] CITIES = {
        "bradford", "mirfield", "leeds", "dewsbury", "huddersfield", "shipley"
    };

    public static final int TOTAL_CITIES = CITIES.length;

    private CityUtils() {}

    public static long combination(int n, int r) {
        if (r < 0 || n < 0 || r > n) return 0;
        if (r > n - r) r = n - r;
        long result = 1;
        for (int i = 1; i <= r; i++) {
            result = result * (n - r + i) / i;
        }
        return result;
    }

    public static long[] calculateCityResults(int[] distances) {
        long[] results = new long[TOTAL_CITIES];
        for (int i = 0; i < TOTAL_CITIES; i++) {
            int d = distances[i];
            if (TOTAL_CITIES > d) {
                results[i] = (d == 0) ? -1 : combination(TOTAL_CITIES, d);
            } else {
                results[i] = combination(d, TOTAL_CITIES);
            }
        }
        return results;
    }
}
