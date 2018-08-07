/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xdandroid.lib;

import android.*;
import android.annotation.*;
import android.app.usage.*;

import java.util.*;

/**
 * API for interacting with "application operation" tracking.
 *
 * <p>This API is not generally intended for third party application developers; most
 * features are only available to system applications.
 */
public interface LocalAppOpsManager {
    /**
     * <p>App ops allows callers to:</p>
     *
     * <ul>
     * <li> Note when operations are happening, and find out if they are allowed for the current
     * caller.</li>
     * <li> Disallow specific apps from doing specific operations.</li>
     * <li> Collect all of the current information about operations that have been executed or
     * are not being allowed.</li>
     * <li> Monitor for changes in whether an operation is allowed.</li>
     * </ul>
     *
     * <p>Each operation is identified by a single integer; these integers are a fixed set of
     * operations, enumerated by the OP_* constants.
     *
     * <p></p>When checking operations, the result is a "mode" integer indicating the current
     * setting for the operation under that caller: MODE_ALLOWED, MODE_IGNORED (don't execute
     * the operation but fake its behavior enough so that the caller doesn't crash),
     * MODE_ERRORED (throw a SecurityException back to the caller; the normal operation calls
     * will do this for you).
     */

    /**
     * Result from {@link #checkOp}, {@link #noteOp}, {@link #startOp}: the given caller is
     * allowed to perform the given operation.
     */
    int MODE_ALLOWED = 0;

    /**
     * Result from {@link #checkOp}, {@link #noteOp}, {@link #startOp}: the given caller is
     * not allowed to perform the given operation, and this attempt should
     * <em>silently fail</em> (it should not cause the app to crash).
     */
    int MODE_IGNORED = 1;

    /**
     * Result from {@link #checkOpNoThrow}, {@link #noteOpNoThrow}, {@link #startOpNoThrow}: the
     * given caller is not allowed to perform the given operation, and this attempt should
     * cause it to have a fatal error, typically a {@link SecurityException}.
     */
    int MODE_ERRORED = 2;

    /**
     * Result from {@link #checkOp}, {@link #noteOp}, {@link #startOp}: the given caller should
     * use its default security check.  This mode is not normally used; it should only be used
     * with appop permissions, and callers must explicitly check for it and deal with it.
     */
    int MODE_DEFAULT = 3;

    /**
     * Special mode that means "allow only when app is in foreground."  This is <b>not</b>
     * returned from {@link #checkOp}, {@link #noteOp}, {@link #startOp}; rather, when this
     * mode is set, these functions will return {@link #MODE_ALLOWED} when the app being
     * checked is currently in the foreground, otherwise {@link #MODE_IGNORED}.
     * @hide
     */
    int MODE_FOREGROUND = 4;

    // when adding one of these:
    //  - increment _NUM_OP
    //  - define an OPSTR_* constant (marked as @SystemApi)
    //  - add rows to sOpToSwitch, sOpToString, sOpNames, sOpToPerms, sOpDefault
    //  - add descriptive strings to Settings/res/values/arrays.xml
    //  - add the op to the appropriate template in AppOpsState.OpsTemplate (settings app)

    /** @hide No operation specified. */
    int OP_NONE = -1;
    /** @hide Access to coarse location information. */
    int OP_COARSE_LOCATION = 0;
    /** @hide Access to fine location information. */
    int OP_FINE_LOCATION = 1;
    /** @hide Causing GPS to run. */
    int OP_GPS = 2;
    /** @hide */
    int OP_VIBRATE = 3;
    /** @hide */
    int OP_READ_CONTACTS = 4;
    /** @hide */
    int OP_WRITE_CONTACTS = 5;
    /** @hide */
    int OP_READ_CALL_LOG = 6;
    /** @hide */
    int OP_WRITE_CALL_LOG = 7;
    /** @hide */
    int OP_READ_CALENDAR = 8;
    /** @hide */
    int OP_WRITE_CALENDAR = 9;
    /** @hide */
    int OP_WIFI_SCAN = 10;
    /** @hide */
    int OP_POST_NOTIFICATION = 11;
    /** @hide */
    int OP_NEIGHBORING_CELLS = 12;
    /** @hide */
    int OP_CALL_PHONE = 13;
    /** @hide */
    int OP_READ_SMS = 14;
    /** @hide */
    int OP_WRITE_SMS = 15;
    /** @hide */
    int OP_RECEIVE_SMS = 16;
    /** @hide */
    int OP_RECEIVE_EMERGECY_SMS = 17;
    /** @hide */
    int OP_RECEIVE_MMS = 18;
    /** @hide */
    int OP_RECEIVE_WAP_PUSH = 19;
    /** @hide */
    int OP_SEND_SMS = 20;
    /** @hide */
    int OP_READ_ICC_SMS = 21;
    /** @hide */
    int OP_WRITE_ICC_SMS = 22;
    /** @hide */
    int OP_WRITE_SETTINGS = 23;
    /** @hide Required to draw on top of other apps. */
    int OP_SYSTEM_ALERT_WINDOW = 24;
    /** @hide */
    int OP_ACCESS_NOTIFICATIONS = 25;
    /** @hide */
    int OP_CAMERA = 26;
    /** @hide */
    int OP_RECORD_AUDIO = 27;
    /** @hide */
    int OP_PLAY_AUDIO = 28;
    /** @hide */
    int OP_READ_CLIPBOARD = 29;
    /** @hide */
    int OP_WRITE_CLIPBOARD = 30;
    /** @hide */
    int OP_TAKE_MEDIA_BUTTONS = 31;
    /** @hide */
    int OP_TAKE_AUDIO_FOCUS = 32;
    /** @hide */
    int OP_AUDIO_MASTER_VOLUME = 33;
    /** @hide */
    int OP_AUDIO_VOICE_VOLUME = 34;
    /** @hide */
    int OP_AUDIO_RING_VOLUME = 35;
    /** @hide */
    int OP_AUDIO_MEDIA_VOLUME = 36;
    /** @hide */
    int OP_AUDIO_ALARM_VOLUME = 37;
    /** @hide */
    int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    /** @hide */
    int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    /** @hide */
    int OP_WAKE_LOCK = 40;
    /** @hide Continually monitoring location data. */
    int OP_MONITOR_LOCATION = 41;
    /** @hide Continually monitoring location data with a relatively high power request. */
    int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    /** @hide Retrieve current usage stats via {@link UsageStatsManager}. */
    int OP_GET_USAGE_STATS = 43;
    /** @hide */
    int OP_MUTE_MICROPHONE = 44;
    /** @hide */
    int OP_TOAST_WINDOW = 45;
    /** @hide Capture the device's display contents and/or audio */
    int OP_PROJECT_MEDIA = 46;
    /** @hide Activate a VPN connection without user intervention. */
    int OP_ACTIVATE_VPN = 47;
    /** @hide Access the WallpaperManagerAPI to write wallpapers. */
    int OP_WRITE_WALLPAPER = 48;
    /** @hide Received the assist structure from an app. */
    int OP_ASSIST_STRUCTURE = 49;
    /** @hide Received a screenshot from assist. */
    int OP_ASSIST_SCREENSHOT = 50;
    /** @hide Read the phone state. */
    int OP_READ_PHONE_STATE = 51;
    /** @hide Add voicemail messages to the voicemail content provider. */
    int OP_ADD_VOICEMAIL = 52;
    /** @hide Access APIs for SIP calling over VOIP or WiFi. */
    int OP_USE_SIP = 53;
    /** @hide Intercept outgoing calls. */
    int OP_PROCESS_OUTGOING_CALLS = 54;
    /** @hide User the fingerprint API. */
    int OP_USE_FINGERPRINT = 55;
    /** @hide Access to body sensors such as heart rate, etc. */
    int OP_BODY_SENSORS = 56;
    /** @hide Read previously received cell broadcast messages. */
    int OP_READ_CELL_BROADCASTS = 57;
    /** @hide Inject mock location into the system. */
    int OP_MOCK_LOCATION = 58;
    /** @hide Read external storage. */
    int OP_READ_EXTERNAL_STORAGE = 59;
    /** @hide Write external storage. */
    int OP_WRITE_EXTERNAL_STORAGE = 60;
    /** @hide Turned on the screen. */
    int OP_TURN_SCREEN_ON = 61;
    /** @hide Get device accounts. */
    int OP_GET_ACCOUNTS = 62;
    /** @hide Control whether an application is allowed to run in the background. */
    int OP_RUN_IN_BACKGROUND = 63;
    /** @hide */
    int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    /** @hide Read the phone number. */
    int OP_READ_PHONE_NUMBERS = 65;
    /** @hide Request package installs through package installer */
    int OP_REQUEST_INSTALL_PACKAGES = 66;
    /** @hide Enter picture-in-picture. */
    int OP_PICTURE_IN_PICTURE = 67;
    /** @hide Instant app start foreground service. */
    int OP_INSTANT_APP_START_FOREGROUND = 68;
    /** @hide Answer incoming phone calls */
    int OP_ANSWER_PHONE_CALLS = 69;
    /** @hide Run jobs when in background */
    int OP_RUN_ANY_IN_BACKGROUND = 70;
    /** @hide Change Wi-Fi connectivity state */
    int OP_CHANGE_WIFI_STATE = 71;
    /** @hide Request package deletion through package installer */
    int OP_REQUEST_DELETE_PACKAGES = 72;
    /** @hide Bind an accessibility service. */
    int OP_BIND_ACCESSIBILITY_SERVICE = 73;
    /** @hide Continue handover of a call from another app */
    int OP_ACCEPT_HANDOVER = 74;
    /** @hide Create and Manage IPsec Tunnels */
    int OP_MANAGE_IPSEC_TUNNELS = 75;
    /** @hide Any app start foreground service. */
    int OP_START_FOREGROUND = 76;
    /** @hide */
    int OP_BLUETOOTH_SCAN = 77;
    /** @hide */
    int _NUM_OP = 78;

    // Warning: If an permission is added here it also has to be added to
    // com.android.packageinstaller.permission.utils.EventLogger
    int[] RUNTIME_AND_APPOP_PERMISSIONS_OPS = {
            // RUNTIME PERMISSIONS
            // Contacts
            OP_READ_CONTACTS,
            OP_WRITE_CONTACTS,
            OP_GET_ACCOUNTS,
            // Calendar
            OP_READ_CALENDAR,
            OP_WRITE_CALENDAR,
            // SMS
            OP_SEND_SMS,
            OP_RECEIVE_SMS,
            OP_READ_SMS,
            OP_RECEIVE_WAP_PUSH,
            OP_RECEIVE_MMS,
            OP_READ_CELL_BROADCASTS,
            // Storage
            OP_READ_EXTERNAL_STORAGE,
            OP_WRITE_EXTERNAL_STORAGE,
            // Location
            OP_COARSE_LOCATION,
            OP_FINE_LOCATION,
            // Phone
            OP_READ_PHONE_STATE,
            OP_READ_PHONE_NUMBERS,
            OP_CALL_PHONE,
            OP_READ_CALL_LOG,
            OP_WRITE_CALL_LOG,
            OP_ADD_VOICEMAIL,
            OP_USE_SIP,
            OP_PROCESS_OUTGOING_CALLS,
            OP_ANSWER_PHONE_CALLS,
            OP_ACCEPT_HANDOVER,
            // Microphone
            OP_RECORD_AUDIO,
            // Camera
            OP_CAMERA,
            // Body sensors
            OP_BODY_SENSORS,

            // APPOP PERMISSIONS
            OP_ACCESS_NOTIFICATIONS,
            OP_SYSTEM_ALERT_WINDOW,
            OP_WRITE_SETTINGS,
            OP_REQUEST_INSTALL_PACKAGES,
            OP_START_FOREGROUND,
    };

    /**
     * This provides a simple name for each operation to be used
     * in debug output.
     */
    String[] sOpNames = new String[] {
            "COARSE_LOCATION",
            "FINE_LOCATION",
            "GPS",
            "VIBRATE",
            "READ_CONTACTS",
            "WRITE_CONTACTS",
            "READ_CALL_LOG",
            "WRITE_CALL_LOG",
            "READ_CALENDAR",
            "WRITE_CALENDAR",
            "WIFI_SCAN",
            "POST_NOTIFICATION",
            "NEIGHBORING_CELLS",
            "CALL_PHONE",
            "READ_SMS",
            "WRITE_SMS",
            "RECEIVE_SMS",
            "RECEIVE_EMERGECY_SMS",
            "RECEIVE_MMS",
            "RECEIVE_WAP_PUSH",
            "SEND_SMS",
            "READ_ICC_SMS",
            "WRITE_ICC_SMS",
            "WRITE_SETTINGS",
            "SYSTEM_ALERT_WINDOW",
            "ACCESS_NOTIFICATIONS",
            "CAMERA",
            "RECORD_AUDIO",
            "PLAY_AUDIO",
            "READ_CLIPBOARD",
            "WRITE_CLIPBOARD",
            "TAKE_MEDIA_BUTTONS",
            "TAKE_AUDIO_FOCUS",
            "AUDIO_MASTER_VOLUME",
            "AUDIO_VOICE_VOLUME",
            "AUDIO_RING_VOLUME",
            "AUDIO_MEDIA_VOLUME",
            "AUDIO_ALARM_VOLUME",
            "AUDIO_NOTIFICATION_VOLUME",
            "AUDIO_BLUETOOTH_VOLUME",
            "WAKE_LOCK",
            "MONITOR_LOCATION",
            "MONITOR_HIGH_POWER_LOCATION",
            "GET_USAGE_STATS",
            "MUTE_MICROPHONE",
            "TOAST_WINDOW",
            "PROJECT_MEDIA",
            "ACTIVATE_VPN",
            "WRITE_WALLPAPER",
            "ASSIST_STRUCTURE",
            "ASSIST_SCREENSHOT",
            "OP_READ_PHONE_STATE",
            "ADD_VOICEMAIL",
            "USE_SIP",
            "PROCESS_OUTGOING_CALLS",
            "USE_FINGERPRINT",
            "BODY_SENSORS",
            "READ_CELL_BROADCASTS",
            "MOCK_LOCATION",
            "READ_EXTERNAL_STORAGE",
            "WRITE_EXTERNAL_STORAGE",
            "TURN_ON_SCREEN",
            "GET_ACCOUNTS",
            "RUN_IN_BACKGROUND",
            "AUDIO_ACCESSIBILITY_VOLUME",
            "READ_PHONE_NUMBERS",
            "REQUEST_INSTALL_PACKAGES",
            "PICTURE_IN_PICTURE",
            "INSTANT_APP_START_FOREGROUND",
            "ANSWER_PHONE_CALLS",
            "RUN_ANY_IN_BACKGROUND",
            "CHANGE_WIFI_STATE",
            "REQUEST_DELETE_PACKAGES",
            "BIND_ACCESSIBILITY_SERVICE",
            "ACCEPT_HANDOVER",
            "MANAGE_IPSEC_TUNNELS",
            "START_FOREGROUND",
            "BLUETOOTH_SCAN",
    };

    /**
     * This optionally maps a permission to an operation.  If there
     * is no permission associated with an operation, it is null.
     */
    @SuppressLint("InlinedApi") String[] sOpPerms = new String[] {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            null,
            android.Manifest.permission.VIBRATE,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.WRITE_CALL_LOG,
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR,
            android.Manifest.permission.ACCESS_WIFI_STATE,
            null, // no permission required for notifications
            null, // neighboring cells shares the coarse location perm
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.READ_SMS,
            null, // no permission required for writing sms
            android.Manifest.permission.RECEIVE_SMS,
            null,
            android.Manifest.permission.RECEIVE_MMS,
            android.Manifest.permission.RECEIVE_WAP_PUSH,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_SMS,
            null, // no permission required for writing icc sms
            android.Manifest.permission.WRITE_SETTINGS,
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            null,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
            null, // no permission for playing audio
            null, // no permission for reading clipboard
            null, // no permission for writing clipboard
            null, // no permission for taking media buttons
            null, // no permission for taking audio focus
            null, // no permission for changing master volume
            null, // no permission for changing voice volume
            null, // no permission for changing ring volume
            null, // no permission for changing media volume
            null, // no permission for changing alarm volume
            null, // no permission for changing notification volume
            null, // no permission for changing bluetooth volume
            android.Manifest.permission.WAKE_LOCK,
            null, // no permission for generic location monitoring
            null, // no permission for high power location monitoring
            android.Manifest.permission.PACKAGE_USAGE_STATS,
            null, // no permission for muting/unmuting microphone
            null, // no permission for displaying toasts
            null, // no permission for projecting media
            null, // no permission for activating vpn
            null, // no permission for supporting wallpaper
            null, // no permission for receiving assist structure
            null, // no permission for receiving assist screenshot
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.USE_FINGERPRINT,
            Manifest.permission.BODY_SENSORS,
            null,
            null,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            null, // no permission for turning the screen on
            Manifest.permission.GET_ACCOUNTS,
            null, // no permission for running in background
            null, // no permission for changing accessibility volume
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.REQUEST_INSTALL_PACKAGES,
            null, // no permission for entering picture-in-picture on hide
            Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
            Manifest.permission.ANSWER_PHONE_CALLS,
            null, // no permission for OP_RUN_ANY_IN_BACKGROUND
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.REQUEST_DELETE_PACKAGES,
            Manifest.permission.BIND_ACCESSIBILITY_SERVICE,
            null,
            null, // no permission for OP_MANAGE_IPSEC_TUNNELS
            null,
            null, // no permission for OP_BLUETOOTH_SCAN
    };

    /**
     * Mapping from a permission to the corresponding app op.
     */
    HashMap<String, Integer> sPermToOp = new HashMap<String, Integer>() {{
        for (int op : RUNTIME_AND_APPOP_PERMISSIONS_OPS) {
            if (sOpPerms[op] != null) {
                put(sOpPerms[op], op);
            }
        }
    }};

    /**
     * Retrieve a non-localized name for the operation, for debugging output.
     * @hide
     */
    static String opToName(int op) {
        if (op == OP_NONE) return "NONE";
        return op < sOpNames.length ? sOpNames[op] : ("Unknown(" + op + ")");
    }

    /**
     * Retrieve the app op code for a permission, or null if there is not one.
     * This API is intended to be used for mapping runtime or appop permissions
     * to the corresponding app op.
     * @hide
     */
    static int permissionToOpCode(String permission) {
        Integer boxedOpCode = sPermToOp.get(permission);
        return boxedOpCode != null ? boxedOpCode : OP_NONE;
    }
}
