package hu.ma.charts.line

import android.graphics.Paint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.unit.Dp
import hu.ma.charts.internal.createLegendEntries
import hu.ma.charts.legend.HorizontalLegend
import hu.ma.charts.legend.VerticalLegend
import hu.ma.charts.legend.data.LegendEntry
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.line.data.AxisLabel
import hu.ma.charts.line.data.DrawAxis
import hu.ma.charts.line.data.LineChartData
import kotlin.math.abs

@Composable
fun LinesChartLegend(position: LegendPosition, entries: List<LegendEntry>) {
  when (position) {
    LegendPosition.End, LegendPosition.Start -> {
      VerticalLegend(legendEntries = entries)
    }
    LegendPosition.Top, LegendPosition.Bottom ->
      HorizontalLegend(legendEntries = entries)
  }
}

@Composable
fun LineChart(
  chartHeight: Dp? = null,
  data: LineChartData,
  onDrillDown: ((x: Int, series: List<LineChartData.SeriesData>) -> Unit)? = null,
  legend: (
    @Composable (
      position: LegendPosition,
      entries: List<LegendEntry>
    ) -> Unit
  )? = { position, entries ->
    LinesChartLegend(position = position, entries = entries)
  },
  updateOnDrag: Boolean = false,
) {
  val legendEntries = remember(data) { data.createLegendEntries(data.legendShapeSize) }

  val maxNumberOfPointsOnX = data.series.maxOf { it.points.maxOf { point -> point.x } }

  val maxYValueFromData = data.series.maxOf { it.points.maxOf { point -> point.value } }

  var drillDownPoint by remember { mutableStateOf<Float?>(null) }
  drillDownPoint = null
  var lastSnapToPointX by remember { mutableStateOf<Int?>(null) }
  lastSnapToPointX = null

  val minYValue = data.series.minOf { series -> series.points.minOf { it.value } }
  val yValueAdjustment =
    if (data.floatingYValue) (minYValue - minYValue * 0.05f) else 0f

  val yLabels = when {
    data.autoYLabels -> {
      val numberOfLabels = if (data.maxYLabels == Int.MAX_VALUE) 5 else data.maxYLabels

      val topYLabelValue = maxYValueFromData +
        maxYValueFromData * (0.1 - 0.1 * (yValueAdjustment / maxYValueFromData))

      val labelInterval = (topYLabelValue - yValueAdjustment) / (numberOfLabels - 1)

      (0 until numberOfLabels).map {
        val intValue = (yValueAdjustment + it * labelInterval).toInt()
        AxisLabel(
          intValue.toFloat(),
          if (intValue < 1000) intValue.toString() else "${intValue / 1000}K"
        )
      }
    }
    else -> {
      if (data.floatingYValue) data.yLabels.filter { it.atValue >= minYValue }
      else data.yLabels
    }
  }

  val hasYLabelsOutsideChartValueRange =
    yLabels.isNotEmpty() && yLabels.maxOf { it.atValue } >= maxYValueFromData

  val maxYValueAdjusted = if (hasYLabelsOutsideChartValueRange) {
    yLabels.maxOf { it.atValue }
  } else {
    maxYValueFromData
  }

  BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
    val maxWidth = this.maxWidth

    Column {
      if (data.legendPosition == LegendPosition.Top && legend != null) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = data.legendAlignment.toHorizontalArrangement()
        ) {
          legend(data.legendPosition, legendEntries)
        }
        Spacer(modifier = Modifier.requiredSize(data.legendOffset))
      }

      val animatedDrillDownX by animateFloatAsState(
        targetValue = drillDownPoint ?: with(LocalDensity.current) { maxWidth.toPx() }
      )

      Row(
        modifier = Modifier.wrapContentHeight(),
        verticalAlignment = data.legendAlignment.toVerticalAlignment()
      ) {

        if (data.legendPosition == LegendPosition.Start && legend != null) {
          Column(
            modifier = Modifier
              .wrapContentWidth()
              .padding(end = data.legendOffset)
          ) {
            legend(data.legendPosition, legendEntries)
          }
        }

        val baseModifier = Modifier
          .weight(1f)
          .fillMaxWidth()
        val modifier = if (chartHeight != null) {
          baseModifier.height(chartHeight)
        } else {
          baseModifier.fillMaxHeight()
        }
        Column(modifier = modifier) {

          Canvas(
            modifier = Modifier
              .fillMaxSize()
              .background(data.chartColors.background)
              .pointerInput(data.series.hashCode()) {
                if (onDrillDown != null) {
                  detectHorizontalDragGestures(
                    onHorizontalDrag = { change, _ ->
                      if (change.position.x >= 0 && change.position.x <= maxWidth.toPx()) {
                        drillDownPoint = change.position.x
                        if (updateOnDrag) {
                          val xinterval = maxWidth.toPx() / maxNumberOfPointsOnX
                          val snapToPoint =
                            snapToPoints(xinterval, drillDownPoint ?: 0f, data.series)
                          if (snapToPoint != null && snapToPoint.x != lastSnapToPointX) {
                            drillDownPoint = (snapToPoint.x) * xinterval
                            onDrillDown(snapToPoint.x, data.series)
                            lastSnapToPointX = snapToPoint.x
                          }
                        }
                      }
                    },
                    onDragEnd = {
                      val xinterval = maxWidth.toPx() / maxNumberOfPointsOnX
                      val snapToPoint =
                        snapToPoints(xinterval, drillDownPoint ?: 0f, data.series)
                      if (snapToPoint != null) {
                        drillDownPoint = ((snapToPoint.x) * xinterval).toFloat()
                        onDrillDown(snapToPoint.x, data.series)
                      }
                    }
                  )
                }
              }
              .pointerInput(data.series.hashCode()) {
                if (onDrillDown != null) {
                  detectTapGestures(
                    onTap = {
                      val xinterval = maxWidth.toPx() / maxNumberOfPointsOnX
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

              val yAxisLabelPaint = Paint().apply {
                textSize = data.axisTextSize.toPx()
                color = data.chartColors.ylabel.toArgb()
                typeface = data.axisTypeface
                isAntiAlias = true
              }
              val xAxisLabelPaint = Paint().apply {
                textSize = data.axisTextSize.toPx()
                color = data.chartColors.xlabel.toArgb()
                typeface = data.axisTypeface
                isAntiAlias = true
              }

              val heightOfAxisLabels =
                if (data.xLabels.isNotEmpty()) xAxisLabelPaint.textHeight()
                else 0f

              val heightOfYLabelsBeyondChart =
                if (yLabels.isNotEmpty() && hasYLabelsOutsideChartValueRange)
                  xAxisLabelPaint.textHeight() + data.axisLabelPadding.value
                else 0f

              val componentBottom = this.size.height
              val chartAreaHeight =
                componentBottom - heightOfAxisLabels - heightOfYLabelsBeyondChart
              val chartBottom = componentBottom - heightOfAxisLabels

              val xinterval = this.size.width / maxNumberOfPointsOnX.toFloat()
              val ynormalization = (maxYValueAdjusted - yValueAdjustment) / chartAreaHeight

              yLabels.forEach { ylabel ->
                val y = chartBottom - ((ylabel.atValue - yValueAdjustment) / ynormalization)
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
                    chartBottom - (firstPoint.value - yValueAdjustment) / ynormalization
                  )

                  series.points.subList(1, series.points.size).forEach { point ->
                    gradientPathBuffer.lineTo(
                      point.x * xinterval,
                      chartBottom - (point.value - yValueAdjustment) / ynormalization
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
                        chartBottom - (previous.value - yValueAdjustment) / ynormalization
                      ),
                      end = Offset(
                        xinterval * point.x,
                        chartBottom - (point.value - yValueAdjustment) / ynormalization
                      ),
                      strokeWidth = series.strokeWidth.value
                    )
                  }
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
                  end = Offset(maxNumberOfPointsOnX * xinterval, chartBottom)
                )
              }

              val allLabelsX = data.xLabels.mapIndexed { index, xlabel ->
                val textWidth = xAxisLabelPaint.measureText(xlabel)
                val x = when (index) {
                  0 -> 0f
                  data.xLabels.size - 1 -> maxNumberOfPointsOnX * xinterval - textWidth
                  else -> index * xinterval - textWidth / 2f
                }
                Label(x, componentBottom, xlabel, textWidth)
              }

              if (allLabelsX.combinedWidthIsGreaterThan(
                  this.size.width,
                  data.axisLabelPadding.value
                ) || allLabelsX.anyAreOverlapping()
              ) {
                var reducedLabelsX = allLabelsX
                while (reducedLabelsX.combinedWidthIsGreaterThan(
                    this.size.width,
                    data.axisLabelPadding.value
                  ) || reducedLabelsX.anyAreOverlapping()
                ) {
                  reducedLabelsX = reducedLabelsX.filterIndexed { index, _ -> index % 3 == 0 }
                }
                reducedLabelsX
              } else {
                allLabelsX
              }.forEach { label ->
                drawContext.canvas.nativeCanvas.drawText(
                  label.text,
                  label.x,
                  label.y,
                  xAxisLabelPaint
                )
              }

              yLabels.forEach { ylabel ->
                val y =
                  (chartBottom - (ylabel.atValue - yValueAdjustment) / ynormalization).toInt()
                    .toFloat() - data.axisLabelPadding.value
                drawContext.canvas.nativeCanvas.drawText(
                  ylabel.label,
                  0f,
                  y,
                  yAxisLabelPaint
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
          )
        }

        if (data.legendPosition == LegendPosition.End && legend != null) {
          Column(
            modifier = Modifier
              .padding(start = data.legendOffset)
              .wrapContentWidth()
          ) {
            legend(data.legendPosition, legendEntries)
          }
        }
      }

      if (data.legendPosition == LegendPosition.Bottom && legend != null) {
        Spacer(modifier = Modifier.requiredHeight(data.legendOffset))
        Row(
          modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
          horizontalArrangement = data.legendAlignment.toHorizontalArrangement()
        ) {
          legend(data.legendPosition, legendEntries)
        }
      }
    }
  }
}

private fun Paint.textHeight(): Float {
  return fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading
}

private fun snapToPoints(
  xinterval: Float,
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

private fun List<Label>.anyAreOverlapping(): Boolean {
  return this.filterIndexed { index, label ->
    if (index != this.lastIndex) {
      label.x + label.measuredWidth >= this[index + 1].x
    } else false
  }.count() > 0
}

private data class Label(val x: Float, val y: Float, val text: String, val measuredWidth: Float)
