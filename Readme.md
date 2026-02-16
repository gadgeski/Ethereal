# Ethereal - Live Wallpaper Engine

> **"Touch the Void."**
>
> A programmable artwork that renders a living, breathing ethereal world directly on your Android home screen.

## 📱 Overview

**Ethereal** is a high-performance Android Live Wallpaper application built entirely with **Kotlin** and native **Android Canvas API**.

Unlike typical video-loop wallpapers, Ethereal renders every frame in real-time, simulating fluid dynamics for fog, particle physics for light, and multi-layer parallax for depth. It transforms the static home screen into an immersive "DailySync" experience—calm, intelligent, and reactive.

## ✨ Key Features

### 1. Multi-Layer Parallax Depth

Implemented a custom parallax engine that moves layers at different velocities relative to the scroll offset, creating a genuine 3D illusion.

- **Background (Sky):** Moves at **30%** speed (Factor 0.3) for stability.

- **Midground (Fog):** Moves at **50%** speed (Factor 0.5) with seamless wrap-around logic.

- **Foreground (Particles):** Moves at **120%** speed (Factor 1.2), creating a dynamic "pop-out" effect.


### 2. Physics-Based Interaction

The fog isn't just an animation; it's a simulation.

- **Fluid Repulsion:** Fog particles react to touch input, gently parting ways based on vector calculations.

- **Inertia & Damping:** Objects carry velocity and slow down naturally due to simulated air resistance (damping factor 0.92).


### 3. Reactive Particle System

- **Touch Ignition:** Spawns bursts of light particles exactly at the touch coordinates (corrected for parallax shift).

- **Dynamic Pooling:** Manages a pool of **2,000+ particles** efficiently, ensuring zero frame drops even during intense interaction.


## 🛠 Technical Highlights

- **Pure Canvas Rendering:** No heavy game engines (Unity/Unreal) or OpenGL dependencies. Lightweight and battery-efficient.

- **Memory Optimization:** Uses a single `Bitmap` for the background and recycles objects to prevent Garbage Collection (GC) pauses.

- **Mathematical Precision:** - Custom coordinate transformation logic to map screen touch points to the parallax-shifted world.

    - `Canvas.scale` and `translate` optimization to prevent background clipping on wide scrolls.


## 📂 Architecture

The project follows a clean separation of concerns, ensuring maintainability and scalability.

```
com.gadgeski.ethereal
├── EtherealWallpaperService.kt  # Entry Point (Service)
└── renderer/
    ├── EtherealRenderer.kt      # Main Render Loop & State Manager
    ├── SkySystem.kt             # Background Layer (Bitmap & Parallax)
    ├── FogSystem.kt             # Physics Layer (Repulsion Logic)
    └── ParticleSystem.kt        # Foreground Layer (Pooling & Ignition)
```

## 🚀 Getting Started

1. Clone the repository.

    ```
    git clone [https://github.com/gadgeski/Ethereal.git](https://github.com/gadgeski/Ethereal.git)
    ```

2. Open in **Android Studio**.

3. Build and Run on a physical device (Emulators may not support Live Wallpapers fully).

4. Select "Ethereal" from the Wallpaper picker.

## 🎨 Design Philosophy

- **Mode:** DailySync (Lifestyle/Cafe)

- **Palette:** Deep Purple, Sunset Orange, Cyan & White Glitch accents.

- **Concept:** A window to a digital ether that bridges the gap between the organic and the synthetic.


Designed & Engineered by **Gemini customized with Gem**.