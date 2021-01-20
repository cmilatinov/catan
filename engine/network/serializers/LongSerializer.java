package network.serializers;

public class LongSerializer extends FieldSerializer<Long> {

    public byte[] serialize(Long value) {
        return new byte[] {
            (byte)(value >>> 56),
            (byte)(value >>> 48),
            (byte)(value >>> 40),
            (byte)(value >>> 32),
            (byte)(value >>> 24),
            (byte)(value >>> 16),
            (byte)(value >>> 8),
            (byte)(long)value
        };
    }

    public Long deserialize(byte[] data) {
        return  ((long)(data[0] & 0xFF) << 56) |
                ((long)(data[1] & 0xFF) << 48) |
                ((long)(data[2] & 0xFF) << 40) |
                ((long)(data[3] & 0xFF) << 32) |
                ((long)(data[4] & 0xFF) << 24) |
                ((long)(data[5] & 0xFF) << 16) |
                ((long)(data[6] & 0xFF) << 8) |
                ((long)(data[7] & 0xFF));
    }

}
