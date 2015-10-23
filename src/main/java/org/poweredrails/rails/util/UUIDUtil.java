package org.poweredrails.rails.util;

import java.util.UUID;

public class UUIDUtil {

    /**
     * Parses and returns the UUID from a flat string.
     * @param str the flat string to converet
     * @return the parsed uuid
     */
    public static UUID fromFlatString(String str) {
        // xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        String parsed = new StringBuilder(str)
                .insert(8,  "-")
                .insert(13, "-")
                .insert(18, "-")
                .insert(23, "-")
                .toString();

        return UUID.fromString(parsed);
    }

    /**
     * Removes the dashes from the UUID to form a flat string.
     * @param uuid the uuid to flatten
     * @return the flattened string
     */
    public static String toFlatString(UUID uuid) {
        String str = uuid.toString();
        return str.replace("-", "");
    }

}
