package test;

import network.serializers.FieldSerializer;
import network.serializers.StringSerializer;

import java.lang.reflect.ParameterizedType;

public class Test {

    private static final FieldSerializer<?>[] serializers = new FieldSerializer[1];

    public static void main(String[] args) {
        serializers[0] = new StringSerializer();
        System.out.println(((ParameterizedType) serializers[0].getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

}
