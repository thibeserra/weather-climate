# weather-climate
App that show the weather's climate around fifity distance by device's current location

## Project Environment
- OS: Linux Mint v.19 Tara 64 bits   https://linuxmint.com/download.php
- ENVIRONMENT DEVELOPMENT: Android Studio v3.5.3   https://developer.android.com/studio?hl=pt-br
- FRAMEWORK DEVELOPMENT: SDK v29 (API 29.0.2: Android 10.0 Q), SDK min. v15 (API 15: Android 4.0.3 IceCreamSandwich), Language: Kotlin v1.3.61
- VIRTUAL DEVICE ANDROID STUDIO PHONE: Pixel size: 5.0, Resolution: 1080x1920px, Density: 420dpi, Startup orientation: portrait
-
## Import and execute the Project in Android Studio
1 - Clone the weather-climate project into your repository folder:

```sh
$ git clone https://github.com/thibeserra/weather-climate.git
```

2 - Open Android Studio IDE and import the cloned project:
![](app/src/readme/_1-open_android_studio.png)

- Go to your repository folder and choose the weather-climate project
![](app/src/readme/_2-select_project_into_repository.png)

- IDE will download the dependencies project and build it. Await this steps:
![](app/src/readme/_3-build_project_step.png)

3 - Let's create a virtual device to start the project
 - Click on AVD MANAGER icon in the right top IDE:
 ![](app/src/readme/_4-click_avd_manager_icon.png)

 - After open AVD MANAGER'S view, click on button CREATE VIRTUAL DEVICE:
 ![](app/src/readme/_5-create-virtual-device.png)

 - Choose in menu CATEGORY, the PHONE option. After, choose Nexus 5X virtual device and click in NEXT button:
 ![](app/src/readme/_6-nexus_5x_virtual_device.png)

 - In RECOMMENDED tab, download the image API Level 29, ABI x86, Target Android 10.0 (Google Play):
  ![](app/src/readme/_7-view-system-image-download.png)

  - After, wait the process download. This can take a long time, depending on the speed of your network:
  ![](app/src/readme/_8-download-device-image.png)

  - when the downlaod finish and unnzip the image, click on FINISH button:
  ![](app/src/readme/_9-finish-download-device-image.png)

  - Then, in SYSTEM IMAGE view, click on NEXT button:
  ![](app/src/readme/_10-next-button.png)

  - The next step is configure the virtual-device. In AVD NAME, show the device name or keep the default:
  ![](app/src/readme/_11-virtual-device-config.png)

  - On the same view, click on SHOW ADVANCED SETTINGS button and check the BOOT OPTION='Cold boot' and click in FINISH button:
  ![](app/src/readme/_12-show-adv-settings.png)

  - At this moment, the virtual devid is created:
  ![](app/src/readme/_13-virtual-device-created.png)

