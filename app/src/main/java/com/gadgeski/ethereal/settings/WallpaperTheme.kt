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
    );

    companion object {
        fun fromName(name: String?): WallpaperTheme {
            return entries.firstOrNull { it.name == name } ?: AZURE_SKY
        }
    }
}