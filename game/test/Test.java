package test;

import network.annotations.SerializableField;
import network.serializers.ObjectSerializer;

public class Test {

    @SerializableField
    private final boolean testBoolean = true;

    @SerializableField
    private final short testShort = 0;

    @SerializableField
    private final int testInt = 1;

    @SerializableField
    private final long testLong = 2;

    @SerializableField
    private final float testFloat = 3.2f;

    @SerializableField
    private final double testDouble = 4.3d;

    @SerializableField
    private final String testStr = "hehe xd";

    public static void main(String[] args) {
        byte[] array = ObjectSerializer.serialize(new Test());
        Test a = ObjectSerializer.deserialize(Test.class, array);
        if (a == null)
            return;
        System.out.println(a.testBoolean);
        System.out.println(a.testShort);
        System.out.println(a.testInt);
        System.out.println(a.testLong);
        System.out.println(a.testFloat);
        System.out.println(a.testDouble);
        System.out.println(a.testStr);
    }

}
