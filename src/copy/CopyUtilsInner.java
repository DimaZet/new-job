package copy;

import sun.misc.Unsafe;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

class CopyUtilsInner {

    private static final Unsafe unsafe = initUnsafe();
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

    private final Map<Object, Object> copies = new HashMap<>();

    public  <T> T deepCopy(T obj) {
        if (obj == null) {
            return null;
        }

        Class<?> objClass = obj.getClass();
        if (isWrapperType(objClass) || objClass.isEnum()) {
            return obj;
        }

        if (objClass.isArray()) {
            int length = Array.getLength(obj);
            Class<?> elementClass = objClass.getComponentType();
            Object copyArray = Array.newInstance(elementClass, length);
            for (int i = 0; i < length; i++) {
                copyArrayElement(obj, i, copyArray, elementClass);
            }
            return (T) copyArray;
        }

        Object newObj = createEmptyCopy(obj);
        copies.put(obj, newObj);
        for (Field field : objClass.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object sourceFieldValue = field.get(obj);
                Object cachedCopy = copies.get(sourceFieldValue);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

                if (!Modifier.isStatic(field.getModifiers())) {
                    if (cachedCopy == null) {
                        field.set(newObj, deepCopy(sourceFieldValue));
                    } else {
                        field.set(newObj, cachedCopy);
                    }
                }

            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new IllegalStateException("Can't set field " + field.getName(), e);
            }
        }
        return (T) newObj;
    }

    private static Unsafe initUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Can't get unsafe", e);
        }
    }

    private Object createEmptyCopy(Object obj) {
        try {
            return unsafe.allocateInstance(obj.getClass());
        } catch (InstantiationException e) {
            throw new IllegalStateException("Can't create empty copy of " + obj, e);
        }
    }

    private void copyArrayElement(Object source, int index, Object target, Class<?> elementClass) {
        if (boolean.class.equals(elementClass)) {
            Array.setBoolean(target, index, Array.getBoolean(source, index));
        } else if (char.class.equals(elementClass)) {
            Array.setChar(target, index, Array.getChar(source, index));
        } else if (byte.class.equals(elementClass)) {
            Array.setByte(target, index, Array.getByte(source, index));
        } else if (short.class.equals(elementClass)) {
            Array.setShort(target, index, Array.getShort(source, index));
        } else if (int.class.equals(elementClass)) {
            Array.setInt(target, index, Array.getInt(source, index));
        } else if (long.class.equals(elementClass)) {
            Array.setLong(target, index, Array.getLong(source, index));
        } else if (float.class.equals(elementClass)) {
            Array.setFloat(target, index, Array.getFloat(source, index));
        } else if (double.class.equals(elementClass)) {
            Array.setDouble(target, index, Array.getDouble(source, index));
        } else {
            Array.set(target, index, deepCopy(Array.get(source, index)));
        }
    }

    private static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
}
