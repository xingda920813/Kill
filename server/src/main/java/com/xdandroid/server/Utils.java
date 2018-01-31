package com.xdandroid.server;

import android.content.pm.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

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

    Field[] sAppInfoField = new Field[1];
    Field[] sServicesField = new Field[1];
    Field[] sServiceInfoField = new Field[1];

    static ApplicationInfo fromPkgToAppInfo(Object pkg) {
        try {
            if (sAppInfoField[0] == null) for (Field f : pkg.getClass().getDeclaredFields())
                if (ApplicationInfo.class.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    sAppInfoField[0] = f;
                    break;
                }
            return (ApplicationInfo) sAppInfoField[0].get(pkg);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApplicationInfo();
        }
    }

    static Stream<ServiceInfo> fromPkgToSrvInfo(Object pkg) {
        try {
            if (sServicesField[0] == null) sServicesField[0] = pkg.getClass().getField("services");
            return ((ArrayList<?>) sServicesField[0].get(pkg)).stream().map(Utils::fromSrvToSrvInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    static ServiceInfo fromSrvToSrvInfo(Object srv) {
        try {
            if (sServiceInfoField[0] == null) for (Field f : srv.getClass().getFields())
                if (ServiceInfo.class.isAssignableFrom(f.getType())) {
                    sServiceInfoField[0] = f;
                    break;
                }
            return (ServiceInfo) sServiceInfoField[0].get(srv);
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceInfo();
        }
    }
}
