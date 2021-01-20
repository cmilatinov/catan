package network.serializers;

import utils.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringSerializer extends FieldSerializer<String> {

    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public byte[] serialize(String str) {
        return StringUtils.getBytes(str, ENCODING);
    }

    public String deserialize(byte[] data) {
        return StringUtils.createFromBytes(data, ENCODING);
    }

}
