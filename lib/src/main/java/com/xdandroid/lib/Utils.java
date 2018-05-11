package com.xdandroid.lib;

import android.*;
import android.app.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import dalvik.system.*;

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
            "com.alibaba.alimei"
    );

    List<String> WHITE_LIST_PERMISSIONS = Arrays.asList(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            AppOpsManager.OPSTR_READ_EXTERNAL_STORAGE,
            "READ_EXTERNAL_STORAGE",

            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE,
            "WRITE_EXTERNAL_STORAGE"
    );

    String[] BLACK_LIST_OPS = {
            "WIFI_SCAN",
            "WAKE_LOCK",
            "RUN_IN_BACKGROUND",
            "RUN_ANY_IN_BACKGROUND",
            "OP_BOOT_COMPLETED",
            "WRITE_SETTINGS",
            "SYSTEM_ALERT_WINDOW"
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

    @SuppressWarnings("unchecked")
    static <T> T[] combineArray(T[] a, T[] b) {
        T[] ts = (T[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
        for (int i = 0; i < a.length + b.length; i++) if (i < a.length) ts[i] = a[i]; else ts[i] = b[i - a.length];
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

    @SuppressWarnings("unchecked")
    static Object recreateDexPathList(Object originalDexPathList, ClassLoader newDefiningContext) throws Exception {
        Field dexElementsField = Utils.findField(originalDexPathList, "dexElements");
        Object[] dexElements = (Object[]) dexElementsField.get(originalDexPathList);
        Field nativeLibraryDirectoriesField = Utils.findField(originalDexPathList, "nativeLibraryDirectories");
        List<File> nativeLibraryDirectories = (List<File>) nativeLibraryDirectoriesField.get(originalDexPathList);
        StringBuilder dexPathBuilder = new StringBuilder();
        Field dexFileField = Utils.findField(dexElements.getClass().getComponentType(), "dexFile");
        boolean isFirstItem = true;
        for (Object dexElement : dexElements) {
            DexFile dexFile = (DexFile) dexFileField.get(dexElement);
            if (dexFile == null) continue;
            if (isFirstItem) isFirstItem = false; else dexPathBuilder.append(File.pathSeparator);
            dexPathBuilder.append(dexFile.getName());
        }
        String dexPath = dexPathBuilder.toString();
        StringBuilder libraryPathBuilder = new StringBuilder();
        isFirstItem = true;
        for (File libDir : nativeLibraryDirectories) {
            if (libDir == null) continue;
            if (isFirstItem) isFirstItem = false; else libraryPathBuilder.append(File.pathSeparator);
            libraryPathBuilder.append(libDir.getAbsolutePath());
        }
        String libraryPath = libraryPathBuilder.toString();
        Constructor<?> dexPathListConstructor = Utils.findConstructor(originalDexPathList, ClassLoader.class, String.class, String.class, File.class);
        return dexPathListConstructor.newInstance(newDefiningContext, dexPath, libraryPath, null);
    }
}
