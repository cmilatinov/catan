package network.serializers;

import network.RSA;

import java.security.PublicKey;

public class PublicKeySerializer extends FieldSerializer<PublicKey> {

    public byte[] serialize(PublicKey data) {
        return data.getEncoded();
    }

    public PublicKey deserialize(byte[] data) {
        return RSA.toPublicKey(data);
    }

}
