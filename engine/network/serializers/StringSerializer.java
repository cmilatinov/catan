package network.serializers;

import utils.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StringSerializer extends FieldSerializer<String> {

    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public byte[] serialize(String str) {
        return ByteBuffer.allocate(str.length() + Integer.BYTES)
                .putInt(str.length())
                .put(StringUtils.getBytes(str, ENCODING))
                .array();
    }

    public String deserialize(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int size = buffer.getInt();
        return StringUtils.createFromBytes(Arrays.copyOfRange(data, Integer.BYTES, Integer.BYTES + size), ENCODING);
    }

}
