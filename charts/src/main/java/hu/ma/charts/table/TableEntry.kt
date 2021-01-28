package hu.ma.charts.table

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString

data class TableEntry(
  val key: AnnotatedString?,
  val value: AnnotatedString?,
  val drawShape: Boolean = false,
  val shape: Shape? = null,
  val shapeColor: Color? = null,
)
