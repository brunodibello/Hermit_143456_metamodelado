/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.datatypes.binarydata;

import java.io.ByteArrayOutputStream;
import org.semanticweb.HermiT.datatypes.binarydata.Base64;
import org.semanticweb.HermiT.datatypes.binarydata.BinaryDataType;

public class BinaryData {
    protected static final char[] INT_TO_HEX;
    protected static final int[] HEX_TO_INT;
    protected final BinaryDataType m_binaryDataType;
    protected final byte[] m_data;
    protected final int m_hashCode;

    public BinaryData(BinaryDataType binaryDataType, byte[] data) {
        this.m_binaryDataType = binaryDataType;
        this.m_data = data;
        int hashCode = binaryDataType.hashCode();
        for (int index = 0; index < this.m_data.length; ++index) {
            hashCode = hashCode * 3 + this.m_data[index];
        }
        this.m_hashCode = hashCode;
    }

    public BinaryDataType getBinaryDataType() {
        return this.m_binaryDataType;
    }

    public int getNumberOfBytes() {
        return this.m_data.length;
    }

    public byte getByte(int index) {
        return this.m_data[index];
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof BinaryData)) {
            return false;
        }
        BinaryData thatData = (BinaryData)that;
        if (this.m_hashCode != thatData.m_hashCode || this.m_data.length != thatData.m_data.length || this.m_binaryDataType != thatData.m_binaryDataType) {
            return false;
        }
        for (int index = this.m_data.length - 1; index >= 0; --index) {
            if (this.m_data[index] == thatData.m_data[index]) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.m_hashCode;
    }

    public String toString() {
        switch (this.m_binaryDataType) {
            case HEX_BINARY: {
                return this.toHexBinary();
            }
            case BASE_64_BINARY: {
                return Base64.base64Encode(this.m_data);
            }
        }
        throw new IllegalStateException("Internal error: invalid binary data type.");
    }

    protected String toHexBinary() {
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < this.m_data.length; ++index) {
            int octet = this.m_data[index] & 255;
            int high = octet / 16;
            int low = octet % 16;
            buffer.append(INT_TO_HEX[high]);
            buffer.append(INT_TO_HEX[low]);
        }
        return buffer.toString();
    }

    public static BinaryData parseHexBinary(String lexicalForm) {
        try {
            if (lexicalForm.length() % 2 != 0) {
                return null;
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            int index = 0;
            while (index < lexicalForm.length()) {
                int low;
                int high;
                char digit1;
                char digit2;
                if ((high = HEX_TO_INT[digit1 = lexicalForm.charAt(index++)]) < 0) {
                    return null;
                }
                if ((low = HEX_TO_INT[digit2 = lexicalForm.charAt(index++)]) < 0) {
                    return null;
                }
                int octet = high * 16 + low;
                result.write(octet);
            }
            return new BinaryData(BinaryDataType.HEX_BINARY, result.toByteArray());
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static BinaryData parseBase64Binary(String lexicalForm) {
        try {
            byte[] data = Base64.decodeBase64(BinaryData.removeWhitespace(lexicalForm));
            return new BinaryData(BinaryDataType.HEX_BINARY, data);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException error) {
            return null;
        }
    }

    protected static String removeWhitespace(String lexicalForm) {
        StringBuilder b = new StringBuilder(lexicalForm);
        int i = 0;
        while (i < b.length()) {
            if (Character.isWhitespace(b.charAt(i))) {
                b.deleteCharAt(i);
                continue;
            }
            ++i;
        }
        return b.toString();
    }

    static {
        int i;
        INT_TO_HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        HEX_TO_INT = new int[127];
        for (i = 0; i < HEX_TO_INT.length; ++i) {
            BinaryData.HEX_TO_INT[i] = -1;
        }
        for (i = 48; i <= 57; ++i) {
            BinaryData.HEX_TO_INT[i] = i - 48;
        }
        for (i = 65; i <= 70; ++i) {
            BinaryData.HEX_TO_INT[i] = i - 65 + 10;
        }
        for (i = 97; i <= 102; ++i) {
            BinaryData.HEX_TO_INT[i] = i - 97 + 10;
        }
    }

}

