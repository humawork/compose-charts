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
      key = AnnotatedString(Categories.random()),
      value = AnnotatedString((it * Random(System.currentTimeMillis()).nextInt(123)).toString()),
    )
  },
  "With Shape" to (1..5).map {
    TableEntry(
      key = AnnotatedString(Categories.random()),
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
    entries = listOf(240f, 430f, 140f, 60f, 50f).map { value ->
      PieChartEntry(
        value = value,
        label = AnnotatedString(Categories.random())
      )
    },
    colors = SimpleColors,
    legendPosition = it,
    legendShape = CircleShape,
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

