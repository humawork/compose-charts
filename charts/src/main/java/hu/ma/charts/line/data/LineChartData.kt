package hu.ma.charts.line.data

import android.graphics.Typeface
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.ma.charts.legend.data.LegendAlignment
import hu.ma.charts.legend.data.LegendPosition

data class LineChartData(
  val series: List<SeriesData>,
  val xLabels: List<String> = emptyList(),
  val yLabels: List<AxisLabel> = listOf(),
  val chartColors: ChartColors = ChartColors.defaultColors(),
  val horizontalLines: Boolean = false,
  val drawAxis: DrawAxis = DrawAxis.Both,
  val axisWidth: Float = 2f,
  val axisTextSize: TextUnit = 10.sp,
  val axisTypeface: Typeface = Typeface.DEFAULT,
  val axisLabelPadding: Dp = 10.dp,
  val drillDownIndicatorStrokeWidth: Dp = 3.dp,
  val legendPosition: LegendPosition = LegendPosition.Hidden,
  val legendAlignment: LegendAlignment = LegendAlignment.Start,
  val legendOffset: Dp = 16.dp,
  val legendShapeSize: Dp = 8.dp,
  val legendShape: Shape = CircleShape,
) {

  data class SeriesData(
    val title: String,
    val points: List<Point>,
    val color: Color,
    val strokeWidth: Dp = 4.dp,
    val gradientFill: Boolean = false,
  ) {
    data class Point(
      val x: Int,
      val value: Float
    )
  }
}
