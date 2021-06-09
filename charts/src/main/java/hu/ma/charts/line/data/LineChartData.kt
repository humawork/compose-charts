package hu.ma.charts.line.data

import android.graphics.Typeface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class LineChartData(
  val series: List<SeriesData>,
  val xLabels: List<Any> = emptyList(),
  val xLabelFormatter: (label: Any) -> String = { label -> label.toString() },
  val showMaxLabelsX: Int? = null,
  val yLabels: List<AxisLabel> = listOf(),
  val chartColors: ChartColors = ChartColors.defaultColors(),
  val horizontalLines: Boolean = false,
  val drawAxis: DrawAxis = DrawAxis.Both,
  val axisWidth: Float = 2f,
  val axisTextSize: TextUnit = 10.sp,
  val axisTypeface: Typeface = Typeface.DEFAULT,
  val axisLabelPadding: Dp = 10.dp,
) {

  data class SeriesData(
    val title: String,
    val points: List<Point>,
    val color: Color,
    val gradientFill: Boolean = false,
  ) {
    data class Point(
      val x: Int,
      val value: Float
    )
  }

}
