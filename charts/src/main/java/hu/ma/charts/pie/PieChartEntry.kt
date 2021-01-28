package hu.ma.charts.pie

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

data class PieChartEntry(
  val value: Float,
  val label: AnnotatedString,
  /**
   * Color of the pie slice and legend entry, if not provided [PieChartData.colors] will be used
   */
  val color: Color? = null,
)
