package com.xdandroid.lib;

import android.os.*;
import android.text.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.*;

public interface Utils {

    HashSet<String> WHITE_LIST_APPS = getWhiteListApps();

    static HashSet<String> getWhiteListApps() {
        HashSet<String> set = new HashSet<>(Arrays.asList(
                "com.github.dawndiy.bifrostv",
                "com.maxmpz.audioplayer",
                "com.maxmpz.audioplayer.unlock",
                "com.topjohnwu.magisk",
                "com.xdandroid.kill",
                "com.xdandroid.server",
                "me.piebridge.brevent",

                "com.tencent.mm",

                "tv.danmaku.bili",
                "com.netease.cloudmusic"
        ));
        String prop = SystemProperties.get("persist.lib.whiteListApps");
        if (!TextUtils.isEmpty(prop)) Collections.addAll(set, prop.split(Pattern.quote(";")));
        return set;
    }

    ArrayList<String> WHITE_LIST_APP_NAME_SLICES = getWhiteListAppNameSlices();

    static ArrayList<String> getWhiteListAppNameSlices() {
        ArrayList<String> l = new ArrayList<>(Arrays.asList(
                "chrome",
                "com.android.",
                "google",
                "vending"
        ));
        String prop = SystemProperties.get("persist.lib.whiteListAppNameSlices");
        if (!TextUtils.isEmpty(prop)) Collections.addAll(l, prop.split(Pattern.quote(";")));
        return l;
    }

    ArrayList<String> BLACK_LIST_OPS = getBlackListOps();

    static ArrayList<String> getBlackListOps() {
        ArrayList<String> l = new ArrayList<>(Arrays.asList(
                "WAKE_LOCK",
                "RUN_IN_BACKGROUND",
                "RUN_ANY_IN_BACKGROUND",
                "BOOT_COMPLETED",
                "WRITE_SETTINGS",
                "SYSTEM_ALERT_WINDOW"
        ));
        String prop = SystemProperties.get("persist.lib.blackListOps");
        if (!TextUtils.isEmpty(prop)) Collections.addAll(l, prop.split(Pattern.quote(";")));
        return l;
    }

    HashSet<String> WHITE_LIST_OPS_FOR_WHITE_LIST_APPS = getWhiteListOpsForWhiteListApps();

    static HashSet<String> getWhiteListOpsForWhiteListApps() {
        HashSet<String> set = new HashSet<>(Arrays.asList(
                "RUN_IN_BACKGROUND",
                "RUN_ANY_IN_BACKGROUND",
                "BOOT_COMPLETED"
        ));
        String prop = SystemProperties.get("persist.lib.whiteListOpsForWhiteListApps");
        if (!TextUtils.isEmpty(prop)) Collections.addAll(set, prop.split(Pattern.quote(";")));
        return set;
    }

    @SuppressWarnings("unchecked")
    static <E extends Throwable, R extends RuntimeException> R asUnchecked(Throwable t) throws E {
        throw (E) t;
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
        for (int i = 0; i < oriFirst.length + newLast.length; i++)
            if (i < oriFirst.length) ts[i] = oriFirst[i];
            else ts[i] = newLast[i - oriFirst.length];
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
