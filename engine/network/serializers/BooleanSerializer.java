package network.serializers;

public class BooleanSerializer extends FieldSerializer<Boolean> {

    public byte[] serialize(Boolean value) {
        return new byte[] {
            value ? (byte)0x1
                  : (byte)0x0
        };
    }

    public Boolean deserialize(byte[] data) {
        return data[0] > 0;
    }

}
