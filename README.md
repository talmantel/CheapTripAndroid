# CheapTripAndroid

This is mobile application to find the most profitable and unusual routes between cities with airports by combining plane, train, bus, ferry and BlaBlaCar.

It's planned to use it as a mobile client for a shared server along with a web application, or as a stand-alone unit.

It's supposed to be developed for two mobile platforms (Android and iOS). A cross-platform implementation based on Kotlin Multiplatform Mobile is currently being developed. Common code and UI version for Android are moving forward, UI version for IOS is pending later.

## Getting started

Use Android Studio v4.1.3 and build on Gradle v6.5. The project created on Kotlin v1.4.32 and KMM v0.2.0, appropriate plugins to be installed. Other required dependencies are specified in build.gradle files and are automatically installed.

## Project structure

* **shared** - Cross-platform codebase for the business logic.
    * commonMain - Shared code with expected classes/funs/properties.
    * commonTest - Unit tests
    * androidMain - Actual (platform-specific) classes/funs/properties for Android.
    * androidTest - Unit tests requiring Android SDK.
    * iosMain - Actual (platform-specific) classes/funs/properties for iOS.
    * iosTest - Unit tests requiring iOS SDK.
* **androidApp** - Platform-specific codebase for the UI version for Android, instrumental tests.
* **iosApp** - Platform-specific codebase for the UI version for iOS, instrumental tests.

> For more details see README.md into modules/packages directories.

## Conventions

### Branching

* **main** - Once testing on **release** branch is done, **release** is merged into **main** to be deployed in production.
* **release** - Once development of current version is done, **dev** is merged into **release** for testing and final bugfixes.
* **dev** - Main development branch. All new features and all bugs for current version should be done here.

> The **kmm-version** branch, forked from **dev**, is currently a basic for cross-platform implementation.

### Versioning

**dev** - dx.x.x
**release** - rx.x.x
**main** - x.x.x

## Developing

1. Create a new branch from **dev** or down, name it with the feature or issue you are dealing with.
2. Develop...
3. Merge remote upstream branch into your branch.
4. Push your branch to remote.
5. Create a pull request: https://github.com/talmantel/CheapTripAndroid/pull/new/**your_branch_name** into upstream branch.

## Issues

When a new issue is found, create a new issue here: https://github.com/talmantel/CheapTripAndroid/issues/new

Make sure to add appropriate labels including:
* **module: common**
* **module: android**
* **module: ios**
in accordance with the belonging of issue to the common/platform codebase.

Assign Denis (@denis-luttcev) or any developer if you know who is reponsible for resolving the issue.

**Resolving issues:**

Link to issues fixed in a specific pull request or commit by typing "Resolves: #{issue number}" in your pull request description or commit message. For example "Resolves: #123".

> For more details see https://docs.github.com/en/enterprise/2.16/user/github/managing-your-work-on-github/closing-issues-using-keywords

You can also see open issues assigned to you here: https://github.com/talmantel/CheapTripAndroid/issues/assigned/@me and comment, close etc.

## When a new version is released

Use this link to see non-verified closed issues:

https://github.com/talmantel/CheapTripAndroid/issues?q=is%3Aissue+-label%3Averified+state%3Aclosed

To verify an issue was resolved, add the label 'verified' to it. If the issue was not resolved, reopen the issue.