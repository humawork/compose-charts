package hu.ma.charts.bars.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

internal data class StackedBarItem(
  val text: AnnotatedString,
  val value: Float,
  val color: Color,
)
