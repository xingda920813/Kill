package com.xdandroid.kill;

import android.app.*;
import android.os.*;
import android.widget.*;

import com.xdandroid.lib.*;

import java.io.*;
import java.lang.*;
import java.lang.Process;
import java.nio.charset.*;
import java.util.*;

/**
 * uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
 * android:theme="@android:style/Theme.NoDisplay"
 */
public class RootRevokeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "RootRevoke", Toast.LENGTH_SHORT).show();
        List<String> revokeOps = GenOpsActivity.getRevokeOps(this, false);
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su");
            OutputStream out = proc.getOutputStream();
            revokeOps.forEach(op -> {
                try {
                    out.write(op.getBytes(StandardCharsets.UTF_8));
                    out.flush();
                } catch (IOException e) {
                    throw Utils.asUnchecked(e);
                }
            });
            out.write("exit\n\n".getBytes(StandardCharsets.UTF_8));
            out.flush();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            throw Utils.asUnchecked(e);
        } finally {
            if (proc != null) proc.destroy();
        }
        finish();
    }
}
