package network.serializers;

public class ShortSerializer extends FieldSerializer<Short> {

    public byte[] serialize(Short value) {
        return new byte[] {
            (byte)(value >>> 8),
            (byte)(int)value
        };
    }

    public Short deserialize(byte[] data) {
        return  (short)(((data[0] & 0xFF) << 8) |
                ((data[1] & 0xFF)));
    }

}
