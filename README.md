# WebSocket ZeroMQ Proxy

Proxy that forwards messages between ZeroMQ and WebSockets. This proxy must run, so that [editor-browser](https://github.com/monto-editor/editor-browser) can communicate with the [broker](https://github.com/monto-editor/broker). This project is build using Gradle.

## IntelliJ setup
After cloning, use the `Import Project` or `File -> New -> Project from Existing Sources...` feature and select the `build.gradle` to start the import.

## Eclipse setup
Make sure you have an up-to-date Buildship Gradle Plug-in installed. At the time of writing Eclipse 4.5.2 (Mars 2) is the newest stable Eclipse build. It ships with the Buildship Gradle Plug-in version 1.0.8, but you will need at least 1.0.10, because of [these changes](https://discuss.gradle.org/t/gradle-prefs-contains-absolute-paths/11475/34). To update Buildship, use the Eclipse Marketplace's `Installed` tab.

After cloning, use the `File -> Import -> Existing Projects into Workspace` feature and select the root folder of this repository to start the import.

## Running services
Run or debug the [Main](src/monto/broker/wensocket/Main.java) class and set the CLI arguments to the ones used in the `start.sh` script.
