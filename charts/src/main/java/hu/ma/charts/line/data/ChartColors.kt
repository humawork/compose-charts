package hu.ma.charts.line.data

import androidx.compose.ui.graphics.Color

data class ChartColors(
  val axis: Color,
  val background: Color,
  val xlabel: Color,
  val ylabel: Color,
  val horizontalLines: Color,
  val drillDownLine: Color,
) {

  companion object {
    fun defaultColors(
      axis: Color = Color.Black,
      background: Color = Color.Transparent,
      ylabel: Color = Color(0xFF909BAA),
      xlabel: Color = Color(0xFF909BAA),
      horizontalLines: Color = Color(0xFF233F53),
      drillDownLines: Color = Color.Black
    ): ChartColors = ChartColors(
      axis = axis,
      background = background,
      xlabel = xlabel,
      ylabel = ylabel,
      horizontalLines = horizontalLines,
      drillDownLine = drillDownLines
    )
  }
}
