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
            new StringSerializer()
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
        if (type == Byte.class)
            return byte.class;
        else if (type == Short.class)
            return short.class;
        else if (type == Integer.class)
            return int.class;
        else if (type == Long.class)
            return long.class;
        else if (type == Float.class)
            return float.class;
        else if (type == Double.class)
            return double.class;
        else if (type == Boolean.class)
            return boolean.class;
        else if (type == Character.class)
            return char.class;
        return type;
    }

}
