package hu.ma.charts.bars.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

data class StackedBarEntry(
  val text: AnnotatedString,
  val value: Float,
  val color: Color? = null,
)
