package network.serializers;

import java.nio.ByteBuffer;

public class DoubleSerializer extends FieldSerializer<Double> {

    public byte[] serialize(Double value) {
        return ByteBuffer.allocate(Double.BYTES)
                .putDouble(value)
                .array();
    }

    public Double deserialize(byte[] data) {
        return ByteBuffer.wrap(data)
                .getDouble();
    }

}
