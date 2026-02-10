# Igniter ⚡️

A high-performance, cyberpunk-inspired Android Live Wallpaper application featuring interactive particle physics and a "breathing" hexagonal grid system.

## ✨ Features

* **Interactive Particle System:** Tap to ignite thousands of "Laser Sparks" that react to physics and collide with screen boundaries.
* **"Limit Break" Performance:** Optimized to handle 1,000+ active particles at 60fps using object pooling.
* **Ambient Background:** A "breathing" hexagonal grid (Honeycomb) that adds depth and cyberpunk aesthetics without draining the battery.
* **User Friendly:** Simple activation via a dedicated "Set Wallpaper" UI (Jetpack Compose).

## 🛠 Tech Stack & Architecture

* **Language:** Kotlin
* **Graphics:** Android Canvas API (Custom SurfaceView)
* **DI:** Hilt (Dagger)
* **UI:** Jetpack Compose (MainActivity)
* **Concurrency:** Kotlin Coroutines

## 🚀 Engineering Highlights

### 1. Zero Allocation Policy (Performance)
To prevent Garbage Collection (GC) stuttering during the drawing loop:
* **Object Pooling:** `Particle` objects are reused from a fixed-size pool. No `new` allocations occur in the `onDraw` loop.
* **Path Caching:** The complex hexagonal grid path is calculated **only once** upon surface resize and cached. The `draw()` method only handles rendering and alpha pulsing.

### 2. Architecture
* **Separation of Concerns:** The drawing logic (`IgniterRenderer`) is completely decoupled from the Android lifecycle service (`IgniterWallpaperService`).
* **Battery Efficiency:** The drawing loop automatically stops when the screen is invisible or locked.

## 📦 Installation

1.  Clone this repository.
2.  Open in Android Studio.
3.  Build and Run on a device/emulator.
4.  Tap **"Set Wallpaper"** on the launch screen.

## 🎨 Design Philosophy (Mode: Abbozzo)

* **Chaos vs. Order:** The chaotic movement of particles contrasts with the structured, rhythmic breathing of the background grid.
* **Visual Depth:** Thicker grid lines with low opacity create a sense of depth, ensuring the foreground particles pop.