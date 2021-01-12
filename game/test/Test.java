package test;

import network.annotations.SerializableField;
import network.serializers.ObjectSerializer;

import java.util.Arrays;

public class Test {

    @SerializableField
    public final int testInt = 0;

    @SerializableField
    public final String testStr = "hehe xd";

    @SerializableField
    public final long testLong = 2;

    @SerializableField
    public final double testDouble = 4.3;

    public static void main(String[] args) {
        byte[] array = ObjectSerializer.serialize(new Test());
        System.out.println(Arrays.toString(array));
    }

}
