package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val HyperEdgeColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = NavyLight,
    onPrimaryContainer = Color.White,
    secondary = OrangeAccent,
    onSecondary = Color.White,
    secondaryContainer = OrangeTint,
    onSecondaryContainer = NavyPrimary,
    tertiary = SlateSecondary,
    onTertiary = Color.White,
    background = CreamBackground,
    onBackground = NavyPrimary,
    surface = CardBackground,
    onSurface = NavyPrimary,
    surfaceVariant = SurfaceCream,
    onSurfaceVariant = SlateSecondary,
    error = RedError,
    onError = Color.White,
    errorContainer = RedTint,
    onErrorContainer = RedError,
    outline = CardBorderNavy,
    outlineVariant = SlateLight
)

// Small 4pt/4dp corner radius mandated by design system
val HyperEdgeShapes = Shapes(
    extraSmall = RoundedCornerShape(2.dp),
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(6.dp),
    extraLarge = RoundedCornerShape(8.dp)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HyperEdgeColorScheme,
        typography = Typography,
        shapes = HyperEdgeShapes,
        content = content
    )
}
