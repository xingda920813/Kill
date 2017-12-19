package com.xdandroid.server;

import android.app.*;
import android.os.*;

import java.lang.reflect.*;

import dalvik.system.*;

/**
 * coreApp="true"
 * android:sharedUserId="android.uid.system"
 * android:process="system"
 * android:theme="@android:style/Theme.NoDisplay"
 */
abstract class BaseActivity extends Activity implements Utils {

    static Field sDexPathListField, sElementsField;
    static {
        try {
            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            for (Field f : BaseDexClassLoader.class.getDeclaredFields()) {
                if (dexPathListClass.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    sDexPathListField = f;
                    break;
                }
            }
            Class<?> elementsClass = Class.forName("[Ldalvik.system.DexPathList$Element;");
            for (Field f : dexPathListClass.getDeclaredFields()) {
                if (elementsClass.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    sElementsField = f;
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(() -> {
            try {
                for (Thread t : Thread.getAllStackTraces().keySet()) {
                    if ("ActivityManager".equals(t.getName())) {
                        ClassLoader srvCL = t.getContextClassLoader();
                        sElementsField.set(sDexPathListField.get(srvCL), combineArray(sElementsField.get(sDexPathListField.get(getClassLoader())), sElementsField.get(sDexPathListField.get(srvCL))));
                        srvCL.loadClass("com.xdandroid.server.Hack").getMethod("hack", Object.class).invoke(null, getToken());
                        break;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }).start();
        finish();
    }

    abstract Object getToken();
}
