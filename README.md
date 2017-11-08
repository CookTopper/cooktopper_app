# CookTopper Mobile Application

This is an Android application, to control the intelligent cooker through the mobile phone. The features of this application relate to the web application, also available in this repository: [CookTopper Display App](https://github.com/CookTopper/display_app)

## Getting Started

Follow the instructions below to run the project.

### Prerequisites

#### Java 8

Considering that your work environment is a Debian-_like_ Linux distro run the commands:

```shell
$ sudo add-apt-repository ppa:webupd8team/java
$ sudo apt-get update
$ sudo apt-get install oracle-java8-installer
```

Set Oracle Java 8 as your default compiler, avoiding the use of OpenJDK.

```shell
$ sudo apt-get install oracle-java8-set-default
```

Type ```$ java -version``` at your terminal to verify the version installed and default compiler. You need the version 1.8.x, and Java (TM) SE Runtime Environment.

```shell
freemanpivo@gnome:~$ java -version
java version "1.8.0_144"
Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
```

#### Android Studio

You can download the IDE [here](https://dl.google.com/dl/android/studio/ide-zips/3.0.0.18/android-studio-ide-171.4408382-linux.zip?hl=pt-br)

After that, run android studio:

```shell
$ cd android-studio/bin
$ ./studio.sh
```

Add Android Studio to your start entry:

* Click _Tools_ > Create Desktop Entry...
* Click [OK]

## Built With

* Android Studio 3.0 [Google Android Studio]()
* Android Support
  * Cardview v7
  * RecyclerView v7
* Android GMS
  * Play Services 9.8

## Authors

* **Izabela Cristina** [izacristina](https://github.com/izacristina)
* **Paulo Markes Calado** [ShutUpPaulo](https://github.com/izacristina)
* **Pedro Ivo de Andrade** [freemanpivo](https://github.com/freemanpivo)

## License

This project is licensed under the GPLv3 - see the [LICENSE.md](LICENSE.md) file for details
