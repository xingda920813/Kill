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

import android.app.usage.*;

/**
 * API for interacting with "application operation" tracking.
 *
 * <p>This API is not generally intended for third party application developers; most
 * features are only available to system applications.
 */
public interface LocalAppOpsManager {

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
}
