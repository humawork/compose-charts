package hu.ma.charts.pie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import hu.ma.charts.internal.calculateFractions
import hu.ma.charts.internal.createLegendEntries
import hu.ma.charts.legend.VerticalLegend
import hu.ma.charts.legend.data.LegendEntry
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.data.PieChartData

@Composable
fun PieChart(
  data: PieChartData,
  modifier: Modifier = Modifier,
  chartSize: Dp = 100.dp,
  sliceWidth: Dp = 16.dp,
  legendOffset: Dp = 24.dp,
  chartShapeSize: Dp = 8.dp,
  sliceSpacing: Dp = 2.dp,
  useMinimumSliceAngle: Boolean = true,
  legend: @Composable (RowScope.(entries: List<LegendEntry>) -> Unit)? = {
    VerticalLegend(modifier = Modifier.weight(1f), legendEntries = it)
  },
) {
  val fractions = remember(data, useMinimumSliceAngle) {
    data.calculateFractions(if (useMinimumSliceAngle) 16f else 0f)
  }
  val legendEntries = remember(data) { data.createLegendEntries(chartShapeSize) }

  val chartSizePx = with(LocalDensity.current) { chartSize.toPx() }
  val sliceWidthPx = with(LocalDensity.current) { sliceWidth.toPx() }
  val sliceSpacingPx = with(LocalDensity.current) { sliceSpacing.toPx() }

  Column(modifier) {
    if (legend != null && data.legendPosition == LegendPosition.Top) {
      Row {
        legend(legendEntries)
      }
      Spacer(modifier = Modifier.requiredSize(legendOffset))
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      val entryColors = data.entries.mapNotNull { it.color }

      if (legend != null && data.legendPosition == LegendPosition.Start) {
        legend(legendEntries)
        Spacer(modifier = Modifier.requiredSize(legendOffset))
      }

      PieChartRenderer(
        modifier = Modifier.requiredSize(chartSize),
        chartSizePx = chartSizePx,
        sliceWidthPx = sliceWidthPx,
        sliceSpacingPx = sliceSpacingPx,
        fractions = fractions,
        composeColors = entryColors.takeIf { it.size == data.entries.size } ?: data.colors,
        animate = data.animate,
      )

      if (legend != null && data.legendPosition == LegendPosition.End) {
        Spacer(modifier = Modifier.requiredSize(legendOffset))
        legend(legendEntries)
      }
    }

    if (legend != null && data.legendPosition == LegendPosition.Bottom) {
      Spacer(modifier = Modifier.requiredSize(legendOffset))
      Row {
        legend(legendEntries)
      }
    }
  }
}
