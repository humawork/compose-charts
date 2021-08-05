package hu.ma.charts.legend.data

import androidx.compose.ui.text.AnnotatedString
import hu.ma.charts.ChartShape

data class LegendEntry(
  val text: AnnotatedString,
  val value: Float? = null,
  val percent: Float = Float.MAX_VALUE,
  val shape: ChartShape = ChartShape.Default
)
