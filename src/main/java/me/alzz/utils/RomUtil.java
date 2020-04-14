package me.alzz.utils;

import android.os.Build;
import android.text.TextUtils;

public class RomUtil {

    private static final String ROM_MIUI = "MIUI";
    private static final String ROM_EMUI = "EMUI";
    private static final String ROM_FLYME = "FLYME";
    private static final String ROM_OPPO = "OPPO";
    private static final String ROM_SMARTISAN = "SMARTISAN";
    private static final String ROM_VIVO = "VIVO";
    // 联想系统vibe ui
    private static final String ROM_VIBEUI = "VIBEUI";
    private static final String ROM_H2OS = "H2OS";
    private static final String ROM_QIHU = "QIHU";

    private static String sRomName = "";
    private static String sVersion = "";

    /**
     * 华为EMUI系统
     */
    public static boolean isEmui() {
        return isRom(ROM_EMUI);
    }

    /**
     * 小米MIUI系统
     */
    public static boolean isMiui() {
        return isRom(ROM_MIUI);
    }

    /**
     * VIVO系统
     */
    public static boolean isVivo() {
        return isRom(ROM_VIVO);
    }

    /**
     * OPPO系统
     */
    public static boolean isOppo() {
        return isRom(ROM_OPPO);
    }

    /**
     * FLYME系统
     */
    public static boolean isFlyme() {
        return isRom(ROM_FLYME);
    }

    /**
     * 锤子系统
     */
    public static boolean isSmartisan() {
        return isRom(ROM_SMARTISAN);
    }

    public static boolean isQihu() {
        return isRom(ROM_QIHU);
    }

    public static String getRomVersion() {
        if (TextUtils.isEmpty(sVersion)) {
            loadRomInfo();
        }
        return sVersion;
    }

    public static String getRomName() {
        if (TextUtils.isEmpty(sRomName)) {
            loadRomInfo();
        }
        return sRomName;
    }

    private static void loadRomInfo() {
        {
            // 华为系统
            final String version = getSystemProperty("ro.build.version.emui");
            if (!TextUtils.isEmpty(version)) {
                cacheRomInfo(ROM_EMUI, version);
                return;
            }
        }

        {
            // 小米系统
            final String name = getSystemProperty("ro.miui.ui.version.name");
            final String code = getSystemProperty("ro.miui.ui.version.code");
            final String storage = getSystemProperty("ro.miui.internal.storage");
            if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(code) || !TextUtils.isEmpty(storage)) {
                cacheRomInfo(ROM_MIUI, name);
                return;
            }
        }

        {
            // oppo系统
            final String version = getSystemProperty("ro.rom.different.version");
            final String oppoRom = getSystemProperty("ro.build.version.opporom");
            if (!TextUtils.isEmpty(version) || !TextUtils.isEmpty(oppoRom)) {
                cacheRomInfo(ROM_OPPO, oppoRom);
                return;
            }
        }

        {
            // vivo系统
            final String version = getSystemProperty("ro.vivo.os.version");
            final String name = getSystemProperty("ro.vivo.os.name");
            if (!TextUtils.isEmpty(version) || !TextUtils.isEmpty(name)) {
                cacheRomInfo(ROM_VIVO, version);
                return;
            }
        }

        {
            // 锤子系统
            final String version = getSystemProperty("ro.smartisan.version");
            if (!TextUtils.isEmpty(version)) {
                cacheRomInfo(ROM_SMARTISAN, version);
                return;
            }
        }

        {
            // 魅族系统
            final String display = Build.DISPLAY;
            if (!TextUtils.isEmpty(display) && display.toUpperCase().contains(ROM_FLYME)) {
                cacheRomInfo(ROM_FLYME, display);
                return;
            }
        }

        {
            // 联想系统
            final String version = getSystemProperty("ro.lenovo.lvp.version");
            if (!TextUtils.isEmpty(version)) {
                cacheRomInfo(ROM_VIBEUI, version);
                return;
            }
        }

        {
            // 一加系统
            final String version = getSystemProperty("ro.rom.version");
            if (!TextUtils.isEmpty(version)) {
                cacheRomInfo(ROM_H2OS, version);
                return;
            }
        }

        {
            // 360系统
            String manufacturer = Build.MANUFACTURER.toUpperCase();
            if (!TextUtils.isEmpty(manufacturer) && manufacturer.equalsIgnoreCase(ROM_QIHU)) {
                cacheRomInfo(ROM_QIHU, Build.DISPLAY);
                return;
            }
        }

        cacheRomInfo(Build.DISPLAY, Build.DISPLAY);
    }

    private static void cacheRomInfo(String romName, String version) {
        sRomName = romName;
        sVersion = version;
    }

    private static boolean isRom(String romName) {
        if (TextUtils.isEmpty(sRomName)) {
            loadRomInfo();
        }

        return sRomName.equals(romName);
    }

    private static String getSystemProperty(String key) {
        String value = "";

        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
        }

        return value;
    }
}
