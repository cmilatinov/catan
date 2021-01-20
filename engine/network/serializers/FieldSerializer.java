package network.serializers;

public abstract class FieldSerializer<T> {

    public abstract byte[] serialize(T field);

    public abstract T deserialize(byte[] data);

}
