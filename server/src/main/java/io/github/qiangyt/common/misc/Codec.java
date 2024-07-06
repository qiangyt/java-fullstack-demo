package io.github.qiangyt.common.misc;

import org.apache.commons.codec.binary.Base64;

/**
 * Encoding and Decoding Tools
 */
public class Codec {

    /**
     * Converts byte array encoded to Base64 (suitable for URL)
     */
    public static String bytesToBase64(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.encodeBase64URLSafeString(bytes);
    }

    /**
     * Converts base64 to byte array
     */
    public static byte[] base64ToBytes(String base64ed) {
        if (base64ed == null) {
            return null;
        }
        return Base64.decodeBase64(base64ed + "==");
    }

    /**
     * Converts long to byte array
     */
    static void longTobytes(long value, byte[] bytes, int offset) {
        for (int i = 7; i > -1; i--) {
            bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
        }
    }

    /**
     * Converts byte array to long
     */
    static long bytesTolong(byte[] bytes, int offset) {
        long r = 0;
        for (int i = 7; i > -1; i--) {
            r |= (((long) bytes[offset++]) & 0xFF) << 8 * i;
        }
        return r;
    }

}
