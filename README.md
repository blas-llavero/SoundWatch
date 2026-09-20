# dBNora

dBNora is an open-source Android app that monitors environmental sound in
the background and creates an alert when the **estimated** level exceeds 80 dB
for one second. It then waits one minute before another alert. A Fitbit Charge 6
can display the alert through Android notification mirroring.

## Creator

dBNora was created by **Blas Llavero** in Reus, Catalonia, in 2026. The
original idea, purpose, product requirements and project direction are his.
Development has been carried out with AI-assisted tools and open-source
components.

## Download the APK from GitHub

Every push to the `main` branch starts the **Android CI** workflow. When the
workflow finishes successfully:

1. Open the repository's **Actions** tab.
2. Select the latest successful **Android CI** run.
3. Scroll to the **Artifacts** section at the bottom of the run summary.
4. Download **dBNora-debug-apk**. GitHub downloads a ZIP file.
5. Extract the ZIP on the Android phone. The installation file inside is
   `app-debug.apk`.

GitHub requires you to be signed in before downloading workflow artifacts.
Debug APKs are intended for testing and are not signed for Google Play release.

## Install the APK on Android

1. Download and extract `dBNora-debug-apk.zip` on the phone.
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

## Run, calibrate and use dBNora

1. Open **dBNora** from the Android app drawer.
2. On the first launch, watch or close the built-in introduction. It shows four
   examples in which a phone/watch alert leads to a safer action.
3. Tap **Start monitoring** and allow microphone access and notifications.
4. dBNora works immediately with its default estimate. A sound level meter
   is not required for normal use.
5. Tap **Introduction** to watch the preventive introduction again, or tap
   **How to use the app** to open the separate instructional video.
6. To improve accuracy optionally, open **Optional calibration**.
7. Place the phone beside a sound level meter while a stable, moderate sound is
   present. Avoid using dangerously loud sound for calibration.
8. Wait for the live dBNora reading to become stable.
9. Enter the sound meter's reading in **Sound meter reading (dB)**.
10. Tap **Calibrate automatically**. dBNora compares the reference with its
   live reading, calculates the new offset and saves it.
11. Keep the persistent monitoring notification active. dBNora will alert
   after the estimated level remains above 80 dB for one second.
12. After an alert, dBNora waits 60 seconds before issuing another one.
13. Tap **Stop monitoring**, or use **Stop** in the persistent notification, to
   release the microphone and end the foreground service.

For reliable background operation, open Android's battery settings for
dBNora and select **Unrestricted** if the phone manufacturer stops the app.
The exact name of this setting varies by Android device.

Both the introduction and help videos are bundled with the APK, play without
Internet access, and do not transmit any data. Each video follows the Android
device language. English is used as the fallback.

## Languages

The interface follows the Android device language automatically. It includes
English, Spanish, Catalan, French, German, Italian, Portuguese, Simplified
Chinese, Hindi and Arabic. English is the fallback language. Arabic supports
right-to-left layout.

The preventive introduction and the help video include localized text in all
ten languages. Natural narration for the corrected branded sentence is
included in English, Catalan, Spanish, German, French, Italian and Portuguese.
The Simplified Chinese, Arabic and Hindi corrections use localized subtitles
without replacement narration rather than bundling an unsuitable or
restrictively licensed voice model. The help videos are intentionally silent.

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
3. Enable `dBNora`. It may need to issue its first notification before it
   appears in this list.
4. Allow dBNora notifications in Android. If the manufacturer stops
   background services, exclude dBNora from battery optimization.

The Charge 6 does not run dBNora itself. It displays the notification
created by the Android phone.

## Show alerts on a Fitbit Charge 6

1. Start dBNora once and allow it to create notifications.
2. Open the Fitbit app and select the Charge 6.
3. Open **Notifications → App notifications**.
4. Enable **dBNora**.
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

## Open-source media and voice assets

The preventive introduction artwork and editing are distributed with this
project under the Apache License 2.0. Narration was generated locally with
[Piper](https://github.com/OHF-Voice/piper1-gpl). The bundled narrated editions
use the following source datasets and licenses:

- English `ljspeech`: public domain.
- Catalan `upc_ona`: CC BY-SA 3.0 ES.
- German `thorsten`: CC0.
- Spanish `davefx`: CC0.
- French `siwis`: CC BY 4.0.
- Italian `serena`: CC BY 4.0.
- Portuguese `tugão`: CC0.
- Simplified Chinese `chaowen`: CC0.

Arabic and Hindi narration generated during development is not included in the
application because the available voice datasets did not meet the project's
open-license requirement.

See [MEDIA_LICENSES.md](MEDIA_LICENSES.md) for attribution and the license that
applies to each localized media file.

## License

Apache License 2.0. See [LICENSE](LICENSE).
