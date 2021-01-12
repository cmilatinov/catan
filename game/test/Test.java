package test;

import network.annotations.SerializableField;
import network.serializers.ObjectSerializer;

public class Test {

    @SerializableField
    private final int testInt = 0;

    @SerializableField
    private final String testStr = "hehe xd";

    @SerializableField
    private final long testLong = 2;

    @SerializableField
    private final double testDouble = 4.3;

    public static void main(String[] args) {
        byte[] array = ObjectSerializer.serialize(new Test());
        Test a = ObjectSerializer.deserialize(Test.class, array);
        if (a == null)
            return;
        System.out.println(a.testInt);
        System.out.println(a.testStr);
        System.out.println(a.testLong);
        System.out.println(a.testDouble);
    }

}
