package network.serializers;

import java.nio.ByteBuffer;

public class FloatSerializer extends FieldSerializer<Float> {

    public byte[] serialize(Float value) {
        return ByteBuffer.allocate(Float.BYTES)
                .putFloat(value)
                .array();
    }

    public Float deserialize(byte[] data) {
        return ByteBuffer.wrap(data)
                .getFloat();
    }

}
