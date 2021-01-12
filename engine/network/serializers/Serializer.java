package network.serializers;

import java.util.List;

public class Serializer {

    public static final List<FieldSerializer<?>> serializers =
            List.of(
                    new StringSerializer(),
                    new ShortSerializer(),
                    new IntegerSerializer(),
                    new LongSerializer(),
                    new FloatSerializer(),
                    new DoubleSerializer()
            );
    
}
