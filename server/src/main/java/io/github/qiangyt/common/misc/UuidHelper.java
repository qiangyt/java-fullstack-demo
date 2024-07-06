package io.github.qiangyt.common.misc;

import java.util.UUID;

/**
 * Utility class for UUID
 */
public class UuidHelper {

    /**
     * Generate short UUID string (22 characters)
     *
     * @return
     */
    public static String shortUuid() {
        var uuid = UUID.randomUUID();
        return compress(uuid);
    }

    /**
     * Compress the UUID object into a short UUID string.
     */
    public static String compress(UUID uuid) {
        byte[] byUuid = new byte[16];
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();
        Codec.longTobytes(most, byUuid, 0);
        Codec.longTobytes(least, byUuid, 8);
        return Codec.bytesToBase64(byUuid);
    }

    /**
     * Decompress the short UUID string into a UUID object.
     */
    public static UUID uncompress(String compressedUuid) {
        if (compressedUuid.length() != 22) {
            throw new IllegalArgumentException("Invalid uuid!");
        }
        byte[] byUuid = Codec.base64ToBytes(compressedUuid);
        long most = Codec.bytesTolong(byUuid, 0);
        long least = Codec.bytesTolong(byUuid, 8);
        return new UUID(most, least);
    }

}
