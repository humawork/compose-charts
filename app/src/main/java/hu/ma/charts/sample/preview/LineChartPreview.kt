package hu.ma.charts.sample.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.ma.charts.line.LineChart
import hu.ma.charts.line.data.AxisLabel
import hu.ma.charts.line.data.DrawAxis
import hu.ma.charts.line.data.LineChartData

@Preview
@Composable
fun LineChartPreview() {
  LineChart(
    data = LineChartData(
      series = listOf(
        LineChartData.SeriesData(
          "Line 1",
          color = Color(0xFF66C194), gradientFill = true,
          points = listOf(
            LineChartData.SeriesData.Point(0, 0f),
            LineChartData.SeriesData.Point(1, 100f),
            LineChartData.SeriesData.Point(2, 150f),
            LineChartData.SeriesData.Point(3, 75f)
          )
        ),
        LineChartData.SeriesData(
          "Line 2", color = Color(0xFF427C9C), gradientFill = true,
          points = listOf(
            LineChartData.SeriesData.Point(1, 50f),
            LineChartData.SeriesData.Point(2, 20f),
            LineChartData.SeriesData.Point(3, 80f),
            LineChartData.SeriesData.Point(4, 120f),
            LineChartData.SeriesData.Point(6, 90f),
          )
        )
      ),
      xLabels = listOf("2015", "2016", "2017", "2018", "2019", "2020", "2021"),
      yLabels = listOf(
        AxisLabel(0f, "0"),
        AxisLabel(50f, "50"),
        AxisLabel(100f, "100"),
        AxisLabel(150f, "150")
      ),
      horizontalLines = true,
      drawAxis = DrawAxis.None
    ),
    chartHeight = 400.dp
  )
}
