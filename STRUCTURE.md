```
Ethereal/
├── AGENTS.md
├── Readme.md
├── STRUCTURE.md
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
│       ├── androidTest
│       │   └── java
│       │       └── com
│       │           └── gadgeski
│       │               └── ethereal
│       │                   └── ExampleInstrumentedTest.kt
│       ├── main
│       │   ├── AndroidManifest.xml
│       │   ├── ic_launcher-playstore.png
│       │   ├── java
│       │   │   └── com
│       │   │       └── gadgeski
│       │   │           └── ethereal
│       │   │               ├── EtherealApp.kt
│       │   │               ├── EtherealWallpaperService.kt
│       │   │               ├── MainActivity.kt
│       │   │               ├── opengl
│       │   │               │   ├── EglHelper.kt
│       │   │               │   ├── ShaderHelper.kt
│       │   │               │   └── TextureHelper.kt
│       │   │               ├── renderer
│       │   │               │   └── EtherealGLRenderer.kt
│       │   │               ├── settings
│       │   │               │   ├── SettingsActivity.kt
│       │   │               │   └── WallpaperTheme.kt
│       │   │               └── ui
│       │   │                   └── theme
│       │   │                       ├── Color.kt
│       │   │                       ├── Theme.kt
│       │   │                       └── Type.kt
│       │   └── res
│       │       ├── drawable
│       │       │   ├── bg_after_rain.webp
│       │       │   ├── bg_after_rain_thumb.webp
│       │       │   ├── bg_azure_fracture.webp
│       │       │   ├── bg_azure_fracture_thumb.webp
│       │       │   ├── bg_azure_sky.webp
│       │       │   ├── bg_azure_sky_thumb.webp
│       │       │   ├── bg_balcony_night.webp
│       │       │   ├── bg_balcony_night_thumb.webp
│       │       │   ├── bg_canyon_sky.webp
│       │       │   ├── bg_canyon_sky_thumb.webp
│       │       │   ├── bg_chill_aquarium.webp
│       │       │   ├── bg_chill_aquarium_thumb.webp
│       │       │   ├── bg_coastal_dusk.webp
│       │       │   ├── bg_coastal_dusk_thumb.webp
│       │       │   ├── bg_cobalt_paint.webp
│       │       │   ├── bg_cobalt_paint_thumb.webp
│       │       │   ├── bg_condensation.webp
│       │       │   ├── bg_condensation_thumb.webp
│       │       │   ├── bg_crossroad_dusk.webp
│       │       │   ├── bg_crossroad_dusk_thumb.webp
│       │       │   ├── bg_distant_lights.webp
│       │       │   ├── bg_distant_lights_thumb.webp
│       │       │   ├── bg_fog_valley.webp
│       │       │   ├── bg_fog_valley_thumb.webp
│       │       │   ├── bg_golden_field.webp
│       │       │   ├── bg_golden_field_thumb.webp
│       │       │   ├── bg_halftone_curve.webp
│       │       │   ├── bg_halftone_curve_thumb.webp
│       │       │   ├── bg_harbor_window.webp
│       │       │   ├── bg_harbor_window_thumb.webp
│       │       │   ├── bg_hillside_view.webp
│       │       │   ├── bg_hillside_view_thumb.webp
│       │       │   ├── bg_indigo_grain.webp
│       │       │   ├── bg_indigo_grain_thumb.webp
│       │       │   ├── bg_last_train.webp
│       │       │   ├── bg_last_train_thumb.webp
│       │       │   ├── bg_light_leak.webp
│       │       │   ├── bg_light_leak_thumb.webp
│       │       │   ├── bg_lone_lamp.webp
│       │       │   ├── bg_lone_lamp_thumb.webp
│       │       │   ├── bg_mint_wave.webp
│       │       │   ├── bg_mint_wave_thumb.webp
│       │       │   ├── bg_mono_fracture.webp
│       │       │   ├── bg_mono_fracture_thumb.webp
│       │       │   ├── bg_porch_sunset.webp
│       │       │   ├── bg_porch_sunset_thumb.webp
│       │       │   ├── bg_quiet_street.webp
│       │       │   ├── bg_quiet_street_thumb.webp
│       │       │   ├── bg_rainy_window.webp
│       │       │   ├── bg_rainy_window_thumb.webp
│       │       │   ├── bg_seaside_shelter.webp
│       │       │   ├── bg_seaside_shelter_thumb.webp
│       │       │   ├── bg_station_below.webp
│       │       │   ├── bg_station_below_thumb.webp
│       │       │   ├── bg_tatami_room.webp
│       │       │   ├── bg_tatami_room_thumb.webp
│       │       │   ├── bg_wet_lane.webp
│       │       │   ├── bg_wet_lane_thumb.webp
│       │       │   ├── bg_window_vigil.webp
│       │       │   ├── bg_window_vigil_thumb.webp
│       │       │   ├── ic_launcher_background.xml
│       │       │   └── ic_launcher_foreground.xml
│       │       ├── mipmap-anydpi
│       │       ├── mipmap-anydpi-v26
│       │       │   ├── ic_launcher.xml
│       │       │   └── ic_launcher_round.xml
│       │       ├── mipmap-hdpi
│       │       │   ├── ic_launcher.webp
│       │       │   ├── ic_launcher_foreground.webp
│       │       │   ├── ic_launcher_monochrome.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-mdpi
│       │       │   ├── ic_launcher.webp
│       │       │   ├── ic_launcher_foreground.webp
│       │       │   ├── ic_launcher_monochrome.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   ├── ic_launcher_foreground.webp
│       │       │   ├── ic_launcher_monochrome.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   ├── ic_launcher_foreground.webp
│       │       │   ├── ic_launcher_monochrome.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxxhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   ├── ic_launcher_foreground.webp
│       │       │   ├── ic_launcher_monochrome.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── raw
│       │       │   ├── bg_fragment.glsl
│       │       │   ├── bg_vertex.glsl
│       │       │   ├── glitch_fragment.glsl
│       │       │   ├── glitch_vertex.glsl
│       │       │   ├── particle_fragment.glsl
│       │       │   └── particle_vertex.glsl
│       │       ├── values
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── xml
│       │           ├── backup_rules.xml
│       │           ├── data_extraction_rules.xml
│       │           └── wallpaper.xml
│       └── test
│           └── java
│               └── com
│                   └── gadgeski
│                       └── ethereal
│                           └── ExampleUnitTest.kt
├── build.gradle.kts
├── gradle
│   ├── libs.versions.toml
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle.kts

37 directories, 126 files
```
