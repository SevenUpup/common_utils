package com.fido.common.common_base_util.util.reflect;

/**
 * @author: FiDo
 * @date: 2024/4/2
 * @des:
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ReflectUtils {
    public static Object invokeStaticMethod(String class_name, String method_name, Class[] pareTyple, Object[] pareVaules) {
        try {
            Class obj_class = Class.forName(class_name);
            Method method = obj_class.getMethod(method_name, pareTyple);
            return method.invoke(null, pareVaules);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return null;
        } catch (SecurityException e5) {
            e5.printStackTrace();
            return null;
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Class<?> clazz, Object obj, String methodName, Object[] args, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            return null;
        } catch (Exception e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(String className, Object obj, String methodName, Object[] args, Class<?>... parameterTypes) {
        if (parameterTypes == null) {
            try {
                parameterTypes = new Class[0];
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (args == null) {
            args = new Object[0];
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return invokeMethod(clazz, obj, methodName, args, parameterTypes);
    }

    public static Object invokeMethod(String class_name, String method_name, Object obj, Class[] pareTyple, Object[] pareVaules) {
        try {
            Class obj_class = Class.forName(class_name);
            Method method = obj_class.getMethod(method_name, pareTyple);
            return method.invoke(obj, pareVaules);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return null;
        } catch (SecurityException e5) {
            e5.printStackTrace();
            return null;
        } catch (InvocationTargetException e6) {
            e6.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(Class<?> clazz, Object obj, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return null;
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static void setFieldOjbect(String classname, String filedName, Object obj, Object filedVaule) {
        try {
            Class obj_class = Class.forName(classname);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedVaule);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
        } catch (SecurityException e5) {
            e5.printStackTrace();
        }
    }

    public static Object getFieldOjbect(String class_name, Object obj, String filedName) {
        try {
            Class obj_class = Class.forName(class_name);
            Field field = obj_class.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
            return null;
        } catch (SecurityException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(String className, Object obj, String fieldName) {
        try {
            Class<?> clazz = Class.forName(className);
            return getFieldValue(clazz, obj, fieldName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static boolean setFieldValue(Class<?> clazz, Object obj, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return false;
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public static boolean setFieldValue(String className, Object obj, String fieldName, Object value) {
        try {
            Class<?> clazz = Class.forName(className);
            setFieldValue(clazz, obj, fieldName, value);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static Object newInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (InstantiationException e3) {
            e3.printStackTrace();
            return null;
        }
    }


    // ===================== Start ========================
    private static HashMap<String, Field> sCacheFiled = new HashMap<>();

    private static HashMap<String, Method> sCacheMethod = new HashMap<>();

    private static Method sGetDeclaredField = null;

    private static Method sGetDeclaredMethod = null;

    static {
        try {
            sGetDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            sGetDeclaredMethod.setAccessible(true);
        } catch (Throwable t) {
        }
        try {
            sGetDeclaredField = Class.class.getDeclaredMethod("getDeclaredField", java.lang.String.class);
            sGetDeclaredField.setAccessible(true);
        } catch (Throwable t) {
        }
    }

    public static Field getClassField(Class clz, String fieldName) {
        Field declaredField = null;
        final String cacheKey = clz.getName() + "." + fieldName;
        if (sCacheFiled.containsKey(cacheKey)) {
            return sCacheFiled.get(cacheKey);
        }
        while (clz != null && clz != Object.class) {
            try {
                if (sGetDeclaredField != null) {
                    declaredField = (Field) sGetDeclaredField.invoke(clz, fieldName);
                } else {
                    declaredField = clz.getDeclaredField(fieldName);
                }
                declaredField.setAccessible(true);
            } catch (Throwable ignore) {
            }
            if (declaredField != null) {
                sCacheFiled.put(cacheKey, declaredField);
                return declaredField;
            }
            clz = clz.getSuperclass();
        }
        sCacheFiled.put(cacheKey, null);
        return null;
    }

    public static Method getClassMethod(Class clz, String methodName) {
        return getClassMethod(clz, methodName, (Class[]) null);
    }

    public static Method getClassMethod(Class clz, String methodName, Class<?>... clzs) {
        if (clz == null || methodName == null) {
            return null;
        }
        Method declaredMethod = null;
        final String cacheKey = clz.getName() + "." + methodName;
        if (sCacheMethod.containsKey(cacheKey)) {
            return sCacheMethod.get(cacheKey);
        }
        while (clz != null && clz != Object.class) {
            try {
                if (sGetDeclaredMethod != null) {
                    declaredMethod = (Method) sGetDeclaredMethod.invoke(clz, methodName, clzs);
                } else {
                    declaredMethod = (Method) clz.getDeclaredMethod(methodName, clzs);
                }
                declaredMethod.setAccessible(true);
            } catch (Throwable ignore) {
            }
            if (declaredMethod != null) {
                sCacheMethod.put(cacheKey, declaredMethod);
                return declaredMethod;
            }
            clz = clz.getSuperclass();
        }
        sCacheMethod.put(cacheKey, null);
        return null;
    }
    // ===================== End =========================
}