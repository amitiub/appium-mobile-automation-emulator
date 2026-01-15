package com.amit.apptest.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Local (ADB) driver factory.
 *
 * This project previously created sessions on BrowserStack. All remote/cloud-specific
 * configuration has been removed so the same tests can run against your local device/emulator
 * via ADB + Appium.
 */
public class DriverFactory {

    /**
     * Local Appium server URL.
     *
     * Override via:
     *  -DappiumServerUrl=http://127.0.0.1:4723
     * or environment variable APPIUM_SERVER_URL.
     */
    private static final String DEFAULT_APPIUM_SERVER_URL = "http://127.0.0.1:4723";

    /**
     * Default APK shipped with this repo (project root).
     * Override via:
     *  -Dapp=path/to/app.apk
     * or environment variable APP_PATH.
     */
    private static final String DEFAULT_APP_RELATIVE_PATH = "Android.SauceLabs.apk";

    public static AppiumDriver create(String deviceName, String platformVersion, String testName) throws Exception {

        // In the old BrowserStack suite, "deviceName" was a marketing name (e.g., "Google Pixel 9").
        // For local ADB runs, pass the actual device UDID (e.g., "emulator-5554" or a real device id).
        String udid = getNonBlank(System.getProperty("udid"), deviceName, "emulator-5554");

        String appiumServerUrl = getNonBlank(
                System.getProperty("appiumServerUrl"),
                System.getenv("APPIUM_SERVER_URL"),
                DEFAULT_APPIUM_SERVER_URL
        );

        String appPathOrPackage = getNonBlank(
                System.getProperty("app"),
                System.getenv("APP_PATH"),
                DEFAULT_APP_RELATIVE_PATH
        );

        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName(udid);
        options.setUdid(udid);

        // If an APK path is provided, install/launch from APK.
        // Otherwise, you can run against an already-installed app by passing -DappPackage/-DappActivity.
        Path resolvedAppPath = resolveAppPath(appPathOrPackage);
        if (resolvedAppPath != null) {
            options.setApp(resolvedAppPath.toString());
        }

        String appPackage = getNonBlank(System.getProperty("appPackage"), "com.swaglabsmobileapp");
        String appActivity = getNonBlank(System.getProperty("appActivity"), "com.swaglabsmobileapp.SplashActivity");
        options.setAppPackage(appPackage);
        options.setAppActivity(appActivity);

        // Helpful defaults for local execution
        options.setAutoGrantPermissions(true);
        options.setNoReset(true);
        options.setNewCommandTimeout(Duration.ofSeconds(120));

        // If you want to force a specific platform version locally, set -DplatformVersion=...
        String pv = getNonBlank(System.getProperty("platformVersion"), platformVersion);
        if (pv != null && !pv.equalsIgnoreCase("auto")) {
            options.setPlatformVersion(pv);
        }

        return new AndroidDriver(new URL(appiumServerUrl), options);
    }

    private static Path resolveAppPath(String app) {
        if (app == null || app.isBlank()) {
            return null;
        }

        // If it looks like an APK path, resolve it.
        // If it's not a file path (e.g., user wants to use appPackage/appActivity), return null.
        if (!app.toLowerCase().endsWith(".apk")) {
            return null;
        }

        Path p = Paths.get(app);
        if (!p.isAbsolute()) {
            // Resolve relative paths from project root
            p = Paths.get(System.getProperty("user.dir")).resolve(p).normalize();
        }

        if (!Files.exists(p)) {
            throw new IllegalArgumentException(
                    "APK not found at: " + p + "\n" +
                            "Provide a valid path via -Dapp=... or set APP_PATH, or put the APK in the project root."
            );
        }
        return p;
    }

    private static String getNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) {
                return v.trim();
            }
        }
        return null;
    }
}
