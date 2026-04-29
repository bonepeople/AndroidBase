# AndroidBase

**AndroidBase** is a custom project tailored for my personal development needs. It aims to provide convenient solutions for daily development tasks. Please note that using this library comes with certain limitations and risks. Evaluate carefully whether it suits your requirements.

1. The code in this project is structured according to personal preferences and may not be suitable for all use cases or developers.
2. To reduce development overhead, some common methods may lack detailed documentation.
3. The project has not undergone comprehensive testing and may exhibit issues in unforeseen scenarios. Using it blindly could lead to unexpected problems during your app's runtime.
4. This project is released under the **GPL-3.0 license**. Projects that use this library must also remain open source.
5. If you still wish to use this library, consider using [AndroidWidget][AndroidWidgetLibrary] instead. `AndroidWidget` is the core dependency of this project, and most of its features are built upon it. Hopefully, it can better meet your needs.

[AndroidWidgetLibrary]: https://github.com/bonepeople/AndroidWidget


**Updated version to 1.7.3**

### Crash Handling Improvements
- Fixed duplicate crash handling caused by coroutine exception propagation
- Introduced `CrashExceptionStore` to deduplicate exceptions and ensure each crash is processed only once
- Improved `CrashHandler` stability:
    - Switched to `supervisorScope` to isolate failures
    - Wrapped tasks with `runCatching` to avoid secondary crashes
    - Added 2-second timeout to prevent blocking

### Architecture Refactoring
- Extracted default crash handling into `CrashDefaultActions` (logging, reporting, file saving, viewer intent)
- Updated crash callback signature to `(thread, exception)`
- Added `setCrashActionWithThread`
- Deprecated legacy APIs with migration guidance

### Project Improvements
- Updated `.gitignore` and codestyle

### Activity Base Class Update
- Introduced `ViewBindingActivity2` based on `FragmentActivity` (no AppCompat theme required)
- Migrated existing usages
- Deprecated `ViewBindingActivity` (planned for future removal)


## Release 1.7.2
### Improvements
- Add build timestamp logging and corresponding config field to improve build traceability.
- Update AndroidWidget and refine related implementation for better consistency.

### New Features
- Add `SimpleLoadingDialog` for displaying loading states in a more lightweight and straightforward way.
- Introduce `switchShow()` method to simplify loading dialog visibility control.

### Refactoring

- Replace existing `loadingDialog` with `simpleLoadingDialog` to standardize loading UI usage and reduce complexity.




## Release 1.7.1
### Improvements
- Allow `LifecycleOwner` to be safely used in background threads, improving flexibility in asynchronous scenarios.
- Store `appName` asynchronously during app startup to reduce blocking operations.

### New Features
- Add `bindCompoundButton` extension for `MutableStateFlow<Boolean>`
    - Provides a simple way to bind `MutableStateFlow<Boolean>` with `CompoundButton` components such as `Switch` and `CheckBox`.
    - Uses lifecycle-aware observation to keep UI state and data flow in sync.

### Dependency Updates
- Upgrade `androidx.fragment:fragment-ktx` from `1.5.6` to `1.6.2`.
- Update `targetSdk` and `compileSdk` from 31 to 33 to align with newer Android platform requirements.
