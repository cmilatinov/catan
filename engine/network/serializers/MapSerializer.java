package network.serializers;

import test.Test;

import java.util.Map;

public class MapSerializer extends FieldSerializer<Map<?, ?>> {

    public byte[] serialize(Map<?, ?> field) {
        return new byte[0];
    }

    public Map<?, ?> deserialize(byte[] data) {
        Class<?> test = Test.class;
        test.hashCode();
        ObjectSerializer.deserialize(Test.class, null);
        return null;
    }

}
