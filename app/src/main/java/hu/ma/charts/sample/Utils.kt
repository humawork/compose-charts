package hu.ma.charts.sample

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import hu.ma.charts.table.TableEntry
import kotlin.random.Random

val Categories = listOf(
  "Teams",
  "Locations",
  "Devices",
  "People",
  "Laptops"
)

val colors = listOf(
  Color.Black,
  Color.Blue,
  Color.Yellow,
  Color.Red,
  Color.LightGray,
  Color.Magenta,
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
      drawShape = true,
      shape = CircleShape,
      shapeColor = colors.random(),
    )
  },
)
