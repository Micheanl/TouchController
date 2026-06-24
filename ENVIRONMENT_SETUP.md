# Setting up development environment

TouchController makes use of [Bazel](https://bazel.build/) instead of the commonly used Gradle.

I know that Bazel is hard to learn, but for most small contributions, you will not need to touch Bazel files.
However, it is still necessary to set up Bazel at first.

## Platform support policy

Linux & macOS will get first-class support in development.

Bazel on Windows has many problems (can't start dev client, some targets don't build), so Windows users may need to
use WSL for better developing experience. Building mod JARs on Windows is supported anyway.

## Install Bazel

Use `bazelisk`. Follow [its README](https://github.com/bazelbuild/bazelisk) to install `bazelisk` to your system.

On plain Windows (not in WSL), you also need [MSYS2](https://www.msys2.org/)
and [Visual Studio Build Tools](https://aka.ms/buildtools).

## Install Android SDK & NDK

The project needs to build native libraries for Android, so Android SDK & NDK is required. The simple way to install
them
is to use [Android Studio](https://developer.android.com/studio), or you can do it manually:

- Download the [command-line tools](https://developer.android.com/studio#command-line-tools-only) from Google
- Put it under `<android_sdk>/cmdline-tools/latest/`, `<android_sdk>` is the root of the Android SDK
- Run `sdkmanager` (in `<android_sdk>/cmdline-tools/latest/bin`) to install recent version
  of `build-tools`, `ndk` and `platforms`.

Whichever method you choose, you must set two environment variables:
`ANDROID_HOME` to `<android_sdk>` (the SDK root), and `ANDROID_NDK_HOME` to `<android_sdk>/ndk/<version>`.

## Install Bazel plugin for IntelliJ IDEA

IntelliJ IDEA is the preferred IDE for this project, because:

- It has good Kotlin support
- It supports Bazel well

Until someone develops a script to generate `workspace.json` for `kotlin-lsp`, IntelliJ IDEA is the only editor that
can work perfectly now.

Install the plugin at JetBrains Marketplace: <https://plugins.jetbrains.com/plugin/22977-bazel>, and open the project.

## Open the project and sync

Open the project, there will be a notification to load the Bazel workspace. After loading the workspace, the IDE will
start syncing the project, and you are ready to go!
