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
import hu.ma.charts.legend.LegendEntry
import hu.ma.charts.pie.LegendPosition
import hu.ma.charts.pie.PieChartData
import hu.ma.charts.pie.PieChartEntry
import hu.ma.charts.table.TableEntry
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
  withStyle(
    style = MaterialTheme.typography.body2.toSpanStyle()
      .copy(color = MaterialTheme.colors.primary)
  ) {
    append(item.value.toInt().toString())
  }

  append(" ")

  withStyle(
    style = MaterialTheme.typography.caption.toSpanStyle()
      .copy(color = MaterialTheme.colors.secondary)
  ) {
    val percentString = item.percent.roundToInt().toString()
    append("($percentString %)")
  }
}
