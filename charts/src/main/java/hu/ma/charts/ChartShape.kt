package hu.ma.charts

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ChartShape(
  val size: Dp,
  val color: Color,
  val shape: Shape,
) {
  companion object {
    val Default = ChartShape(
      size = 8.dp,
      color = Color.Cyan,
      shape = CircleShape
    )
  }
}
