package com.gadgeski.ethereal.settings

import androidx.annotation.DrawableRes
import com.gadgeski.ethereal.R

enum class WallpaperTheme(
    val displayName: String,
    val description: String,
    @field:DrawableRes val backgroundDrawableRes: Int,
    @field:DrawableRes val thumbnailDrawableRes: Int,
    val glitchIntensity: Float,
    val particleDensity: Float,
    val scanlineStrength: Float
) {
    AZURE_SKY(
        displayName = "Azure Sky",
        description = "A fantastical sky of towering clouds and blue light.",
        backgroundDrawableRes = R.drawable.bg_azure_sky,
        thumbnailDrawableRes = R.drawable.bg_azure_sky,
        glitchIntensity = 0.6f,
        particleDensity = 0.4f,
        scanlineStrength = 0.04f
    ),

    RAINY_WINDOW(
        displayName = "Rainy Window",
        description = "A doodle left on a fogged window on a rainy day.",
        backgroundDrawableRes = R.drawable.bg_rainy_window,
        thumbnailDrawableRes = R.drawable.bg_rainy_window,
        glitchIntensity = 0.4f,
        particleDensity = 0.3f,
        scanlineStrength = 0.02f
    ),

    CHILL_AQUARIUM(
        displayName = "Chill Aquarium",
        description = "Deep blue waters glowing behind curved glass.",
        backgroundDrawableRes = R.drawable.bg_chill_aquarium,
        thumbnailDrawableRes = R.drawable.bg_chill_aquarium,
        glitchIntensity = 0.3f,
        particleDensity = 0.3f,
        scanlineStrength = 0.03f
    ),

    INDIGO_GRAIN(
        displayName = "Indigo Grain",
        description = "Organic wood grain flowing in deep indigo.",
        backgroundDrawableRes = R.drawable.bg_indigo_grain,
        thumbnailDrawableRes = R.drawable.bg_indigo_grain,
        glitchIntensity = 0.5f,
        particleDensity = 0.4f,
        scanlineStrength = 0.05f
    ),

    COBALT_PAINT(
        displayName = "Cobalt Paint",
        description = "Cobalt strokes splashed across a dark canvas.",
        backgroundDrawableRes = R.drawable.bg_cobalt_paint,
        thumbnailDrawableRes = R.drawable.bg_cobalt_paint,
        glitchIntensity = 0.5f,
        particleDensity = 0.6f,
        scanlineStrength = 0.03f
    ),

    HALFTONE_CURVE(
        displayName = "Halftone Curve",
        description = "Vivid halftone dots sweeping through blue curves.",
        backgroundDrawableRes = R.drawable.bg_halftone_curve,
        thumbnailDrawableRes = R.drawable.bg_halftone_curve,
        glitchIntensity = 0.75f,
        particleDensity = 0.5f,
        scanlineStrength = 0.06f
    ),

    MINT_WAVE(
        displayName = "Mint Wave",
        description = "Soft mint curves drifting over a midnight field.",
        backgroundDrawableRes = R.drawable.bg_mint_wave,
        thumbnailDrawableRes = R.drawable.bg_mint_wave,
        glitchIntensity = 0.3f,
        particleDensity = 0.3f,
        scanlineStrength = 0.02f
    ),

    MONO_FRACTURE(
        displayName = "Mono Fracture",
        description = "A quiet room fractured into stepped monochrome blocks.",
        backgroundDrawableRes = R.drawable.bg_mono_fracture,
        thumbnailDrawableRes = R.drawable.bg_mono_fracture,
        glitchIntensity = 0.35f,
        particleDensity = 0.2f,
        scanlineStrength = 0.01f
    ),

    AZURE_FRACTURE(
        displayName = "Azure Fracture",
        description = "An open sky fractured into stepped blocks of blue.",
        backgroundDrawableRes = R.drawable.bg_azure_fracture,
        thumbnailDrawableRes = R.drawable.bg_azure_fracture,
        glitchIntensity = 0.35f,
        particleDensity = 0.2f,
        scanlineStrength = 0.01f
    ),

    QUIET_STREET(
        displayName = "Quiet Street",
        description = "A residential street after dark, lit only by a few windows.",
        backgroundDrawableRes = R.drawable.bg_quiet_street,
        thumbnailDrawableRes = R.drawable.bg_quiet_street,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    DISTANT_LIGHTS(
        displayName = "Distant Lights",
        description = "A town seen from above, long after sunset.",
        backgroundDrawableRes = R.drawable.bg_distant_lights,
        thumbnailDrawableRes = R.drawable.bg_distant_lights,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    WINDOW_VIGIL(
        displayName = "Window Vigil",
        description = "The town after dark, seen through a window frame.",
        backgroundDrawableRes = R.drawable.bg_window_vigil,
        thumbnailDrawableRes = R.drawable.bg_window_vigil,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    LONE_LAMP(
        displayName = "Lone Lamp",
        description = "A single street lamp holding back the dark.",
        backgroundDrawableRes = R.drawable.bg_lone_lamp,
        thumbnailDrawableRes = R.drawable.bg_lone_lamp,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    CROSSROAD_DUSK(
        displayName = "Crossroad Dusk",
        description = "An empty intersection under a single lamp.",
        backgroundDrawableRes = R.drawable.bg_crossroad_dusk,
        thumbnailDrawableRes = R.drawable.bg_crossroad_dusk,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    HILLSIDE_VIEW(
        displayName = "Hillside View",
        description = "The valley spread out below a hillside stairway.",
        backgroundDrawableRes = R.drawable.bg_hillside_view,
        thumbnailDrawableRes = R.drawable.bg_hillside_view,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    BALCONY_NIGHT(
        displayName = "Balcony Night",
        description = "String lights on a balcony, facing the neighbors' roofs.",
        backgroundDrawableRes = R.drawable.bg_balcony_night,
        thumbnailDrawableRes = R.drawable.bg_balcony_night,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    LAST_TRAIN(
        displayName = "Last Train",
        description = "Hillside houses passing by the window of a night train.",
        backgroundDrawableRes = R.drawable.bg_last_train,
        thumbnailDrawableRes = R.drawable.bg_last_train,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    AFTER_RAIN(
        displayName = "After Rain",
        description = "A wet path holding what little light is left.",
        backgroundDrawableRes = R.drawable.bg_after_rain,
        thumbnailDrawableRes = R.drawable.bg_after_rain,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    CANYON_SKY(
        displayName = "Canyon Sky",
        description = "A strip of sky between towers.",
        backgroundDrawableRes = R.drawable.bg_canyon_sky,
        thumbnailDrawableRes = R.drawable.bg_canyon_sky,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    FOG_VALLEY(
        displayName = "Fog Valley",
        description = "Fog settling between ridges of pine.",
        backgroundDrawableRes = R.drawable.bg_fog_valley,
        thumbnailDrawableRes = R.drawable.bg_fog_valley,
        glitchIntensity = 0.15f,
        particleDensity = 0.2f,
        scanlineStrength = 0.02f
    ),

    STATION_BELOW(
        displayName = "Station Below",
        description = "Rails curving through a town, seen from the hill above.",
        backgroundDrawableRes = R.drawable.bg_station_below,
        thumbnailDrawableRes = R.drawable.bg_station_below,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    CONDENSATION(
        displayName = "Condensation",
        description = "City lights dissolving behind a fogged pane.",
        backgroundDrawableRes = R.drawable.bg_condensation,
        thumbnailDrawableRes = R.drawable.bg_condensation,
        glitchIntensity = 0.15f,
        particleDensity = 0.2f,
        scanlineStrength = 0.02f
    ),
    
    LIGHT_LEAK(
        displayName = "Light Leak",
        description = "An alley of fire escapes, burned by stray film light.",
        backgroundDrawableRes = R.drawable.bg_light_leak,
        thumbnailDrawableRes = R.drawable.bg_light_leak,
        glitchIntensity = 0.25f,
        particleDensity = 0.15f,
        scanlineStrength = 0.03f
    ),

    TATAMI_ROOM(
        displayName = "Tatami Room",
        description = "Afternoon light through shoji, a table set for tea.",
        backgroundDrawableRes = R.drawable.bg_tatami_room,
        thumbnailDrawableRes = R.drawable.bg_tatami_room,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    SEASIDE_SHELTER(
        displayName = "Seaside Shelter",
        description = "A bag left on a bench, facing the open sea.",
        backgroundDrawableRes = R.drawable.bg_seaside_shelter,
        thumbnailDrawableRes = R.drawable.bg_seaside_shelter,
        glitchIntensity = 0.2f,
        particleDensity = 0.15f,
        scanlineStrength = 0.02f
    ),

    COASTAL_DUSK(
        displayName = "Coastal Dusk",
        description = "A seaside platform at sunset, rails curving out of sight.",
        backgroundDrawableRes = R.drawable.bg_coastal_dusk,
        thumbnailDrawableRes = R.drawable.bg_coastal_dusk,
        glitchIntensity = 0.25f,
        particleDensity = 0.2f,
        scanlineStrength = 0.03f
    ),

    PORCH_SUNSET(
        displayName = "Porch Sunset",
        description = "A mug left on the rail as the bay turns orange.",
        backgroundDrawableRes = R.drawable.bg_porch_sunset,
        thumbnailDrawableRes = R.drawable.bg_porch_sunset,
        glitchIntensity = 0.25f,
        particleDensity = 0.2f,
        scanlineStrength = 0.03f
    ),

    GOLDEN_FIELD(
        displayName = "Golden Field",
        description = "Tall grass catching the last of the sun.",
        backgroundDrawableRes = R.drawable.bg_golden_field,
        thumbnailDrawableRes = R.drawable.bg_golden_field,
        glitchIntensity = 0.25f,
        particleDensity = 0.25f,
        scanlineStrength = 0.03f
    ),

    HARBOR_WINDOW(
        displayName = "Harbor Window",
        description = "An open shutter over a bay at golden hour.",
        backgroundDrawableRes = R.drawable.bg_harbor_window,
        thumbnailDrawableRes = R.drawable.bg_harbor_window,
        glitchIntensity = 0.25f,
        particleDensity = 0.2f,
        scanlineStrength = 0.03f
    ),

    WET_LANE(
        displayName = "Wet Lane",
        description = "A narrow lane still wet, catching the last sun.",
        backgroundDrawableRes = R.drawable.bg_wet_lane,
        thumbnailDrawableRes = R.drawable.bg_wet_lane_thumb,
        glitchIntensity = 0.25f,
        particleDensity = 0.2f,
        scanlineStrength = 0.03f
    );

    companion object {
        fun fromName(name: String?): WallpaperTheme {
            return entries.firstOrNull { it.name == name } ?: AZURE_SKY
        }
    }
}