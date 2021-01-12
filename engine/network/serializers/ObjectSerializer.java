package network.serializers;

import network.annotations.SerializableField;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ObjectSerializer {

    private static final List<FieldSerializer<?>> serializerList = List.of(
            new StringSerializer(),
            new ShortSerializer(),
            new IntegerSerializer(),
            new LongSerializer(),
            new FloatSerializer(),
            new DoubleSerializer()
    );

    private static final List<Class<?>> classFromId =
            serializerList.stream()
            .map(ObjectSerializer::serializerType)
            .collect(Collectors.toList());

    private static final Map<Class<?>, Integer> classToId =
            classFromId.stream()
            .collect(Collectors.toMap(clazz -> clazz, classFromId::indexOf));

    private static final Map<Class<?>, FieldSerializer<?>> serializers =
            serializerList.stream()
            .collect(Collectors.toMap(
                ObjectSerializer::serializerType,
                serializer -> serializer
            ));

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(Object object) {
        // Get list of annotated fields
        List<Field> fields = Arrays.stream(((Object)object).getClass().getFields())
                .filter(f -> f.isAnnotationPresent(SerializableField.class))
                .collect(Collectors.toList());

        // Calculate total buffer size and serialize each field
        AtomicInteger totalSize = new AtomicInteger();
        totalSize.set(Integer.BYTES);
        List<byte[]> fieldBuffers = fields.stream()
                .map(field -> {
                    try {
                        FieldSerializer<T> serializer = (FieldSerializer<T>) serializers.get(field.getType());
                        byte[] bytes = serializer.serialize((T) field.get(object));
                        int bufferSize = 2 * Integer.BYTES + bytes.length;
                        totalSize.addAndGet(bufferSize);
                        return ByteBuffer.allocate(bufferSize)
                                .putInt(classToId.get(field.getType()))
                                .putInt(bytes.length)
                                .put(bytes)
                                .array();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Concatenate all resulting buffer into one and add its size at the start
        ByteBuffer serialized = ByteBuffer.allocate(totalSize.get())
                .putInt(fieldBuffers.size());
        for (byte[] buffer : fieldBuffers)
            serialized.put(buffer);

        // Return the serialized object as a byte array
        return serialized.array();
    }

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
