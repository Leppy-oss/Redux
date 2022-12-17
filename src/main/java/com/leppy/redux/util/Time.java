package com.leppy.redux.util;

public final class Time {
    public static long startTime = System.nanoTime();

    public static double time(Resolution res) {
        return (double) (System.nanoTime() - startTime) * res.mult;
    }

    public static double time() {
        return time(Resolution.S);
    }

    public static void reset() {
        Time.startTime = System.nanoTime();
    }

    public enum Resolution {
        NS(1),
        MS(1E-3),
        S(1E-9);

        public final double mult;

        private Resolution(double mult) {
            this.mult = mult;
        }
    }
}
