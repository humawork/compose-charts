package hu.ma.charts.pie

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.util.fastForEachIndexed
import hu.ma.charts.internal.FDEG2RAD
import hu.ma.charts.internal.FLOAT_EPSILON
import hu.ma.charts.internal.safeGet
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

private const val StartDegree = -90f

@Composable
internal fun PieChartRenderer(
  modifier: Modifier = Modifier,
  chartSizePx: Float,
  sliceWidthPx: Float,
  sliceSpacingPx: Float,
  fractions: List<Float>,
  composeColors: List<Color>,
  animate: Boolean,
) {
  var animationRan by rememberSaveable(fractions, animate) { mutableStateOf(false) }
  val animation = remember {
    Animatable(
      when {
        animate -> if (animationRan) 1f else 0f
        else -> 1f
      }
    )
  }
  val phase by animation.asState()

  LaunchedEffect(Unit) {
    if (!animationRan)
      animation.animateTo(1f, tween(325)) { animationRan = true }
  }

  val pathBuffer by remember { mutableStateOf(Path()) }
  val innerRectBuffer by remember { mutableStateOf(RectF()) }

  val holeRadius = remember(chartSizePx, sliceWidthPx) {
    (chartSizePx - (sliceWidthPx * 2f)) / 2f
  }

  Canvas(modifier = modifier) {
    val circleBox = RectF(0f, 0f, size.width, size.height)
    val nativeCanvas = drawContext.canvas.nativeCanvas

    var angle = 0f

    val radius = chartSizePx / 2f
    val drawInnerArc = sliceWidthPx > FLOAT_EPSILON && sliceWidthPx < chartSizePx / 2f
    val userInnerRadius = if (drawInnerArc) holeRadius else 0f

    val rotationAngle = StartDegree

    fractions.fastForEachIndexed { idx, sliceAngle ->
      val piecePaint = Paint().apply {
        color = composeColors.safeGet(idx).toArgb()
        isAntiAlias = true
      }
      var innerRadius = userInnerRadius

      val accountForSliceSpacing = sliceSpacingPx > 0f && sliceAngle <= 180f

      val sliceSpaceAngleOuter = sliceSpacingPx / (FDEG2RAD * radius)
      val startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2f) * phase
      val sweepAngleOuter = ((sliceAngle - sliceSpaceAngleOuter) * phase).coerceAtLeast(0f)

      pathBuffer.reset()

      val arcStartPointX = center.x + radius * cos(startAngleOuter * FDEG2RAD)
      val arcStartPointY = center.y + radius * sin(startAngleOuter * FDEG2RAD)

      if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= FLOAT_EPSILON) {
        // Android is doing "mod 360"
        pathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW)
      } else {
        pathBuffer.arcTo(
          circleBox,
          startAngleOuter,
          sweepAngleOuter
        )
      }

      innerRectBuffer.set(
        center.x - innerRadius,
        center.y - innerRadius,
        center.x + innerRadius,
        center.y + innerRadius
      )

      if (drawInnerArc && (innerRadius > 0f || accountForSliceSpacing)) {
        if (accountForSliceSpacing) {
          val minSpacedRadius = calculateMinimumRadiusForSpacedSlice(
            center,
            radius,
            sliceAngle * phase,
            arcStartPointX, arcStartPointY,
            startAngleOuter,
            sweepAngleOuter
          ).let {
            if (it < 0f) -it else it
          }

          innerRadius = max(innerRadius, minSpacedRadius)
        }

        val sliceSpaceAngleInner = if (fractions.size == 1 || innerRadius == 0f) 0f
        else sliceSpacingPx / (FDEG2RAD * innerRadius)

        val startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2f) * phase
        val sweepAngleInner = ((sliceAngle - sliceSpaceAngleInner) * phase).coerceAtLeast(0f)

        val endAngleInner = startAngleInner + sweepAngleInner
        if (sweepAngleOuter >= 360f && sweepAngleOuter % 360f <= FLOAT_EPSILON) {
          // Android is doing "mod 360"
          pathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW)
        } else {
          pathBuffer.lineTo(
            center.x + innerRadius * cos(endAngleInner * FDEG2RAD),
            center.y + innerRadius * sin(endAngleInner * FDEG2RAD)
          )

          pathBuffer.arcTo(
            innerRectBuffer,
            endAngleInner,
            -sweepAngleInner
          )
        }
      } else {
        if (sweepAngleOuter % 360f > FLOAT_EPSILON) {
          if (accountForSliceSpacing) {
            val angleMiddle = startAngleOuter + sweepAngleOuter / 2f
            val sliceSpaceOffset = calculateMinimumRadiusForSpacedSlice(
              center,
              radius,
              sliceAngle * phase,
              arcStartPointX,
              arcStartPointY,
              startAngleOuter,
              sweepAngleOuter
            )
            val arcEndPointX = center.x + sliceSpaceOffset * cos(angleMiddle * FDEG2RAD)
            val arcEndPointY = center.y + sliceSpaceOffset * sin(angleMiddle * FDEG2RAD)
            pathBuffer.lineTo(
              arcEndPointX,
              arcEndPointY
            )
          } else {
            pathBuffer.lineTo(
              center.x,
              center.y
            )
          }
        }
      }

      pathBuffer.close()

      nativeCanvas.drawPath(pathBuffer, piecePaint)

      angle += sliceAngle * phase
    }
  }
}
