package network.serializers;

import network.annotations.SerializableField;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ObjectSerializer {

    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(Object object) {
        // Get list of annotated fields
        List<Field> fields = collectFields(object.getClass());

        // Calculate total buffer size and serialize each field
        AtomicInteger totalSize = new AtomicInteger();
        totalSize.set(Integer.BYTES);
        List<byte[]> fieldBuffers = fields.stream()
                .map(field -> {
                    try {
                        // Find appropriate serializer for field, otherwise send empty field
                        FieldSerializer<T> serializer = (FieldSerializer<T>) Serializers.FIELD_SERIALIZER_FROM_TYPE.get(field.getType());
                        if (serializer == null) {
                            totalSize.addAndGet(2 * Integer.BYTES);
                            return ByteBuffer.allocate(2 * Integer.BYTES)
                                    .putInt(-1)
                                    .putInt(0)
                                    .array();
                        }

                        // Serialize field and create byte array starting with the field type and size
                        field.setAccessible(true);
                        byte[] bytes = serializer.serialize((T) field.get(object));
                        field.setAccessible(false);
                        int bufferSize = 2 * Integer.BYTES + bytes.length;
                        totalSize.addAndGet(bufferSize);
                        return ByteBuffer.allocate(bufferSize)
                                .putInt(Serializers.FIELD_ID_FROM_TYPE.get(field.getType()))
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

    @SuppressWarnings("unchecked")
    public static <T, F> T deserialize(Class<T> classType, byte[] data) {
        try {
            // Wrap buffer and get number of fields
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int numFields = buffer.getInt();

            // Check number of fields matches
            List<Field> classFields = collectFields(classType);
            if (numFields != classFields.size())
                return null;

            // Create instance using empty constructor
            T instance = classType.getConstructor().newInstance();

            for (Field field : classFields) {
                // Get field type and data
                int fieldType = buffer.getInt();
                int fieldSize = buffer.getInt();
                byte[] fieldBuffer = new byte[fieldSize];
                buffer.get(fieldBuffer);

                // Not a supported serializable type
                if (fieldType < 0)
                    continue;

                // Find serializer and use to deserialize data
                FieldSerializer<F> serializer = (FieldSerializer<F>) Serializers.FIELD_SERIALIZER_FROM_TYPE.get(Serializers.FIELD_TYPE_FROM_ID.get(fieldType));
                F value = serializer.deserialize(fieldBuffer);
                field.setAccessible(true);
                field.set(instance, value);
                field.setAccessible(false);
            }

            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    private static List<Field> collectFields(Class<?> classType) {
        return Arrays.stream(classType.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(SerializableField.class))
                .collect(Collectors.toList());
    }

}
