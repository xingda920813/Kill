package com.xdandroid.lib;

import android.*;
import android.app.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public interface Utils {

    List<String> WHITE_LIST_APPS = Arrays.asList(
            "com.github.shadowsocks",
            "com.maxmpz.audioplayer",
            "com.maxmpz.audioplayer.unlock",
            "com.xdandroid.kill",
            "com.xdandroid.server",
            "me.piebridge.brevent",

            "com.alibaba.android.rimet",
            "com.bearyinnovative.horcrux",
            "com.tencent.mm",
            "com.tencent.tim",

            "com.samsung.accessory",
            "com.samsung.android.app.watchmanager",
            "com.samsung.android.gearfit2plugin",
            "com.samsung.accessory.safiletransfer"
    );

    List<?> WHITE_LIST_PERMISSIONS = Arrays.asList(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            AppOpsManager.OP_READ_EXTERNAL_STORAGE,
            "READ_EXTERNAL_STORAGE",

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            AppOpsManager.OP_WRITE_EXTERNAL_STORAGE,
            "WRITE_EXTERNAL_STORAGE"
    );

    String[] BLACK_LIST_OPS = {
            "WIFI_SCAN",
            "WAKE_LOCK",
            "RUN_IN_BACKGROUND",
            "RUN_ANY_IN_BACKGROUND",
            "OP_BOOT_COMPLETED",
            "WRITE_SETTINGS",
            "SYSTEM_ALERT_WINDOW",
            "BLUETOOTH_SCAN"
    };

    List<String> WHITE_LIST_OPS_FOR_WHITE_LIST_APPS = Arrays.asList(
            "RUN_IN_BACKGROUND",
            "RUN_ANY_IN_BACKGROUND",
            "OP_BOOT_COMPLETED"
    );

    List<String> NON_DEBUGGABLE_APPS = Arrays.asList(
            "chrome",
            "vending",
            "google"
    );

    @SuppressWarnings("unchecked")
    static <E extends Throwable, R extends RuntimeException> R asUnchecked(Throwable t) throws E {
        throw (E) t;
    }

    static void copyToFileOrThrow(InputStream in, File dest) throws IOException {
        if (dest.exists()) dest.delete();
        try (FileOutputStream out = new FileOutputStream(dest)) {
            byte[] buf = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buf)) >= 0) out.write(buf, 0, bytesRead);
            out.flush();
            try { out.getFD().sync(); } catch (IOException ignored) { }
        }
    }

    static <T> int indexOf(T[] array, T value) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) if (Objects.equals(array[i], value)) return i;
        return -1;
    }

    static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) != -1;
    }

    static <T> boolean containsAll(T[] array, T[] check) {
        if (check == null) return true;
        for (T checkItem : check) if (!contains(array, checkItem)) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    static <T> T[] combineArray(T[] oriFirst, T[] newLast) {
        if (containsAll(oriFirst, newLast)) return oriFirst;
        T[] ts = (T[]) Array.newInstance(oriFirst.getClass().getComponentType(), oriFirst.length + newLast.length);
        for (int i = 0; i < oriFirst.length + newLast.length; i++) if (i < oriFirst.length) ts[i] = oriFirst[i]; else ts[i] = newLast[i - oriFirst.length];
        return ts;
    }

    static Field findField(Object o, String name) throws NoSuchFieldException {
        for (Class<?> clazz = o.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException ignored) { }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + o.getClass());
    }

    static Field findField(Class<?> originClass, String name) throws NoSuchFieldException {
        for (Class<?> clazz = originClass; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException ignored) { }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + originClass);
    }

    static Constructor<?> findConstructor(Object instance, Class<?>... parameterTypes) throws NoSuchMethodException {
        for (Class<?> clazz = instance.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Constructor<?> cons = clazz.getDeclaredConstructor(parameterTypes);
                cons.setAccessible(true);
                return cons;
            } catch (NoSuchMethodException ignored) { }
        }
        throw new NoSuchMethodException("Constructor" + " with parameters " + Arrays.asList(parameterTypes) + " not found in " + instance.getClass());
    }
}
