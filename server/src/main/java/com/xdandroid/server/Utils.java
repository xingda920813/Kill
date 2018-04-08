package com.xdandroid.server;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import dalvik.system.*;

interface Utils {

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
        final Field dexElementsField = findField(originalDexPathList, "dexElements");
        final Object[] dexElements = (Object[]) dexElementsField.get(originalDexPathList);
        final Field nativeLibraryDirectoriesField = findField(originalDexPathList, "nativeLibraryDirectories");
        final List<File> nativeLibraryDirectories = (List<File>) nativeLibraryDirectoriesField.get(originalDexPathList);
        final StringBuilder dexPathBuilder = new StringBuilder();
        final Field dexFileField = findField(dexElements.getClass().getComponentType(), "dexFile");
        boolean isFirstItem = true;
        for (Object dexElement : dexElements) {
            final DexFile dexFile = (DexFile) dexFileField.get(dexElement);
            if (dexFile == null) continue;
            if (isFirstItem) isFirstItem = false; else dexPathBuilder.append(File.pathSeparator);
            dexPathBuilder.append(dexFile.getName());
        }
        final String dexPath = dexPathBuilder.toString();
        final StringBuilder libraryPathBuilder = new StringBuilder();
        isFirstItem = true;
        for (File libDir : nativeLibraryDirectories) {
            if (libDir == null) continue;
            if (isFirstItem) isFirstItem = false; else libraryPathBuilder.append(File.pathSeparator);
            libraryPathBuilder.append(libDir.getAbsolutePath());
        }
        final String libraryPath = libraryPathBuilder.toString();
        final Constructor<?> dexPathListConstructor = findConstructor(originalDexPathList, ClassLoader.class, String.class, String.class, File.class);
        return dexPathListConstructor.newInstance(newDefiningContext, dexPath, libraryPath, null);
    }
}
