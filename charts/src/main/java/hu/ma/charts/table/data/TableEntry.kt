package hu.ma.charts.table.data

import androidx.compose.ui.text.AnnotatedString
import hu.ma.charts.ChartShape

data class TableEntry(
  val key: AnnotatedString?,
  val value: AnnotatedString?,
  val drawShape: ChartShape? = null,
)
