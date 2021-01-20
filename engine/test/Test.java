package test;

import network.annotations.SerializableField;
import network.packets.PacketKey;
import network.packets.Packets;

public class Test {

    @SerializableField
    protected final boolean testBoolean = true;

    @SerializableField
    protected final short testShort = 0;

    @SerializableField
    protected final int testInt = 1;

    @SerializableField
    protected final long testLong = 2;

    @SerializableField
    protected final float testFloat = 3.2f;

    @SerializableField
    protected final double testDouble = 4.3d;

    @SerializableField
    protected final String testStr = "hehe xd";

    private static class DerivedTest extends Test {

        @SerializableField
        protected final float cool = 10.0f;

    }

    public static void main(String[] args) {
        System.out.println(Packets.getPacketID(PacketKey.class));
    }

}
