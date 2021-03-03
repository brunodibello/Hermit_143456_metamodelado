package org.semanticweb.HermiT.datatypes.binarydata;

public class Base64 {
    private static final char[] TO_BASE_64 = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] FROM_BASE_64 = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};

    public static String base64Encode(byte[] array) {
        int arrayLength = array.length;
        int nummerOfFullGroups = arrayLength / 3;
        int bytesInLastGroup = arrayLength - 3 * nummerOfFullGroups;
        StringBuffer result = new StringBuffer((arrayLength + 2) / 3 * 4);
        int index = 0;
        for (int i = 0; i < nummerOfFullGroups; ++i) {
            int byte0 = array[index++] & 255;
            int byte1 = array[index++] & 255;
            int byte2 = array[index++] & 255;
            result.append(TO_BASE_64[byte0 >> 2]);
            result.append(TO_BASE_64[byte0 << 4 & 63 | byte1 >> 4]);
            result.append(TO_BASE_64[byte1 << 2 & 63 | byte2 >> 6]);
            result.append(TO_BASE_64[byte2 & 63]);
        }
        if (bytesInLastGroup != 0) {
            int byte0 = array[index++] & 255;
            result.append(TO_BASE_64[byte0 >> 2]);
            if (bytesInLastGroup == 1) {
                result.append(TO_BASE_64[byte0 << 4 & 63]);
                result.append("==");
            } else {
                int byte1 = array[index++] & 255;
                result.append(TO_BASE_64[byte0 << 4 & 63 | byte1 >> 4]);
                result.append(TO_BASE_64[byte1 << 2 & 63]);
                result.append('=');
            }
        }
        return result.toString();
    }

    public static byte[] decodeBase64(String string) throws IllegalArgumentException {
        int stringLength = string.length();
        if (stringLength % 4 != 0) {
            throw new IllegalArgumentException("The length of the string must be divisible by 4.");
        }
        int numberOfGroups = stringLength / 4;
        int missingBytesInLastGroup = 0;
        int numberOfFullGroups = numberOfGroups;
        if (stringLength != 0) {
            if (string.charAt(stringLength - 1) == '=') {
                ++missingBytesInLastGroup;
                --numberOfFullGroups;
            }
            if (string.charAt(stringLength - 2) == '=') {
                ++missingBytesInLastGroup;
            }
        }
        byte[] result = new byte[3 * numberOfGroups - missingBytesInLastGroup];
        int inputIndex = 0;
        int outputIndex = 0;
        for (int i = 0; i < numberOfFullGroups; ++i) {
            int b0 = Base64.decodeChar(string.charAt(inputIndex++));
            int b1 = Base64.decodeChar(string.charAt(inputIndex++));
            int b2 = Base64.decodeChar(string.charAt(inputIndex++));
            int b3 = Base64.decodeChar(string.charAt(inputIndex++));
            result[outputIndex++] = (byte)(b0 << 2 | b1 >> 4);
            result[outputIndex++] = (byte)(b1 << 4 | b2 >> 2);
            result[outputIndex++] = (byte)(b2 << 6 | b3);
        }
        if (missingBytesInLastGroup != 0) {
            int b0 = Base64.decodeChar(string.charAt(inputIndex++));
            int b1 = Base64.decodeChar(string.charAt(inputIndex++));
            result[outputIndex++] = (byte)(b0 << 2 | b1 >> 4);
            if (missingBytesInLastGroup == 1) {
                int b2 = Base64.decodeChar(string.charAt(inputIndex++));
                result[outputIndex++] = (byte)(b1 << 4 | b2 >> 2);
            }
        }
        return result;
    }

    private static int decodeChar(char c) {
        byte result = FROM_BASE_64[c];
        if (result < 0) {
            throw new IllegalArgumentException("Illegal BASE64 character " + c + ".");
        }
        return result;
    }
}

