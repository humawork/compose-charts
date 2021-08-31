package hu.ma.charts.sample

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import hu.ma.charts.ChartShape
import hu.ma.charts.bars.data.HorizontalBarsData
import hu.ma.charts.bars.data.StackedBarData
import hu.ma.charts.bars.data.StackedBarEntry
import hu.ma.charts.legend.data.LegendAlignment
import hu.ma.charts.legend.data.LegendEntry
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.line.data.AxisLabel
import hu.ma.charts.line.data.DrawAxis
import hu.ma.charts.line.data.LineChartData
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry
import hu.ma.charts.table.data.TableEntry
import kotlin.math.roundToInt
import kotlin.random.Random

val Categories = listOf(
  "Teams",
  "Locations",
  "Devices",
  "People",
  "Laptops",
  "Titles",
  "Flowers",
  "Bugs",
  "Windows",
  "Screens",
  "Colors",
  "Bottles",
  "Cars",
  "Tricks",
)

val SimpleColors = listOf(
  Color.Black,
  Color.Blue,
  Color.Yellow,
  Color.Red,
  Color.LightGray,
  Color.Magenta,
  Color.Cyan,
  Color.Green,
  Color.Gray,
)

internal val TableSampleData = listOf(
  "Without Shape" to (1..6).map {
    TableEntry(
      key = AnnotatedString(Categories[it]),
      value = AnnotatedString((it * Random(System.currentTimeMillis()).nextInt(123)).toString()),
    )
  },
  "With Shape" to (1..5).map {
    TableEntry(
      key = AnnotatedString(Categories[it]),
      value = AnnotatedString((it * Random(System.currentTimeMillis()).nextInt(123)).toString()),
      drawShape = ChartShape(
        size = 8.dp,
        shape = CircleShape,
        color = SimpleColors[it],
      )
    )
  },
)

internal val PieSampleData = LegendPosition.values().map {
  PieChartData(
    entries = listOf(430f, 240f, 140f, 60f, 50f).mapIndexed { idx, value ->
      PieChartEntry(
        value = value,
        label = AnnotatedString(Categories[idx])
      )
    },
    colors = SimpleColors,
    legendPosition = it,
    legendShape = CircleShape,
  )
}

internal val BarsSampleData = listOf(
  "Bars" to HorizontalBarsData(
    bars = createBars(true),
  ),
  "Bars without colors for entries" to HorizontalBarsData(
    bars = createBars(false),
    colors = SimpleColors.reversed(),
  ),
  "Bars without popup" to HorizontalBarsData(
    bars = createBars(true),
    colors = SimpleColors.reversed(),
    isPopupEnabled = false,
  ),
  "Bars with custom legend" to HorizontalBarsData(
    bars = createBars(true),
    colors = SimpleColors.reversed(),
    customLegendEntries = (0..4).map {
      LegendEntry(
        text = AnnotatedString("Legend entry $it"),
        value = it.toFloat(),
        shape = ChartShape.Default.copy(color = SimpleColors.reversed()[it])
      )
    }
  ),
)

internal val LinesSampleData = listOf(
  "Lines" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red
      ),
      LineChartData.SeriesData(
        "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue
      ),
    ),
    xLabels = listOf("Year 1", "2", "3", "4", "5", "6")
  ),
  "Gradient fill" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
  ),
  "Y-axis labels" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
  ),
  "Y-axis labels w/lines" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
    horizontalLines = true,
  ),
  "Legend top" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.Top,
  ),
  "Legend bottom" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.Bottom,
  ),
  "Legend start" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.Start,
  ),
  "Legend end" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.End,
  ),
  "Legend end, vertical alignment" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.End,
    legendAlignment = LegendAlignment.Center,
  ),
  "Legend bottom, center alignment" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    xLabels = listOf("A", "B", "C", "D", "E", "F"),
    yLabels = listOf(
      AxisLabel(0f, "0K"),
      AxisLabel(20f, "20K"),
      AxisLabel(40f, "40K"),
    ),
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.Bottom,
    legendAlignment = LegendAlignment.Center,
  ),
  "Autogenerated Y-labels" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 0f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 20.0f),
          LineChartData.SeriesData.Point(3, 30.0f),
          LineChartData.SeriesData.Point(4, 50.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20f),
          LineChartData.SeriesData.Point(1, 10.0f),
          LineChartData.SeriesData.Point(2, 5.0f),
          LineChartData.SeriesData.Point(3, 15.0f),
          LineChartData.SeriesData.Point(4, 30.0f),
          LineChartData.SeriesData.Point(5, 35.0f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    xLabels = listOf("A", "B", "C", "D", "E", "F"),
    autoYLabels = true,
    maxYLabels = 4,
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.Bottom,
    legendAlignment = LegendAlignment.Center,
  ),
  "Floating y-value" to LineChartData(
    series = listOf(
      LineChartData.SeriesData(
        title = "Line A",
        points = listOf(
          LineChartData.SeriesData.Point(0, 15_000f),
          LineChartData.SeriesData.Point(1, 10_000f),
          LineChartData.SeriesData.Point(2, 20_000f),
          LineChartData.SeriesData.Point(3, 30_000f),
          LineChartData.SeriesData.Point(4, 50_000f),
          LineChartData.SeriesData.Point(5, 35_000f),
        ),
        Color.Red, gradientFill = true
      ),
      LineChartData.SeriesData(
        title = "Line B",
        points = listOf(
          LineChartData.SeriesData.Point(0, 20_000f),
          LineChartData.SeriesData.Point(1, 10_000f),
          LineChartData.SeriesData.Point(2, 18_000f),
          LineChartData.SeriesData.Point(3, 15_000f),
          LineChartData.SeriesData.Point(4, 30_000f),
          LineChartData.SeriesData.Point(5, 35_000f),
        ),
        Color.Blue, gradientFill = true
      ),
    ),
    xLabels = listOf("A", "B", "C", "D", "E", "F"),
    autoYLabels = true,
    maxYLabels = 4,
    floatingYValue = true,
    drawAxis = DrawAxis.X,
    horizontalLines = true,
    legendPosition = LegendPosition.Bottom,
    legendAlignment = LegendAlignment.Center,
  ),
)

private fun createBars(withColor: Boolean) = listOf(
  listOf(12f, 2f, 3f, 2f),
  listOf(3f, 2f, 4f, 5f),
  listOf(1f, 4f, 12f, 5f),
  listOf(1f, 20f, 2f, 1f),
).mapIndexed { idx, values ->
  StackedBarData(
    title = AnnotatedString("Bars $idx"),
    entries = values.mapIndexed { index, value ->
      StackedBarEntry(
        text = AnnotatedString(Categories[index]),
        value = value,
        color = SimpleColors[index].takeIf { withColor }
      )
    }
  )
}

@Composable
internal fun buildValuePercentString(item: LegendEntry) = buildAnnotatedString {
  item.value?.let { value ->
    withStyle(
      style = MaterialTheme.typography.body2.toSpanStyle()
        .copy(color = MaterialTheme.colors.primary)
    ) {
      append(value.toInt().toString())
    }
    append(" ")
  }

  withStyle(
    style = MaterialTheme.typography.caption.toSpanStyle()
      .copy(color = MaterialTheme.colors.secondary)
  ) {
    val percentString = item.percent.roundToInt().toString()
    append("($percentString %)")
  }
}
