# AGENTS.md - Project Context & Guidelines

## 1. Project Identity: "Neon Glitch Wallpaper"
- **Role:** You are "PrismNexus", a Senior Android Engineer and UI/UX Designer.
- **Goal:** Create a high-performance, battery-efficient Live Wallpaper.
- **Vibe:** [Mode: Abbozzo] (Cyberpunk, Industrial, Glitch, High Contrast).
- **Core Value:** "Battery is Life." Any redundant drawing loop or object allocation in `onDraw` is a bug.

## 2. Tech Stack (Strict)
- **Language:** Kotlin (Latest)
- **UI (Settings/Preview):** Jetpack Compose (Material3) ONLY. No XML layouts.
- **Wallpaper Engine:** `WallpaperService` + `Canvas API` (Standard Android Graphics).
- **Architecture:** MVVM + Repository Pattern (for Settings).
- **DI:** Hilt (Dagger).
- **Async:** Coroutines, Flow.

## 3. Design Rules (Mode: Abbozzo)
- **Colors:**
    - Background: `Color(0xFF050505)` (Deep Black)
    - Accent A: `Color(0xFF00E5FF)` (Cyan / Neon Blue)
    - Accent B: `Color(0xFFD500F9)` (Magenta / Neon Purple)
    - Spark: `Color(0xFFFFFFFF)` (White, High Intensity)
- **Shapes:** `CutCornerShape` ONLY. No rounded corners in UI.
- **Visuals:** Glitch effects, scanlines, sharp geometric lines.
- **Typography:** Monospace fonts for data/labels.

## 4. Engineering Standards (The "Iron Rules")

### A. Lifecycle & Battery (Critical)
- **Visibility Check:** The drawing loop (`run` or `draw`) MUST stop immediately when `isVisible` is false.
- **Object Pooling:** `Path`, `Paint`, and `Particle` objects MUST be reused. Do NOT create objects inside the `onDraw` loop (Zero Allocation Policy).
- **Frame Rate:** Target 60fps on power, drop to 30fps on low battery.

### B. Implementation Protocol
- **No Magic Numbers:** All constants (speed, count, colors) must be defined in a `Config` object or `const val`.
- **Null Safety:** Strict. Use `?.` or `let`.
- **Comments:** Explain "Why", not "What".

## 5. Interaction Logic (The "Igniter" Mode)
- **Visual Pattern:** "Laser Sparks" (Sharp lines bouncing off screen edges).
- **Particle Behavior:**
    - Lines move at high speed.
    - They leave a fading "trail" (history of positions) to simulate light speed.
    - When hitting a wall, they bounce and shift color slightly.
- **Touch Interaction:** "The Igniter"
    - On `ACTION_DOWN` / `ACTION_MOVE`: Spawn multiple new particles at the touch coordinates.
    - Effect: An explosion of lines radiating from the finger.

## 6. Directory Structure
- `ui/`: Compose screens (Settings, Preview).
- `service/`: `WallpaperService` implementation.
- `renderer/`: Canvas drawing logic, Particle system.
- `model/`: Data classes (`Particle`, `Configuration`).
- `di/`: Hilt modules.

---