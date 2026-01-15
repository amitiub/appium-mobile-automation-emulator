**Author: Amit Kumar Das**
**Test Automation Consultant/Framework Architect**


**License**

This project is intended for learning, demonstration, and portfolio purposes.

## Android Mobile Automation Framework (Appium + ADB)

This repository contains a **production-ready Android mobile automation framework** built using **Appium**, **Java**, **TestNG**, and **Page Object Model (POM)**.

---

## Technology Stack

- Language: Java 17  
- Automation Tool: Appium 3  
- Driver: UiAutomator2  
- Test Framework: TestNG  
- Build Tool: Maven  
- Design Pattern: Page Object Model (POM)  
- Execution: Android Emulator / Real Device via ADB  

---

## Key Features

- Local Android execution using **ADB**
- Clean **driver lifecycle management**
- Single login per device** optimization
- Thread-safe driver pooling
- Explicit app lifecycle control (force-close on suite completion)
- Cloud-provider-agnostic (can be extended to BrowserStack / SauceLabs)
- Designed for scalability and parallel execution

---

## Project Structure

src
└── test
├── core
│ ├── BaseTest.java
│ └── DriverFactory.java
├── pages
└── tests



---

## How to Run Locally

### Prerequisites

- Java 17+
- Node.js (LTS)
- Android SDK
- Appium Server
- Android Emulator (AVD) or Real Device
- Maven

---

### Start Android Emulator

```bash
emulator -avd Pixel7API30

Verify:
adb devices


Start Appium Server
appium --address 127.0.0.1 --port 4723

Verify in browser:
http://127.0.0.1:4723/status


Run Tests

mvn clean test


Optional overrides:

mvn clean test -Dudid=emulator-5554 -DappPackage=com.swaglabsmobileapp


Framework Design Highlights

Driver Pooling: One driver per device for performance optimization

Session Reuse: Avoids repeated login across tests

Explicit Teardown: App is force-closed after suite execution

Extensible Design: Easy to add cloud execution back if needed


APK Handling

APK file is not included in this repository.

Place your APK locally and provide the path via:

-Dapp=/path/to/your.apk


Future Enhancements

Parallel execution across multiple local emulators

CI integration (GitHub Actions / Jenkins)

iOS execution support

Hybrid app (WebView) support



