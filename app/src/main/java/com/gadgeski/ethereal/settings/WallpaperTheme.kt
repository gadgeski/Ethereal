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
    GLITCH_SUNSET(
        displayName = "Glitch Sunset",
        description = "Warm sunset sky fractured by analog glitch.",
        backgroundDrawableRes = R.drawable.ethereal_bg,
        thumbnailDrawableRes = R.drawable.ethereal_bg,
        glitchIntensity = 0.6f,
        particleDensity = 0.4f,
        scanlineStrength = 0.04f
    ),

    NEON_ORGANIC(
        displayName = "Neon Organic",
        description = "Dark wood grain alive with neon circuit light.",
        backgroundDrawableRes = R.drawable.bg_wood_grain_abstract,
        thumbnailDrawableRes = R.drawable.bg_wood_grain_abstract,
        glitchIntensity = 0.8f,
        particleDensity = 0.6f,
        scanlineStrength = 0.05f
    ),

    FLUID_SURGE(
        displayName = "Fluid Surge",
        description = "Magenta flow fields surging with electric energy.",
        backgroundDrawableRes = R.drawable.bg_wavy_abstract,
        thumbnailDrawableRes = R.drawable.bg_wavy_abstract,
        glitchIntensity = 0.5f,
        particleDensity = 0.8f,
        scanlineStrength = 0.03f
    ),

    PSYCHE_BLOOM(
        displayName = "Psyche Bloom",
        description = "Psychedelic graffiti flower with chromatic chaos.",
        backgroundDrawableRes = R.drawable.bg_flower_comical_abstract,
        thumbnailDrawableRes = R.drawable.bg_flower_comical_abstract,
        glitchIntensity = 1.0f,
        particleDensity = 1.0f,
        scanlineStrength = 0.02f
    ),

    CYBER_DRIVE(
        displayName = "Cyber Drive",
        description = "Highway through a glitched neon city.",
        backgroundDrawableRes = R.drawable.bg_glitch_highway,
        thumbnailDrawableRes = R.drawable.bg_glitch_highway,
        glitchIntensity = 0.9f,
        particleDensity = 0.5f,
        scanlineStrength = 0.06f
    ),

    GRAFFITI_PULSE(
        displayName = "Graffiti Pulse",
        description = "Street art exploding with glitch and color.",
        backgroundDrawableRes = R.drawable.bg_graffiti_abstract,
        thumbnailDrawableRes = R.drawable.bg_graffiti_abstract,
        glitchIntensity = 0.7f,
        particleDensity = 0.7f,
        scanlineStrength = 0.04f
    ),
    
    STATION_GLITCH(
        displayName = "Station Glitch",
        description = "A rain-soaked platform lost in analog interference.",
        backgroundDrawableRes = R.drawable.bg_vivid_abstract,
        thumbnailDrawableRes = R.drawable.bg_vivid_abstract,
        glitchIntensity = 0.75f,
        particleDensity = 0.5f,
        scanlineStrength = 0.06f
    );

    companion object {
        fun fromName(name: String?): WallpaperTheme {
            return entries.firstOrNull { it.name == name } ?: GLITCH_SUNSET
        }
    }
}