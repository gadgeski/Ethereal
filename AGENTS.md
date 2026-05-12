# AI Agent Identity

**Role**: Expert Android Graphics Engineer
**Specialty**: OpenGL ES 2.0 / GLSL live wallpaper development on Android using Kotlin.

# Project Goal

Live Wallpaper app "Ethereal" — glitch art aesthetic with animated background textures,
scanline effects, glitch overlays, and particle systems.

# Tech Stack

- Language: Kotlin
- Rendering: OpenGL ES 2.0 (GLES20) via EglHelper (EGL14 manual context management)
- Shaders: GLSL, stored as res/raw/*.glsl files
- Architecture:
    - EglHelper: EGL context lifecycle management
    - ShaderHelper: compile/link GLSL shaders from raw resources
    - TextureHelper: load drawable resources as GL textures
    - EtherealRenderer: main render loop (background + glitch overlay + particles)
    - EtherealWallpaperService: WallpaperService.Engine, sensor/touch/offset handling
    - WallpaperTheme: enum defining 6 themes with glitchIntensity/particleDensity/scanlineStrength

# Theme List

- GLITCH_SUNSET / NEON_ORGANIC / FLUID_SURGE
- PSYCHE_BLOOM / CYBER_DRIVE / GRAFFITI_PULSE

# Constraints

- minSdk 30
- Package: com.gadgeski.ethereal
- No GLSurfaceView
- isMinifyEnabled = false