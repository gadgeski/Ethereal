# Ethereal — Live Wallpaper Engine

> **"Touch the Glitch."**
>
> An OpenGL ES live wallpaper that renders lo-fi glitch aesthetics in real time on your Android home screen.

## 📱 Overview

**Ethereal** is an Android live wallpaper built with **Kotlin** and **OpenGL ES 2.0**.

Rather than looping a video, every frame is rendered on the GPU through custom GLSL shaders. Scanlines, sporadic glitch bursts, and drifting particles are layered over curated background artwork to create a calm, lo-fi atmosphere that reacts to touch and device motion.

## ✨ Key Features

### 1. Three-Layer GPU Rendering

Each frame composites three independent shader programs.

- **Background:** Texture sampling with scanlines, RGB channel shift, and horizontal band displacement.
- **Glitch Overlay:** Block noise, scanline tearing, and touch-driven ripple distortion.
- **Particles:** `GL_POINTS` batched by color, with gravity-driven motion.

### 2. Per-Theme Motion Profiles

Every theme carries its own parameters, so the same shaders produce distinctly different moods.

- `glitchIntensity` — strength of glitch and RGB shift
- `particleDensity` — spawn rate and burst size
- `scanlineStrength` — scanline contrast

Calm themes such as *Chill Aquarium* stay near 0.3, while *Halftone Curve* pushes to 0.75.

### 3. Sensor & Touch Reactivity

- **Touch Ignition:** Particle bursts spawn at the touch point; the glitch shader adds a localized ripple.
- **Accelerometer Drift:** Particles respond to device tilt through the gravity vector.
- **Parallax:** Home screen scroll offsets shift the background texture.

## 🛠 Technical Highlights

- **Manual EGL Management:** `WallpaperService.Engine` cannot host a `GLSurfaceView`, so the EGL14 context, surface, and lifecycle are handled directly.
- **Single GL Thread:** A dedicated single-thread dispatcher serializes all GL work — context creation, texture upload, draw calls, and teardown — through coroutines.
- **Shaders as Resources:** GLSL lives in `res/raw/*.glsl` and is compiled at theme-switch time, keeping rendering logic out of Kotlin.
- **Live Theme Switching:** A `SharedPreferences` listener applies theme changes the moment they are selected, with no service restart.

## 📂 Architecture

```text
com.gadgeski.ethereal
├── EtherealWallpaperService.kt   # Service, EGL lifecycle, draw loop
├── MainActivity.kt               # Entry screen
├── opengl/
│   ├── EglHelper.kt              # EGL14 context & surface management
│   ├── ShaderHelper.kt           # GLSL compile & link
│   └── TextureHelper.kt          # Drawable → GL texture
├── renderer/
│   └── EtherealGLRenderer.kt     # Three-layer render pipeline
└── settings/
    ├── WallpaperTheme.kt         # Theme definitions & parameters
    └── SettingsActivity.kt       # Theme picker (Compose)
```

```text
res/raw/
├── bg_vertex.glsl / bg_fragment.glsl
├── glitch_vertex.glsl / glitch_fragment.glsl
└── particle_vertex.glsl / particle_fragment.glsl
```

## 🎨 Themes

| Theme | Mood |
|---|---|
| Azure Sky | Towering clouds in blue light |
| Rainy Window | A doodle on fogged glass |
| Chill Aquarium | Deep water behind curved glass |
| Indigo Grain | Organic grain in deep indigo |
| Cobalt Paint | Cobalt strokes on dark canvas |
| Halftone Curve | Vivid dots through blue curves |
| Mint Wave | Mint curves over a midnight field |

## 🚀 Getting Started

1. Clone the repository.

   ```bash
   git clone https://github.com/gadgeski/Ethereal.git
   ```

2. Open in **Android Studio**.
3. Build and run on a physical device (live wallpapers are unreliable on emulators).
4. Select **Ethereal** from the wallpaper picker.

## 🎭 Design Philosophy

- **Aesthetic:** Lo-fi / glitch — noise as texture, never as spectacle.
- **Palette:** Deep blue, cobalt, cyan, with mint and magenta accents.
- **Principle:** Glitch is seasoning. If it announces itself, the atmosphere is already broken.

## 🔧 Requirements

- Android 11 (API 30) or higher
- OpenGL ES 2.0 support