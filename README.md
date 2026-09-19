# SoundWatch

SoundWatch is an open-source Android app that monitors environmental sound in
the background and creates an alert when the **estimated** level exceeds 80 dB
for one second. It then waits one minute before another alert. A Fitbit Charge 6
can display the alert through Android notification mirroring.

## Download the APK from GitHub

Every push to the `main` branch starts the **Android CI** workflow. When the
workflow finishes successfully:

1. Open the repository's **Actions** tab.
2. Select the latest successful **Android CI** run.
3. Scroll to the **Artifacts** section at the bottom of the run summary.
4. Download **SoundWatch-debug-apk**. GitHub downloads a ZIP file.
5. Extract the ZIP on the Android phone. The installation file inside is
   `app-debug.apk`.

GitHub requires you to be signed in before downloading workflow artifacts.
Debug APKs are intended for testing and are not signed for Google Play release.

## Install the APK on Android

1. Download and extract `SoundWatch-debug-apk.zip` on the phone.
2. Open `app-debug.apk` from the Files or Downloads app.
3. If Android blocks the installation, tap **Settings** on the warning and
   temporarily enable **Allow from this source** for the app used to open the
   APK, such as Chrome or Files.
4. Return to the installer and tap **Install**.
5. After installation, disable **Allow from this source** again if you do not
   normally install APKs manually.

If Android reports that the app cannot be installed, uninstall an older build
signed with a different key and try again. Uninstalling also removes that
build's settings.

## Run, calibrate and use SoundWatch

1. Open **SoundWatch** from the Android app drawer.
2. Tap **Start monitoring** and allow microphone access and notifications.
3. SoundWatch works immediately with its default estimate. A sound level meter
   is not required for normal use.
4. To improve accuracy optionally, open **Optional calibration**.
5. Place the phone beside a sound level meter while a stable, moderate sound is
   present. Avoid using dangerously loud sound for calibration.
6. Wait for the live SoundWatch reading to become stable.
7. Enter the sound meter's reading in **Sound meter reading (dB)**.
8. Tap **Calibrate automatically**. SoundWatch compares the reference with its
   live reading, calculates the new offset and saves it.
9. Keep the persistent monitoring notification active. SoundWatch will alert
   after the estimated level remains above 80 dB for one second.
10. After an alert, SoundWatch waits 60 seconds before issuing another one.
11. Tap **Stop monitoring**, or use **Stop** in the persistent notification, to
   release the microphone and end the foreground service.

For reliable background operation, open Android's battery settings for
SoundWatch and select **Unrestricted** if the phone manufacturer stops the app.
The exact name of this setting varies by Android device.

## Languages

The interface follows the Android device language automatically. It includes
English, Spanish, Catalan, French, German, Italian, Portuguese, Simplified
Chinese, Hindi and Arabic. English is the fallback language. Arabic supports
right-to-left layout.

## Privacy

All audio processing stays on the phone. The app never saves or uploads audio,
contains no analytics, and does not request Internet access.

## Accuracy and calibration

Android provides digital amplitude rather than calibrated sound-pressure level
(dB SPL). Phone microphones and automatic gain control vary between devices.
The initial value is therefore an estimate and **not a certified sound meter**.
Calibrate the offset by comparing the reading with a sound level meter next to
the phone while a stable sound is playing.

## Configure a Fitbit Charge 6

1. Pair the Charge 6 in the Fitbit app and enable phone notifications.
2. Open Charge 6 → Notifications → App notifications in Fitbit.
3. Enable `SoundWatch`. It may need to issue its first notification before it
   appears in this list.
4. Allow SoundWatch notifications in Android. If the manufacturer stops
   background services, exclude SoundWatch from battery optimization.

The Charge 6 does not run SoundWatch itself. It displays the notification
created by the Android phone.

## Show alerts on a Fitbit Charge 6

1. Start SoundWatch once and allow it to create notifications.
2. Open the Fitbit app and select the Charge 6.
3. Open **Notifications → App notifications**.
4. Enable **SoundWatch**.
5. Keep Bluetooth, Fitbit notification access and phone notifications enabled.

## Default behaviour

- Threshold: estimated 80 dB.
- Minimum duration: one continuous second.
- Alert cooldown: 60 seconds.
- Monitoring: foreground service with a persistent notification, as required by Android.

## Build from source

1. Install Android Studio and JDK 17.
2. Clone this repository or download and extract its source ZIP.
3. Open the project folder in Android Studio.
4. Allow Gradle to synchronize and download the required dependencies.
5. Connect a phone running Android 8.0 or later with USB debugging enabled, or
   start an Android emulator.
6. Select the `app` configuration and click **Run**.

To build an APK from the command line with Gradle 8.9, run:

```bash
gradle assembleDebug
```

The generated file will be located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## License

Apache License 2.0. See [LICENSE](LICENSE).
