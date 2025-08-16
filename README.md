# 📍 Background Location Tracking Service (Android – Kotlin)

This project demonstrates how to implement a **Foreground Service** in Android that continuously tracks the device’s location — even when the app is minimized or closed from recent tasks.

---

## 🚀 Features
- ✅ Uses **FusedLocationProviderClient** for efficient & accurate location updates  
- ✅ Runs as a **Foreground Service** with a persistent notification  
- ✅ Compatible with **latest LocationRequest APIs** (Android 12+)  
- ✅ Properly stops updates when service is destroyed  

---


## Screenshots

<div style="display: flex; gap: 10px;">
  <img src="https://github.com/user-attachments/assets/c60a6ea3-abc7-4daf-8359-bc423e21eab8" alt="Screenshot 1" width="250"/>
  <img src="https://github.com/user-attachments/assets/4ba5bb2e-e10f-40ff-9047-ab10f639add7" alt="Screenshot 3" width="250"/>
  <img src="https://github.com/user-attachments/assets/31430630-9fd0-415e-832b-5729b45b146c" alt="Screenshot 2" width="250"/>
</div>



---

## 🛠️ Tech Stack
- **Kotlin**  
- **Foreground Service**  
- **Fused Location Provider API**  

---

## 📌 Core Code

### 1️⃣ Create Location Request (new API)
```kotlin
val locationRequest = LocationRequest.Builder(
    Priority.PRIORITY_HIGH_ACCURACY,
    5000L // interval
).setMinUpdateIntervalMillis(2000L) // fastest interval
 .build()
