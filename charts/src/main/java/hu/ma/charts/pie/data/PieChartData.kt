package hu.ma.charts.pie.data

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import hu.ma.charts.legend.data.LegendPosition

data class PieChartData(
  val entries: List<PieChartEntry>,
  val colors: List<Color> = emptyList(),
  val legendPosition: LegendPosition = LegendPosition.Bottom,
  val legendShape: Shape = CircleShape,
  val animate: Boolean = true,
)
