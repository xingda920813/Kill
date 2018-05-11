package com.xdandroid.lib;

import android.*;
import android.annotation.*;
import android.app.*;
import android.app.usage.*;
import android.os.*;

import java.util.*;

public interface LocalAppOpsManager {

    int MODE_ALLOWED = 0;
    int MODE_IGNORED = 1;
    int MODE_ERRORED = 2;
    int MODE_DEFAULT = 3;

    // when adding one of these:
    //  - increment _NUM_OP
    //  - add rows to sOpToSwitch, sOpToString, sOpNames, sOpToPerms, sOpDefault
    //  - add descriptive strings to Settings/res/values/arrays.xml
    //  - add the op to the appropriate template in AppOpsState.OpsTemplate (settings app)

    /**
     * @hide No operation specified.
     */
    int OP_NONE = -1;
    /**
     * @hide Access to coarse location information.
     */
    int OP_COARSE_LOCATION = 0;
    /**
     * @hide Access to fine location information.
     */
    int OP_FINE_LOCATION = 1;
    /**
     * @hide Causing GPS to run.
     */
    int OP_GPS = 2;
    /**
     * @hide
     */
    int OP_VIBRATE = 3;
    /**
     * @hide
     */
    int OP_READ_CONTACTS = 4;
    /**
     * @hide
     */
    int OP_WRITE_CONTACTS = 5;
    /**
     * @hide
     */
    int OP_READ_CALL_LOG = 6;
    /**
     * @hide
     */
    int OP_WRITE_CALL_LOG = 7;
    /**
     * @hide
     */
    int OP_READ_CALENDAR = 8;
    /**
     * @hide
     */
    int OP_WRITE_CALENDAR = 9;
    /**
     * @hide
     */
    int OP_WIFI_SCAN = 10;
    /**
     * @hide
     */
    int OP_POST_NOTIFICATION = 11;
    /**
     * @hide
     */
    int OP_NEIGHBORING_CELLS = 12;
    /**
     * @hide
     */
    int OP_CALL_PHONE = 13;
    /**
     * @hide
     */
    int OP_READ_SMS = 14;
    /**
     * @hide
     */
    int OP_WRITE_SMS = 15;
    /**
     * @hide
     */
    int OP_RECEIVE_SMS = 16;
    /**
     * @hide
     */
    int OP_RECEIVE_EMERGECY_SMS = 17;
    /**
     * @hide
     */
    int OP_RECEIVE_MMS = 18;
    /**
     * @hide
     */
    int OP_RECEIVE_WAP_PUSH = 19;
    /**
     * @hide
     */
    int OP_SEND_SMS = 20;
    /**
     * @hide
     */
    int OP_READ_ICC_SMS = 21;
    /**
     * @hide
     */
    int OP_WRITE_ICC_SMS = 22;
    /**
     * @hide
     */
    int OP_WRITE_SETTINGS = 23;
    /**
     * @hide
     */
    int OP_SYSTEM_ALERT_WINDOW = 24;
    /**
     * @hide
     */
    int OP_ACCESS_NOTIFICATIONS = 25;
    /**
     * @hide
     */
    int OP_CAMERA = 26;
    /**
     * @hide
     */
    int OP_RECORD_AUDIO = 27;
    /**
     * @hide
     */
    int OP_PLAY_AUDIO = 28;
    /**
     * @hide
     */
    int OP_READ_CLIPBOARD = 29;
    /**
     * @hide
     */
    int OP_WRITE_CLIPBOARD = 30;
    /**
     * @hide
     */
    int OP_TAKE_MEDIA_BUTTONS = 31;
    /**
     * @hide
     */
    int OP_TAKE_AUDIO_FOCUS = 32;
    /**
     * @hide
     */
    int OP_AUDIO_MASTER_VOLUME = 33;
    /**
     * @hide
     */
    int OP_AUDIO_VOICE_VOLUME = 34;
    /**
     * @hide
     */
    int OP_AUDIO_RING_VOLUME = 35;
    /**
     * @hide
     */
    int OP_AUDIO_MEDIA_VOLUME = 36;
    /**
     * @hide
     */
    int OP_AUDIO_ALARM_VOLUME = 37;
    /**
     * @hide
     */
    int OP_AUDIO_NOTIFICATION_VOLUME = 38;
    /**
     * @hide
     */
    int OP_AUDIO_BLUETOOTH_VOLUME = 39;
    /**
     * @hide
     */
    int OP_WAKE_LOCK = 40;
    /**
     * @hide Continually monitoring location data.
     */
    int OP_MONITOR_LOCATION = 41;
    /**
     * @hide Continually monitoring location data with a relatively high power request.
     */
    int OP_MONITOR_HIGH_POWER_LOCATION = 42;
    /**
     * @hide Retrieve current usage stats via {@link UsageStatsManager}.
     */
    int OP_GET_USAGE_STATS = 43;
    /**
     * @hide
     */
    int OP_MUTE_MICROPHONE = 44;
    /**
     * @hide
     */
    int OP_TOAST_WINDOW = 45;
    /**
     * @hide Capture the device's display contents and/or audio
     */
    int OP_PROJECT_MEDIA = 46;
    /**
     * @hide Activate a VPN connection without user intervention.
     */
    int OP_ACTIVATE_VPN = 47;
    /**
     * @hide Access the WallpaperManagerAPI to write wallpapers.
     */
    int OP_WRITE_WALLPAPER = 48;
    /**
     * @hide Received the assist structure from an app.
     */
    int OP_ASSIST_STRUCTURE = 49;
    /**
     * @hide Received a screenshot from assist.
     */
    int OP_ASSIST_SCREENSHOT = 50;
    /**
     * @hide Read the phone state.
     */
    int OP_READ_PHONE_STATE = 51;
    /**
     * @hide Add voicemail messages to the voicemail content provider.
     */
    int OP_ADD_VOICEMAIL = 52;
    /**
     * @hide Access APIs for SIP calling over VOIP or WiFi.
     */
    int OP_USE_SIP = 53;
    /**
     * @hide Intercept outgoing calls.
     */
    int OP_PROCESS_OUTGOING_CALLS = 54;
    /**
     * @hide User the fingerprint API.
     */
    int OP_USE_FINGERPRINT = 55;
    /**
     * @hide Access to body sensors such as heart rate, etc.
     */
    int OP_BODY_SENSORS = 56;
    /**
     * @hide Read previously received cell broadcast messages.
     */
    int OP_READ_CELL_BROADCASTS = 57;
    /**
     * @hide Inject mock location into the system.
     */
    int OP_MOCK_LOCATION = 58;
    /**
     * @hide Read external storage.
     */
    int OP_READ_EXTERNAL_STORAGE = 59;
    /**
     * @hide Write external storage.
     */
    int OP_WRITE_EXTERNAL_STORAGE = 60;
    /**
     * @hide Turned on the screen.
     */
    int OP_TURN_SCREEN_ON = 61;
    /**
     * @hide Get device accounts.
     */
    int OP_GET_ACCOUNTS = 62;
    /**
     * @hide Control whether an application is allowed to run in the background.
     */
    int OP_RUN_IN_BACKGROUND = 63;
    /**
     * @hide
     */
    int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
    /**
     * @hide Read the phone number.
     */
    int OP_READ_PHONE_NUMBERS = 65;
    /**
     * @hide Request package installs through package installer
     */
    int OP_REQUEST_INSTALL_PACKAGES = 66;
    /**
     * @hide Enter picture-in-picture.
     */
    int OP_PICTURE_IN_PICTURE = 67;
    /**
     * @hide Instant app start foreground service.
     */
    int OP_INSTANT_APP_START_FOREGROUND = 68;
    /**
     * @hide Answer incoming phone calls
     */
    int OP_ANSWER_PHONE_CALLS = 69;
    /**
     * @hide
     */
    int _NUM_OP = 70;

    /**
     * Access to coarse location information.
     */
    String OPSTR_COARSE_LOCATION = "android:coarse_location";
    /**
     * Access to fine location information.
     */
    String OPSTR_FINE_LOCATION =
            "android:fine_location";
    /**
     * Continually monitoring location data.
     */
    String OPSTR_MONITOR_LOCATION
            = "android:monitor_location";
    /**
     * Continually monitoring location data with a relatively high power request.
     */
    String OPSTR_MONITOR_HIGH_POWER_LOCATION
            = "android:monitor_location_high_power";
    /**
     * Access to {@link android.app.usage.UsageStatsManager}.
     */
    String OPSTR_GET_USAGE_STATS
            = "android:get_usage_stats";
    /**
     * Activate a VPN connection without user intervention. @hide
     */
    String OPSTR_ACTIVATE_VPN
            = "android:activate_vpn";
    /**
     * Allows an application to read the user's contacts data.
     */
    String OPSTR_READ_CONTACTS
            = "android:read_contacts";
    /**
     * Allows an application to write to the user's contacts data.
     */
    String OPSTR_WRITE_CONTACTS
            = "android:write_contacts";
    /**
     * Allows an application to read the user's call log.
     */
    String OPSTR_READ_CALL_LOG
            = "android:read_call_log";
    /**
     * Allows an application to write to the user's call log.
     */
    String OPSTR_WRITE_CALL_LOG
            = "android:write_call_log";
    /**
     * Allows an application to read the user's calendar data.
     */
    String OPSTR_READ_CALENDAR
            = "android:read_calendar";
    /**
     * Allows an application to write to the user's calendar data.
     */
    String OPSTR_WRITE_CALENDAR
            = "android:write_calendar";
    /**
     * Allows an application to initiate a phone call.
     */
    String OPSTR_CALL_PHONE
            = "android:call_phone";
    /**
     * Allows an application to read SMS messages.
     */
    String OPSTR_READ_SMS
            = "android:read_sms";
    /**
     * Allows an application to receive SMS messages.
     */
    String OPSTR_RECEIVE_SMS
            = "android:receive_sms";
    /**
     * Allows an application to receive MMS messages.
     */
    String OPSTR_RECEIVE_MMS
            = "android:receive_mms";
    /**
     * Allows an application to receive WAP push messages.
     */
    String OPSTR_RECEIVE_WAP_PUSH
            = "android:receive_wap_push";
    /**
     * Allows an application to send SMS messages.
     */
    String OPSTR_SEND_SMS
            = "android:send_sms";
    /**
     * Required to be able to access the camera device.
     */
    String OPSTR_CAMERA
            = "android:camera";
    /**
     * Required to be able to access the microphone device.
     */
    String OPSTR_RECORD_AUDIO
            = "android:record_audio";
    /**
     * Required to access phone state related information.
     */
    String OPSTR_READ_PHONE_STATE
            = "android:read_phone_state";
    /**
     * Required to access phone state related information.
     */
    String OPSTR_ADD_VOICEMAIL
            = "android:add_voicemail";
    /**
     * Access APIs for SIP calling over VOIP or WiFi
     */
    String OPSTR_USE_SIP
            = "android:use_sip";
    /**
     * Access APIs for diverting outgoing calls
     */
    String OPSTR_PROCESS_OUTGOING_CALLS
            = "android:process_outgoing_calls";
    /**
     * Use the fingerprint API.
     */
    String OPSTR_USE_FINGERPRINT
            = "android:use_fingerprint";
    /**
     * Access to body sensors such as heart rate, etc.
     */
    String OPSTR_BODY_SENSORS
            = "android:body_sensors";
    /**
     * Read previously received cell broadcast messages.
     */
    String OPSTR_READ_CELL_BROADCASTS
            = "android:read_cell_broadcasts";
    /**
     * Inject mock location into the system.
     */
    String OPSTR_MOCK_LOCATION
            = "android:mock_location";
    /**
     * Read external storage.
     */
    String OPSTR_READ_EXTERNAL_STORAGE
            = "android:read_external_storage";
    /**
     * Write external storage.
     */
    String OPSTR_WRITE_EXTERNAL_STORAGE
            = "android:write_external_storage";
    /**
     * Required to draw on top of other apps.
     */
    String OPSTR_SYSTEM_ALERT_WINDOW
            = "android:system_alert_window";
    /**
     * Required to write/modify/update system settingss.
     */
    String OPSTR_WRITE_SETTINGS
            = "android:write_settings";
    /**
     * @hide Get device accounts.
     */
    String OPSTR_GET_ACCOUNTS
            = "android:get_accounts";
    String OPSTR_READ_PHONE_NUMBERS
            = "android:read_phone_numbers";
    /**
     * Access to picture-in-picture.
     */
    String OPSTR_PICTURE_IN_PICTURE
            = "android:picture_in_picture";
    /**
     * @hide
     */
    String OPSTR_INSTANT_APP_START_FOREGROUND
            = "android:instant_app_start_foreground";
    /**
     * Answer incoming phone calls
     */
    String OPSTR_ANSWER_PHONE_CALLS
            = "android:answer_phone_calls";

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
    };

    /**
     * This maps each operation to the operation that serves as the
     * switch to determine whether it is allowed.  Generally this is
     * a 1:1 mapping, but for some things (like location) that have
     * multiple low-level operations being tracked that should be
     * presented to the user as one switch then this can be used to
     * make them all controlled by the same single operation.
     */
    int[] sOpToSwitch = new int[]{
            OP_COARSE_LOCATION,
            OP_COARSE_LOCATION,
            OP_COARSE_LOCATION,
            OP_VIBRATE,
            OP_READ_CONTACTS,
            OP_WRITE_CONTACTS,
            OP_READ_CALL_LOG,
            OP_WRITE_CALL_LOG,
            OP_READ_CALENDAR,
            OP_WRITE_CALENDAR,
            OP_COARSE_LOCATION,
            OP_POST_NOTIFICATION,
            OP_COARSE_LOCATION,
            OP_CALL_PHONE,
            OP_READ_SMS,
            OP_WRITE_SMS,
            OP_RECEIVE_SMS,
            OP_RECEIVE_SMS,
            OP_RECEIVE_MMS,
            OP_RECEIVE_WAP_PUSH,
            OP_SEND_SMS,
            OP_READ_SMS,
            OP_WRITE_SMS,
            OP_WRITE_SETTINGS,
            OP_SYSTEM_ALERT_WINDOW,
            OP_ACCESS_NOTIFICATIONS,
            OP_CAMERA,
            OP_RECORD_AUDIO,
            OP_PLAY_AUDIO,
            OP_READ_CLIPBOARD,
            OP_WRITE_CLIPBOARD,
            OP_TAKE_MEDIA_BUTTONS,
            OP_TAKE_AUDIO_FOCUS,
            OP_AUDIO_MASTER_VOLUME,
            OP_AUDIO_VOICE_VOLUME,
            OP_AUDIO_RING_VOLUME,
            OP_AUDIO_MEDIA_VOLUME,
            OP_AUDIO_ALARM_VOLUME,
            OP_AUDIO_NOTIFICATION_VOLUME,
            OP_AUDIO_BLUETOOTH_VOLUME,
            OP_WAKE_LOCK,
            OP_COARSE_LOCATION,
            OP_COARSE_LOCATION,
            OP_GET_USAGE_STATS,
            OP_MUTE_MICROPHONE,
            OP_TOAST_WINDOW,
            OP_PROJECT_MEDIA,
            OP_ACTIVATE_VPN,
            OP_WRITE_WALLPAPER,
            OP_ASSIST_STRUCTURE,
            OP_ASSIST_SCREENSHOT,
            OP_READ_PHONE_STATE,
            OP_ADD_VOICEMAIL,
            OP_USE_SIP,
            OP_PROCESS_OUTGOING_CALLS,
            OP_USE_FINGERPRINT,
            OP_BODY_SENSORS,
            OP_READ_CELL_BROADCASTS,
            OP_MOCK_LOCATION,
            OP_READ_EXTERNAL_STORAGE,
            OP_WRITE_EXTERNAL_STORAGE,
            OP_TURN_SCREEN_ON,
            OP_GET_ACCOUNTS,
            OP_RUN_IN_BACKGROUND,
            OP_AUDIO_ACCESSIBILITY_VOLUME,
            OP_READ_PHONE_NUMBERS,
            OP_REQUEST_INSTALL_PACKAGES,
            OP_PICTURE_IN_PICTURE,
            OP_INSTANT_APP_START_FOREGROUND,
            OP_ANSWER_PHONE_CALLS
    };

    /**
     * This maps each operation to the public string constant for it.
     * If it doesn't have a public string constant, it maps to null.
     */
    String[] sOpToString = new String[]{
            OPSTR_COARSE_LOCATION,
            OPSTR_FINE_LOCATION,
            null,
            null,
            OPSTR_READ_CONTACTS,
            OPSTR_WRITE_CONTACTS,
            OPSTR_READ_CALL_LOG,
            OPSTR_WRITE_CALL_LOG,
            OPSTR_READ_CALENDAR,
            OPSTR_WRITE_CALENDAR,
            null,
            null,
            null,
            OPSTR_CALL_PHONE,
            OPSTR_READ_SMS,
            null,
            OPSTR_RECEIVE_SMS,
            null,
            OPSTR_RECEIVE_MMS,
            OPSTR_RECEIVE_WAP_PUSH,
            OPSTR_SEND_SMS,
            null,
            null,
            OPSTR_WRITE_SETTINGS,
            OPSTR_SYSTEM_ALERT_WINDOW,
            null,
            OPSTR_CAMERA,
            OPSTR_RECORD_AUDIO,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            OPSTR_MONITOR_LOCATION,
            OPSTR_MONITOR_HIGH_POWER_LOCATION,
            OPSTR_GET_USAGE_STATS,
            null,
            null,
            null,
            OPSTR_ACTIVATE_VPN,
            null,
            null,
            null,
            OPSTR_READ_PHONE_STATE,
            OPSTR_ADD_VOICEMAIL,
            OPSTR_USE_SIP,
            OPSTR_PROCESS_OUTGOING_CALLS,
            OPSTR_USE_FINGERPRINT,
            OPSTR_BODY_SENSORS,
            OPSTR_READ_CELL_BROADCASTS,
            OPSTR_MOCK_LOCATION,
            OPSTR_READ_EXTERNAL_STORAGE,
            OPSTR_WRITE_EXTERNAL_STORAGE,
            null,
            OPSTR_GET_ACCOUNTS,
            null,
            null, // OP_AUDIO_ACCESSIBILITY_VOLUME
            OPSTR_READ_PHONE_NUMBERS,
            null, // OP_REQUEST_INSTALL_PACKAGES
            OPSTR_PICTURE_IN_PICTURE,
            OPSTR_INSTANT_APP_START_FOREGROUND,
            OPSTR_ANSWER_PHONE_CALLS,
    };

    /**
     * This provides a simple name for each operation to be used
     * in debug output.
     */
    String[] sOpNames = new String[]{
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
    };

    /**
     * This optionally maps a permission to an operation.  If there
     * is no permission associated with an operation, it is null.
     */
    @SuppressLint("InlinedApi") String[] sOpPerms = new String[]{
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
    };

    /**
     * Specifies whether an Op should be restricted by a user restriction.
     * Each Op should be filled with a restriction string from UserManager or
     * null to specify it is not affected by any user restriction.
     */
    String[] sOpRestrictions = new String[]{
            UserManager.DISALLOW_SHARE_LOCATION, //COARSE_LOCATION
            UserManager.DISALLOW_SHARE_LOCATION, //FINE_LOCATION
            UserManager.DISALLOW_SHARE_LOCATION, //GPS
            null, //VIBRATE
            null, //READ_CONTACTS
            null, //WRITE_CONTACTS
            UserManager.DISALLOW_OUTGOING_CALLS, //READ_CALL_LOG
            UserManager.DISALLOW_OUTGOING_CALLS, //WRITE_CALL_LOG
            null, //READ_CALENDAR
            null, //WRITE_CALENDAR
            UserManager.DISALLOW_SHARE_LOCATION, //WIFI_SCAN
            null, //POST_NOTIFICATION
            null, //NEIGHBORING_CELLS
            null, //CALL_PHONE
            UserManager.DISALLOW_SMS, //READ_SMS
            UserManager.DISALLOW_SMS, //WRITE_SMS
            UserManager.DISALLOW_SMS, //RECEIVE_SMS
            null, //RECEIVE_EMERGENCY_SMS
            UserManager.DISALLOW_SMS, //RECEIVE_MMS
            null, //RECEIVE_WAP_PUSH
            UserManager.DISALLOW_SMS, //SEND_SMS
            UserManager.DISALLOW_SMS, //READ_ICC_SMS
            UserManager.DISALLOW_SMS, //WRITE_ICC_SMS
            null, //WRITE_SETTINGS
            UserManager.DISALLOW_CREATE_WINDOWS, //SYSTEM_ALERT_WINDOW
            null, //ACCESS_NOTIFICATIONS
            null, //CAMERA
            null, //RECORD_AUDIO
            null, //PLAY_AUDIO
            null, //READ_CLIPBOARD
            null, //WRITE_CLIPBOARD
            null, //TAKE_MEDIA_BUTTONS
            null, //TAKE_AUDIO_FOCUS
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_MASTER_VOLUME
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_VOICE_VOLUME
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_RING_VOLUME
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_MEDIA_VOLUME
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_ALARM_VOLUME
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_NOTIFICATION_VOLUME
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_BLUETOOTH_VOLUME
            null, //WAKE_LOCK
            UserManager.DISALLOW_SHARE_LOCATION, //MONITOR_LOCATION
            UserManager.DISALLOW_SHARE_LOCATION, //MONITOR_HIGH_POWER_LOCATION
            null, //GET_USAGE_STATS
            UserManager.DISALLOW_UNMUTE_MICROPHONE, // MUTE_MICROPHONE
            UserManager.DISALLOW_CREATE_WINDOWS, // TOAST_WINDOW
            null, //PROJECT_MEDIA
            null, // ACTIVATE_VPN
            null, // WRITE_WALLPAPER
            null, // ASSIST_STRUCTURE
            null, // ASSIST_SCREENSHOT
            null, // READ_PHONE_STATE
            null, // ADD_VOICEMAIL
            null, // USE_SIP
            null, // PROCESS_OUTGOING_CALLS
            null, // USE_FINGERPRINT
            null, // BODY_SENSORS
            null, // READ_CELL_BROADCASTS
            null, // MOCK_LOCATION
            null, // READ_EXTERNAL_STORAGE
            null, // WRITE_EXTERNAL_STORAGE
            null, // TURN_ON_SCREEN
            null, // GET_ACCOUNTS
            null, // RUN_IN_BACKGROUND
            UserManager.DISALLOW_ADJUST_VOLUME, //AUDIO_ACCESSIBILITY_VOLUME
            null, // READ_PHONE_NUMBERS
            null, // REQUEST_INSTALL_PACKAGES
            null, // ENTER_PICTURE_IN_PICTURE_ON_HIDE
            null, // INSTANT_APP_START_FOREGROUND
            null, // ANSWER_PHONE_CALLS
    };

    /**
     * This specifies whether each option should allow the system
     * (and system ui) to bypass the user restriction when active.
     */
    boolean[] sOpAllowSystemRestrictionBypass = new boolean[]{
            true, //COARSE_LOCATION
            true, //FINE_LOCATION
            false, //GPS
            false, //VIBRATE
            false, //READ_CONTACTS
            false, //WRITE_CONTACTS
            false, //READ_CALL_LOG
            false, //WRITE_CALL_LOG
            false, //READ_CALENDAR
            false, //WRITE_CALENDAR
            true, //WIFI_SCAN
            false, //POST_NOTIFICATION
            false, //NEIGHBORING_CELLS
            false, //CALL_PHONE
            false, //READ_SMS
            false, //WRITE_SMS
            false, //RECEIVE_SMS
            false, //RECEIVE_EMERGECY_SMS
            false, //RECEIVE_MMS
            false, //RECEIVE_WAP_PUSH
            false, //SEND_SMS
            false, //READ_ICC_SMS
            false, //WRITE_ICC_SMS
            false, //WRITE_SETTINGS
            true, //SYSTEM_ALERT_WINDOW
            false, //ACCESS_NOTIFICATIONS
            false, //CAMERA
            false, //RECORD_AUDIO
            false, //PLAY_AUDIO
            false, //READ_CLIPBOARD
            false, //WRITE_CLIPBOARD
            false, //TAKE_MEDIA_BUTTONS
            false, //TAKE_AUDIO_FOCUS
            false, //AUDIO_MASTER_VOLUME
            false, //AUDIO_VOICE_VOLUME
            false, //AUDIO_RING_VOLUME
            false, //AUDIO_MEDIA_VOLUME
            false, //AUDIO_ALARM_VOLUME
            false, //AUDIO_NOTIFICATION_VOLUME
            false, //AUDIO_BLUETOOTH_VOLUME
            false, //WAKE_LOCK
            false, //MONITOR_LOCATION
            false, //MONITOR_HIGH_POWER_LOCATION
            false, //GET_USAGE_STATS
            false, //MUTE_MICROPHONE
            true, //TOAST_WINDOW
            false, //PROJECT_MEDIA
            false, //ACTIVATE_VPN
            false, //WALLPAPER
            false, //ASSIST_STRUCTURE
            false, //ASSIST_SCREENSHOT
            false, //READ_PHONE_STATE
            false, //ADD_VOICEMAIL
            false, // USE_SIP
            false, // PROCESS_OUTGOING_CALLS
            false, // USE_FINGERPRINT
            false, // BODY_SENSORS
            false, // READ_CELL_BROADCASTS
            false, // MOCK_LOCATION
            false, // READ_EXTERNAL_STORAGE
            false, // WRITE_EXTERNAL_STORAGE
            false, // TURN_ON_SCREEN
            false, // GET_ACCOUNTS
            false, // RUN_IN_BACKGROUND
            false, // AUDIO_ACCESSIBILITY_VOLUME
            false, // READ_PHONE_NUMBERS
            false, // REQUEST_INSTALL_PACKAGES
            false, // ENTER_PICTURE_IN_PICTURE_ON_HIDE
            false, // INSTANT_APP_START_FOREGROUND
            false, // ANSWER_PHONE_CALLS
    };

    /**
     * This specifies the default mode for each operation.
     */
    int[] sOpDefaultMode = new int[]{
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_IGNORED, // OP_WRITE_SMS
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_DEFAULT, // OP_WRITE_SETTINGS
            AppOpsManager.MODE_DEFAULT, // OP_SYSTEM_ALERT_WINDOW
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_DEFAULT, // OP_GET_USAGE_STATS
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_IGNORED, // OP_PROJECT_MEDIA
            AppOpsManager.MODE_IGNORED, // OP_ACTIVATE_VPN
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ERRORED,  // OP_MOCK_LOCATION
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,  // OP_TURN_ON_SCREEN
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_ALLOWED,  // OP_RUN_IN_BACKGROUND
            AppOpsManager.MODE_ALLOWED,  // OP_AUDIO_ACCESSIBILITY_VOLUME
            AppOpsManager.MODE_ALLOWED,
            AppOpsManager.MODE_DEFAULT,  // OP_REQUEST_INSTALL_PACKAGES
            AppOpsManager.MODE_ALLOWED,  // OP_PICTURE_IN_PICTURE
            AppOpsManager.MODE_DEFAULT,  // OP_INSTANT_APP_START_FOREGROUND
            AppOpsManager.MODE_ALLOWED, // ANSWER_PHONE_CALLS
    };

    /**
     * This specifies whether each option is allowed to be reset
     * when resetting all app preferences.  Disable reset for
     * app ops that are under strong control of some part of the
     * system (such as OP_WRITE_SMS, which should be allowed only
     * for whichever app is selected as the current SMS app).
     */
    boolean[] sOpDisableReset = new boolean[]{
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,      // OP_WRITE_SMS
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false, // OP_AUDIO_ACCESSIBILITY_VOLUME
            false,
            false, // OP_REQUEST_INSTALL_PACKAGES
            false, // OP_PICTURE_IN_PICTURE
            false,
            false, // ANSWER_PHONE_CALLS
    };

    /**
     * Mapping from an app op name to the app op code.
     */
    HashMap<String, Integer> sOpStrToOp = new HashMap<String, Integer>() {{
        for (int i = 0; i < _NUM_OP; i++) {
            if (sOpToString[i] != null) {
                put(sOpToString[i], i);
            }
        }
    }};

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

    static String opToName(int op) {
        if (op == OP_NONE) return "NONE";
        return op < sOpNames.length ? sOpNames[op] : ("Unknown(" + op + ")");
    }

    static int permissionToOpCode(String permission) {
        Integer boxedOpCode = sPermToOp.get(permission);
        return boxedOpCode != null ? boxedOpCode : OP_NONE;
    }
}
