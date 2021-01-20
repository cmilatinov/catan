package network.serializers;

public class IntegerSerializer extends FieldSerializer<Integer> {

    public byte[] serialize(Integer value) {
        return new byte[] {
            (byte)(value >>> 24),
            (byte)(value >>> 16),
            (byte)(value >>> 8),
            (byte)(int)value
        };
    }

    public Integer deserialize(byte[] data) {
        return  ((data[0] & 0xFF) << 24) |
                ((data[1] & 0xFF) << 16) |
                ((data[2] & 0xFF) << 8) |
                ((data[3] & 0xFF));
    }

}
