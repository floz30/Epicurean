package fr.floz.epicurean.utils

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.scale

/**
 *
 * @see <a>https://medium.com/@kappdev/how-to-create-a-pulse-effect-in-jetpack-compose-265d49aad044</a>
 *
 * @param targetScale specifies the scale to which the pulse effect will grow
 * @param initialScale defines the starting scale of the pulse effect
 * @param brush the brush used ti fill the pulse effect
 * @param shape the shape of the pulse effect
 * @param animationSpec the animation specification that controls the effect
 */
@Composable
fun Modifier.pulseEffect(
    targetScale: Float = 1.5f,
    initialScale: Float = 1f,
    brush: Brush = SolidColor(Color.Black.copy(0.32f)),
    shape: Shape = CircleShape,
    animationSpec: DurationBasedAnimationSpec<Float> = tween(1200)
): Modifier {
    val pulseTransition = rememberInfiniteTransition("PulseTransition")

    val pulseScale by pulseTransition.animateFloat(
        initialValue = initialScale,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(animationSpec),
        label = "PulseScale"
    )

    val pulseAlpha by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(animationSpec),
        label = "PulseAlpha"
    )

    return this.drawBehind {
        // Convert the shape into an Outline
        val outline = shape.createOutline(size, layoutDirection, this)
        // Apply the scale
        scale(pulseScale) {
            // Draw the shape outline
            drawOutline(outline, brush, pulseAlpha)
        }
    }
}


/**
 * @see pulseEffect
 */
@Composable
fun Modifier.doublePulseEffect(
    targetScale: Float = 1.5f,
    initialScale: Float = 1f,
    brush: Brush = SolidColor(Color.Black.copy(0.32f)),
    shape: Shape = CircleShape,
    duration: Int = 1200,
): Modifier {
    return this
        .pulseEffect(
            targetScale, initialScale, brush, shape,
            animationSpec = tween(duration, easing = FastOutSlowInEasing)
        )
        .pulseEffect(
            targetScale, initialScale, brush, shape,
            animationSpec = tween(
                durationMillis = (duration * 0.7f).toInt(),
                delayMillis = (duration * 0.3f).toInt(),
                easing = LinearEasing
            )
        )
}