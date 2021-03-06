package com.xdandroid.server;

import android.app.*;
import android.os.*;
import android.widget.*;

import com.xdandroid.lib.*;

import java.lang.reflect.*;

import dalvik.system.*;

/**
 * coreApp="true"
 * android:sharedUserId="android.uid.system"
 * android:process="system"
 * android:theme="@android:style/Theme.NoDisplay"
 */
abstract class BaseActivity extends Activity {

    static ClassLoader sServicesClassLoader;
    static {
        try {
            Field dexPathListField = null;
            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            for (Field f : BaseDexClassLoader.class.getDeclaredFields()) {
                if (dexPathListClass.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    dexPathListField = f;
                    break;
                }
            }
            Field elementsField = null;
            Class<? extends Object[]> elementsClass = (Class<? extends Object[]>) Class.forName("[Ldalvik.system.DexPathList$Element;");
            for (Field f : dexPathListClass.getDeclaredFields()) {
                if (elementsClass.isAssignableFrom(f.getType())) {
                    f.setAccessible(true);
                    elementsField = f;
                    break;
                }
            }
            assert dexPathListField != null;
            assert elementsField != null;
            for (Thread t : Thread.getAllStackTraces().keySet()) {
                if ("ActivityManager".equals(t.getName())) {
                    Object srvDexPathList = dexPathListField.get(sServicesClassLoader = t.getContextClassLoader());
                    elementsField.set(
                            srvDexPathList,
                            Utils.combineArray(
                                    (Object[]) elementsField.get(srvDexPathList),
                                    (Object[]) elementsField.get(ServerUtils.recreateDexPathList(dexPathListField.get(BaseActivity.class.getClassLoader()), sServicesClassLoader))
                            )
                    );
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
        Toast.makeText(this, getToken().toString(), Toast.LENGTH_SHORT).show();
        invokeHack();
        finish();
    }

    void invokeHack() {
        try {
            sServicesClassLoader
                    .loadClass("com.xdandroid.server.Hack")
                    .getMethod("hack", Object.class)
                    .invoke(null, getToken());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    abstract Object getToken();
}
