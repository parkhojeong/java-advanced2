package annotation.validator;

import java.lang.reflect.Field;

public class Validator {
    public static void validate(Object obj) throws Exception{
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(NotEmpty.class)) {
                String value = (String) field.get(obj);
                NotEmpty notEmpty = field.getAnnotation(NotEmpty.class);
                if (value == null || value.trim().isEmpty()) {
                    throw new RuntimeException(notEmpty.message());
                }
            }

            if (field.isAnnotationPresent(Range.class)) {
                int value = field.getInt(obj);
                Range range = field.getAnnotation(Range.class);
                if (value < range.min() || range.max() < value) {
                    throw new RuntimeException(range.message());
                }
            }
        }
    }
}
