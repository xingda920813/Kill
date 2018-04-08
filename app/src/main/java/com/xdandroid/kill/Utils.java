package com.xdandroid.kill;

import android.*;
import android.app.*;
import android.os.*;

import java.io.*;
import java.util.*;

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
            "OP_BOOT_COMPLETED",
            "WRITE_SETTINGS",
            "SYSTEM_ALERT_WINDOW"
    };

    List<String> WHITE_LIST_OPS_FOR_WHITE_LIST_APPS = Arrays.asList(
            "RUN_IN_BACKGROUND",
            "OP_BOOT_COMPLETED"
    );

    int OP_BOOT_COMPLETED = SystemProperties.getInt("ro.lineage.build.version.plat.sdk", 0) < 6 ? 0 : Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 72 : 66;

    static void setPermissive() {
        try { Runtime.getRuntime().exec("su -c setenforce 0"); } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings({"TypeParameterHidesVisibleType", "unchecked"})
    default <E extends Throwable, R extends RuntimeException> R asUnchecked(Throwable t) throws E {
        throw (E) t;
    }
}
