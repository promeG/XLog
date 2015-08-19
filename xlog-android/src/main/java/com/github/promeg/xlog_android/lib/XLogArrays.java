package com.github.promeg.xlog_android.lib;

public class XLogArrays {

    private static final int RedundancyLength = 20;

    public static String toStringPartly(boolean[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 7 < maxLength) ? (array.length * 7)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }


    public static String toStringPartly(byte[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 6 < maxLength) ? (array.length * 6)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toStringPartly(char[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 3 < maxLength) ? (array.length * 3)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toStringPartly(double[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 7 < maxLength) ? (array.length * 7)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toStringPartly(float[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 7 < maxLength) ? (array.length * 7)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toStringPartly(int[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 6 < maxLength) ? (array.length * 6)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toStringPartly(long[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 6 < maxLength) ? (array.length * 6)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toStringPartly(short[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 6 < maxLength) ? (array.length * 6)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }


    public static String toStringPartly(Object[] array, int maxLength) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        int preAllocLength = (array.length * 7 < maxLength) ? (array.length * 7)
                : (maxLength + RedundancyLength);
        StringBuilder sb = new StringBuilder(preAllocLength);
        sb.append('[');
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (sb.length() >= maxLength) {
                return sb.append("] (" + i + ":" + array.length + ")").toString();
            }
            sb.append(", ");
            sb.append(array[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}

