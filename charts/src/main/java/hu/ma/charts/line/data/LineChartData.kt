package hu.ma.charts.line.data

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hu.ma.charts.legend.data.LegendAlignment
import hu.ma.charts.legend.data.LegendPosition

data class LineChartData(
  val series: List<SeriesData>,
  val xLabels: List<String> = emptyList(),
  val yLabels: List<AxisLabel> = emptyList(),
  val maxYLabels: Int = Int.MAX_VALUE,
  val autoYLabels: Boolean = false,
  val chartColors: ChartColors = ChartColors.defaultColors(),
  val horizontalLines: Boolean = false,
  val floatingYValue: Boolean = false,
  val drawAxis: DrawAxis = DrawAxis.Both,
  val axisWidth: Float = 2f,
  val xAxisTypeface: TextStyle = TextStyle.Default,
  val yAxisTypeface: TextStyle = TextStyle.Default,
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
