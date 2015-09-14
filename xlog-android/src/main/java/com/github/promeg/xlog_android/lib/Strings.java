package com.github.promeg.xlog_android.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * from https://github.com/JakeWharton/hugo
 */
final class Strings {

    static final int LOG_CONTENT_MAX_LENGTH = 1000;

    static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof CharSequence) {
            return '"' + printableToString(obj.toString()) + '"';
        }

        if (obj instanceof Collection) {
            return collectionToString((Collection) obj);
        }

        Class<?> cls = obj.getClass();
        if (Byte.class == cls) {
            return byteToString((Byte) obj);
        }

        if (cls.isArray()) {
            return arrayToString(cls.getComponentType(), obj);
        }
        return obj.toString();
    }

    private static String printableToString(String string) {
        int length = string.length();
        StringBuilder builder = new StringBuilder(length);
        //CHECKSTYLE:OFF
        for (int i = 0; i < length; ) {
            //CHECKSTYLE:ON
            int codePoint = string.codePointAt(i);
            switch (Character.getType(codePoint)) {
                case Character.CONTROL:
                case Character.FORMAT:
                case Character.PRIVATE_USE:
                case Character.SURROGATE:
                case Character.UNASSIGNED:
                    switch (codePoint) {
                        case '\n':
                            builder.append("\\n");
                            break;
                        case '\r':
                            builder.append("\\r");
                            break;
                        case '\t':
                            builder.append("\\t");
                            break;
                        case '\f':
                            builder.append("\\f");
                            break;
                        case '\b':
                            builder.append("\\b");
                            break;
                        default:
                            builder.append("\\u").append(String.format("%04x", codePoint)
                                    .toUpperCase(Locale.US));
                            break;
                    }
                    break;
                default:
                    builder.append(Character.toChars(codePoint));
                    break;
            }
            i += Character.charCount(codePoint);
        }
        return builder.toString();
    }

    private static String collectionToString(Collection collection) {
        StringBuilder builder = new StringBuilder("[");
        int count = 0;
        for (Object element : collection) {
            if (builder.length() > LOG_CONTENT_MAX_LENGTH) {
                return builder.append("] (" + count + ":" + collection.size() + ")").toString();
            }
            if (count > 0) {
                builder.append(", ");
            }
            count++;
            if (element == null) {
                builder.append("null");
            } else {
                Class elementClass = element.getClass();
                if (elementClass.isArray() && elementClass.getComponentType() == Object.class) {
                    Object[] arrayElement = (Object[]) element;
                    arrayToString(arrayElement, builder, new HashSet<Object[]>());
                } else {
                    builder.append(toString(element));
                }
            }
        }
        return builder.append(']').toString();
    }

    private static String arrayToString(Class<?> cls, Object obj) {
        if (byte.class == cls) {
            return byteArrayToString((byte[]) obj);
        }
        if (short.class == cls) {
            return XLogArrays.toStringPartly((short[]) obj, LOG_CONTENT_MAX_LENGTH);
        }
        if (char.class == cls) {
            return XLogArrays.toStringPartly((char[]) obj, LOG_CONTENT_MAX_LENGTH);
        }
        if (int.class == cls) {
            return XLogArrays.toStringPartly((int[]) obj, LOG_CONTENT_MAX_LENGTH);
        }
        if (long.class == cls) {
            return XLogArrays.toStringPartly((long[]) obj, LOG_CONTENT_MAX_LENGTH);
        }
        if (float.class == cls) {
            return XLogArrays.toStringPartly((float[]) obj, LOG_CONTENT_MAX_LENGTH);
        }
        if (double.class == cls) {
            return XLogArrays.toStringPartly((double[]) obj, LOG_CONTENT_MAX_LENGTH);
        }
        if (boolean.class == cls) {
            return XLogArrays.toStringPartly((boolean[]) obj, LOG_CONTENT_MAX_LENGTH);
        }
        return arrayToString((Object[]) obj);
    }

    /**
     * A more human-friendly version of Arrays#toString(byte[]) that uses hex representation.
     */
    private static String byteArrayToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < bytes.length && builder.length() < LOG_CONTENT_MAX_LENGTH; i++) {
            if (builder.length() > LOG_CONTENT_MAX_LENGTH) {
                return builder.append("] (" + i + ":" + bytes.length + ")").toString();
            }
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(byteToString(bytes[i]));
        }
        return builder.append(']').toString();
    }

    private static String byteToString(Byte b) {
        if (b == null) {
            return "null";
        }
        return "0x" + String.format("%02x", b).toUpperCase(Locale.US);
    }

    private static String arrayToString(Object[] array) {
        StringBuilder buf = new StringBuilder();
        arrayToString(array, buf, new HashSet<Object[]>());
        return buf.toString();
    }

    private static void arrayToString(Object[] array, StringBuilder builder, Set<Object[]> seen) {
        if (array == null) {
            builder.append("null");
            return;
        }

        seen.add(array);
        builder.append('[');
        for (int i = 0; i < array.length; i++) {
            if (builder.length() > LOG_CONTENT_MAX_LENGTH) {
                builder.append("] (" + i + ":" + array.length + ")");
                seen.remove(array);
                return;
            }
            if (i > 0) {
                builder.append(", ");
            }

            Object element = array[i];
            if (element == null) {
                builder.append("null");
            } else {
                Class elementClass = element.getClass();
                if (elementClass.isArray() && elementClass.getComponentType() == Object.class) {
                    Object[] arrayElement = (Object[]) element;
                    if (seen.contains(arrayElement)) {
                        builder.append("[...]");
                    } else {
                        arrayToString(arrayElement, builder, seen);
                    }
                } else {
                    builder.append(toString(element));
                }
            }
        }
        builder.append(']');
        seen.remove(array);
    }

    private Strings() {
        throw new AssertionError("No instances.");
    }
}
