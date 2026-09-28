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

Quiet night scenes sit near 0.15–0.2 so the effects never overpower the image, while abstract themes push past 0.7.

### 3. Sensor & Touch Reactivity

- **Touch Ignition:** Particle bursts spawn at the touch point; the glitch shader adds a localized ripple.
- **Accelerometer Drift:** Particles respond to device tilt through the gravity vector.
- **Parallax:** Home screen scroll offsets shift the background texture.

### 4. Charge Scan

Plugging in a charger triggers a single scan band that sweeps from the bottom of the screen to the top over two seconds, then disappears. `ACTION_POWER_CONNECTED` is received at runtime and passed to the glitch shader as a progress uniform — no extra layer, no persistent animation.

## 🛠 Technical Highlights

- **Manual EGL Management:** `WallpaperService.Engine` cannot host a `GLSurfaceView`, so the EGL14 context, surface, and lifecycle are handled directly.
- **Single GL Thread:** A dedicated single-thread dispatcher serializes all GL work — context creation, texture upload, draw calls, and teardown — through coroutines.
- **Shaders as Resources:** GLSL lives in `res/raw/*.glsl` and is compiled at theme-switch time, keeping rendering logic out of Kotlin.
- **Live Theme Switching:** A `SharedPreferences` listener applies theme changes the moment they are selected, with no service restart.
- **Scaled Theme List:** As the catalog grew past twenty entries, the picker moved to `LazyColumn` so only visible rows are composed, and every theme gained a dedicated 298×660 thumbnail instead of decoding its full-size background for an 84dp cell.

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

Themes are grouped into four series. Each shares a treatment rather than a subject — the processing stays consistent while the source material varies.

### Abstract

Saturated blues and engineered textures. Halftone dots, wood grain, poured paint, aquarium glass. The original set, and the most openly synthetic.

_Azure Sky · Rainy Window · Chill Aquarium · Indigo Grain · Cobalt Paint · Halftone Curve · Mint Wave_

### Fracture

A photograph cut into stepped blocks, half of it withheld. Two pieces that share a composition and differ only in what lies behind the cut.

_Mono Fracture · Azure Fracture_

### Night

Monochrome scenes after dark, built on absence rather than color. A street, a window, a hillside, a train — lit by whatever happens to still be on.

_Quiet Street · Distant Lights · Window Vigil · Lone Lamp · Crossroad Dusk · Hillside View · Balcony Night · Last Train · After Rain · Canyon Sky · Station Below · Fog Valley_

### Warm

The counterweight. Amber, rose, and gold at the edges of the day, with enough shadow left in the lower frame to keep icons legible.

_Condensation · Light Leak · Tatami Room · Seaside Shelter · Coastal Dusk · Porch Sunset · Golden Field · Harbor Window · Wet Lane · Signal Dusk · Blurred Canyon_

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
- **Composition:** Light is earned by darkness around it. Most themes hold the lower third back so the home screen stays readable.
- **Principle:** Glitch is seasoning. If it announces itself, the atmosphere is already broken.

## 🔧 Requirements

- Android 11 (API 30) or higher
- OpenGL ES 2.0 support
