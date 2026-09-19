# SoundWatch

SoundWatch is an open-source Android app that monitors environmental sound in
the background and creates an alert when the **estimated** level exceeds 80 dB
for one second. It then waits one minute before another alert. A Fitbit Charge 6
can display the alert through Android notification mirroring.

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

## Build

1. Open the project in Android Studio with JDK 17.
2. Allow Gradle to synchronize the project.
3. Connect a phone running Android 8.0 or later.
4. Run the `app` configuration.

## Configure a Fitbit Charge 6

1. Pair the Charge 6 in the Fitbit app and enable phone notifications.
2. Open Charge 6 → Notifications → App notifications in Fitbit.
3. Enable `SoundWatch`. It may need to issue its first notification before it
   appears in this list.
4. Allow SoundWatch notifications in Android. If the manufacturer stops
   background services, exclude SoundWatch from battery optimization.

The Charge 6 does not run SoundWatch itself. It displays the notification
created by the Android phone.

## Default behaviour

- Threshold: estimated 80 dB.
- Minimum duration: one continuous second.
- Alert cooldown: 60 seconds.
- Monitoring: foreground service with a persistent notification, as required by Android.

## License

Apache License 2.0. See [LICENSE](LICENSE).
