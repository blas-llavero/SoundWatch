# SoundWatch

Aplicació Android de codi obert que vigila el nivell de soroll en segon pla i
genera una notificació quan el nivell **estimat** supera 80 dB durant 1 segon.
Aplica una espera d'1 minut abans de tornar a avisar. Una polsera Fitbit Charge 6
pot mostrar l'alerta mitjançant la rèplica de notificacions del telèfon.

## Privadesa

Tot el processament es fa localment. L'app no desa àudio, no l'envia a Internet,
no conté analítica ni necessita permís de xarxa.

## Precisió i calibratge

Android proporciona amplitud digital, no dB SPL calibrats. Els micròfons i el
control automàtic de guany varien entre telèfons. Per això el valor inicial és
orientatiu i **no és un sonòmetre certificat**. Cal ajustar el desplaçament de
calibratge comparant la lectura amb un sonòmetre al mateix lloc i amb un so estable.

## Compilar

1. Obre la carpeta amb Android Studio (JDK 17).
2. Deixa que Gradle sincronitzi el projecte.
3. Connecta un telèfon Android 8 o posterior.
4. Executa la configuració `app`.

## Configurar el Fitbit Charge 6

1. Emparella el Charge 6 amb l'app Fitbit i activa les notificacions del telèfon.
2. A Fitbit: dispositiu Charge 6 → Notificacions → Notificacions d'aplicacions.
3. Activa `SoundWatch`. Pot ser necessari que l'app hagi emès una notificació abans.
4. A Android, permet les notificacions de SoundWatch i exclou-la de l'estalvi de
   bateria si el fabricant atura serveis en segon pla.

El Charge 6 no executa aquesta app: només mostra la notificació generada pel telèfon.

## Comportament

- Llindar: 80 dB estimats.
- Durada mínima: 1 segon continu.
- Pausa entre alertes: 60 segons.
- Monitoratge: servei en primer pla amb notificació persistent, tal com exigeix Android.

## Llicència

Apache License 2.0. Consulta [LICENSE](LICENSE).

