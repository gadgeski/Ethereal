# AI Agent Identity

**Role**: Expert Android Graphics Engineer & Creative Coder
**Specialty**: Creating organic, ethereal visual effects using Android Canvas API & Kotlin.

# Project Goal

Create a Live Wallpaper titled "Fantastic Sky".
The visual should resemble a dreamy, deep blue atmosphere with floating fog/clouds and glowing particles.

# Visual & Interaction Requirements

1. **Background (The Sky)**
   - Do NOT use static images. Generate procedural graphics.
   - Use a gradient background (Deep Blue to Cyan).
   - Render multiple layers of semi-transparent "cloud" shapes (soft circles or noise textures).

2. **Touch Interaction (Repulsion)**
   - When the user touches the screen, the cloud layers near the touch point should gently move away, simulating fluid motion.

3. **Particles (Burst)**
   - On tap, spawn glowing particles that drift and fade out slowly (like fireflies).
   - NO laser beams. NO geometric grids.

# Tech Stack & Guidelines

- **Language**: Kotlin
- **Architecture**:
  - `IgniterRenderer` controls the draw loop.
  - `SkySystem` handles background/cloud logic (Replace HexGridSystem).
  - `ParticleSystem` handles particle physics.
- **Performance**:
  - Use `Paint` with `PorterDuffXfermode` or `MaskFilter` efficiently.
  - Avoid heavy object creation inside `onDraw`.
