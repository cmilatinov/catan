package network.serializers;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Serializers {

    public static final List<FieldSerializer<?>> FIELD_SERIALIZERS = List.of(
            new BooleanSerializer(),
            new ShortSerializer(),
            new IntegerSerializer(),
            new LongSerializer(),
            new FloatSerializer(),
            new DoubleSerializer(),
            new StringSerializer(),
            new NetworkEventSerializer(),
            new PublicKeySerializer()
    );

    public static final List<Class<?>> FIELD_TYPE_FROM_ID =
            FIELD_SERIALIZERS.stream()
            .map(Serializers::serializerType)
            .collect(Collectors.toList());

    public static final Map<Class<?>, Integer> FIELD_ID_FROM_TYPE =
            FIELD_TYPE_FROM_ID.stream()
            .collect(Collectors.toMap(clazz -> clazz, FIELD_TYPE_FROM_ID::indexOf));

    public static final Map<Class<?>, FieldSerializer<?>> FIELD_SERIALIZER_FROM_TYPE =
            FIELD_SERIALIZERS.stream()
            .collect(Collectors.toMap(
                    Serializers::serializerType,
                    serializer -> serializer
            ));

    private static Class<?> serializerType(FieldSerializer<?> serializer) {
        Class<?> type = (Class<?>)((ParameterizedType) serializer.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (Byte.class.equals(type))
            return byte.class;
        else if (Short.class.equals(type))
            return short.class;
        else if (Integer.class.equals(type))
            return int.class;
        else if (Long.class.equals(type))
            return long.class;
        else if (Float.class.equals(type))
            return float.class;
        else if (Double.class.equals(type))
            return double.class;
        else if (Boolean.class.equals(type))
            return boolean.class;
        else if (Character.class.equals(type))
            return char.class;
        return type;
    }

}
