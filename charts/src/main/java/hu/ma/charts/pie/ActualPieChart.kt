package hu.ma.charts.pie

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import hu.ma.charts.safeGet

private const val DividerLengthInDegrees = 1.8f * 2

@Composable
internal fun ActualPieChart(
  modifier: Modifier = Modifier,
  chartSizePx: Float,
  sliceWidthPx: Float,
  fractions: List<Float>,
  composeColors: List<Color>,
) {
  Canvas(modifier = modifier) {
    val innerRadius = (chartSizePx - sliceWidthPx) / 2f

    var startAngle = -90f

    fractions.forEachIndexed { index, sweep ->
      drawArc(
        color = composeColors.safeGet(index),
        startAngle = startAngle + DividerLengthInDegrees / 2,
        sweepAngle = sweep - DividerLengthInDegrees,
        useCenter = false,
        topLeft = Offset(
          x = (chartSizePx / 2f) - innerRadius,
          y = (chartSizePx / 2f) - innerRadius,
        ),
        size = Size(innerRadius * 2, innerRadius * 2),
        style = Stroke(sliceWidthPx)
      )

      startAngle += sweep
    }
  }
}
