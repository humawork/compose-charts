package hu.ma.charts.line

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import hu.ma.charts.line.data.DrawAxis
import hu.ma.charts.line.data.LineChartData
import kotlin.math.abs
import kotlin.math.max

@Composable
fun LineChart(
  modifier: Modifier = Modifier,
  data: LineChartData,
  onDrillDown: ((x: Int, series: List<LineChartData.SeriesData>) -> Unit)? = null
) {

  val maxXValues = data.series.maxOf { it.points.maxOf { point -> point.x } }

  val maxYValue = max(
    data.series.maxOf { it.points.maxOf { point -> point.value } },
    if (data.yLabels.isNotEmpty()) data.yLabels.maxOf { it.atValue } else 0f
  )

  var drillDownPoint by remember { mutableStateOf<Float?>(null) }

  BoxWithConstraints(modifier = modifier) {

    val maxWidth = this.maxWidth
    val animatedDrillDownX by animateFloatAsState(
      targetValue = drillDownPoint ?: with(LocalDensity.current) { maxWidth.toPx() }
    )

    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .background(data.chartColors.background)
        .pointerInput("drag") {
          if (onDrillDown != null) {
            detectHorizontalDragGestures(
              onHorizontalDrag = { change, _ ->
                if (change.position.x >= 0 && change.position.x <= this.size.width) {
                  drillDownPoint = change.position.x
                }
              },
              onDragEnd = {
                val xinterval = this.size.width / maxXValues
                val snapToPoint = snapToPoints(xinterval, drillDownPoint ?: 0f, data.series)
                if (snapToPoint != null) {
                  drillDownPoint = ((snapToPoint.x) * xinterval).toFloat()
                  onDrillDown(snapToPoint.x, data.series)
                }
              }
            )
          }
        }
        .pointerInput("tap") {
          if (onDrillDown != null) {
            detectTapGestures(
              onTap = {
                val xinterval = this.size.width / maxXValues
                val snapToPoint = snapToPoints(xinterval, it.x, data.series)
                if (snapToPoint != null) {
                  drillDownPoint = ((snapToPoint.x) * xinterval).toFloat()
                  onDrillDown(snapToPoint.x, data.series)
                }
              }
            )
          }
        },
      onDraw = {

        val axisLabelPaint = Paint().apply {
          textSize = data.axisTextSize.toPx()
          color = data.chartColors.label.toArgb()
          typeface = data.axisTypeface
          isAntiAlias = true
        }
        val heightOfAxisLabels =
          axisLabelPaint.fontMetrics.descent - axisLabelPaint.fontMetrics.ascent +
            axisLabelPaint.fontMetrics.leading

        val heightOfYAxisLabels =
          if (data.yLabels.isNotEmpty() && data.yLabels.maxOf { it.atValue } >= maxYValue)
            heightOfAxisLabels + data.axisLabelPadding.value
          else 0f

        val componentBottom = this.size.height
        val chartAreaHeight = componentBottom - heightOfAxisLabels - heightOfYAxisLabels
        val chartBottom = componentBottom - heightOfAxisLabels

        val xinterval = this.size.width / maxXValues.toFloat()
        val ynormalization = maxYValue / chartAreaHeight

        val nativeCanvas = drawContext.canvas.nativeCanvas

        data.yLabels.forEach { ylabel ->
          val y = (chartBottom - (ylabel.atValue / ynormalization)).toInt().toFloat()
          if (data.horizontalLines) {
            drawLine(
              data.chartColors.horizontalLines,
              strokeWidth = 1f,
              start = Offset(0f, y),
              end = Offset(
                this.size.width, y
              ),
            )
          }
        }

        data.series.forEach { series ->

          if (series.gradientFill) {
            val gradientPathBuffer = Path()
            val gradient = Brush.verticalGradient(
              colors = listOf(series.color, Color.Transparent)
            )

            val firstPoint = series.points.first()

            gradientPathBuffer.moveTo(
              firstPoint.x * xinterval,
              chartBottom
            )
            gradientPathBuffer.lineTo(
              firstPoint.x * xinterval,
              chartBottom - firstPoint.value / ynormalization
            )

            series.points.subList(1, series.points.size).forEach { point ->
              gradientPathBuffer.lineTo(
                point.x * xinterval,
                chartBottom - point.value / ynormalization
              )
            }

            gradientPathBuffer.lineTo(series.points.last().x * xinterval, chartBottom)
            gradientPathBuffer.lineTo(firstPoint.x * xinterval, chartBottom)
            drawPath(gradientPathBuffer, gradient)
          }

          series.points.forEachIndexed { index, point ->
            if (index > 0) {
              val previous = series.points[index - 1]

              drawLine(
                color = series.color,
                start = Offset(
                  xinterval * previous.x,
                  chartBottom - previous.value / ynormalization
                ),
                end = Offset(
                  xinterval * point.x,
                  chartBottom - point.value / ynormalization
                ),
                strokeWidth = series.strokeWidth.value
              )
            }
          }

          if (data.drawAxis == DrawAxis.Y || data.drawAxis == DrawAxis.Both) {
            drawLine(
              data.chartColors.axis,
              strokeWidth = data.axisWidth,
              start = Offset(0f, chartBottom),
              end = Offset(0f, 0f)
            )
          }

          if (data.drawAxis == DrawAxis.X || data.drawAxis == DrawAxis.Both) {
            drawLine(
              data.chartColors.axis,
              strokeWidth = data.axisWidth,
              start = Offset(0f, chartBottom),
              end = Offset(maxXValues * xinterval, chartBottom)
            )
          }

          val allLabelsX = data.xLabels.mapIndexed { index, xlabel ->
            val textWidth = axisLabelPaint.measureText(xlabel)
            val x = when (index) {
              0 -> 0f
              data.xLabels.size - 1 -> maxXValues * xinterval - textWidth
              else -> index * xinterval - textWidth / 2f
            }
            Label(x, componentBottom, xlabel, textWidth)
          }

          if (allLabelsX.combinedWidthIsGreaterThan(this.size.width, data.axisLabelPadding.value)) {
            var reducedLabelsX = allLabelsX
            while (reducedLabelsX.combinedWidthIsGreaterThan(
                this.size.width,
                data.axisLabelPadding.value
              )
            ) {
              reducedLabelsX = reducedLabelsX.reversed().filterIndexed { index, label ->
                index % 2 == 0
              }.reversed()
            }
            reducedLabelsX
          } else {
            allLabelsX
          }.forEach { label ->
            nativeCanvas.drawText(
              label.text,
              label.x,
              label.y,
              axisLabelPaint
            )
          }

          data.yLabels.forEach { ylabel ->
            val y = (chartBottom - ylabel.atValue / ynormalization).toInt()
              .toFloat() - data.axisLabelPadding.value
            nativeCanvas.drawText(
              ylabel.label,
              0f,
              y,
              axisLabelPaint
            )
          }

          if (drillDownPoint != null) {
            drawLine(
              color = data.chartColors.drillDownLine,
              start = Offset(animatedDrillDownX, 0f),
              end = Offset(
                animatedDrillDownX, chartBottom
              ),
              strokeWidth = data.drillDownIndicatorStrokeWidth.value
            )
          }
        }
      }
    )
  }
}

private fun snapToPoints(
  xinterval: Int,
  x: Float,
  series: List<LineChartData.SeriesData>
): LineChartData.SeriesData.Point? =
  series.mapNotNull { s -> s.points.minByOrNull { point -> abs(point.x * xinterval - x) } }
    .minByOrNull { abs(it.x * xinterval - x) }

private fun List<Label>.combinedWidthIsGreaterThan(
  totalWidth: Float,
  individualLabelPadding: Float = 0f
): Boolean =
  this.sumOf { label -> label.measuredWidth.toDouble() + individualLabelPadding } > totalWidth

private data class Label(val x: Float, val y: Float, val text: String, val measuredWidth: Float)
