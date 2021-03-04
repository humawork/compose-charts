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
import hu.ma.charts.legend.DrawVerticalLegend
import hu.ma.charts.legend.LegendEntry

@Composable
fun PieChart(
  data: PieChartData,
  modifier: Modifier = Modifier,
  chartSize: Dp = 100.dp,
  sliceWidth: Dp = 16.dp,
  legendOffset: Dp = 24.dp,
  chartShapeSize: Dp = 8.dp,
  sliceSpacing: Dp = 2.dp,
  legend: @Composable (RowScope.(entries: List<LegendEntry>) -> Unit)? = null,
) {
  val fractions = remember(data) { data.calculateFractions() }
  val legendEntries = remember(data) { data.createLegendEntries(chartShapeSize) }

  val chartSizePx = with(LocalDensity.current) { chartSize.toPx() }
  val sliceWidthPx = with(LocalDensity.current) { sliceWidth.toPx() }
  val sliceSpacingPx = with(LocalDensity.current) { sliceSpacing.toPx() }

  @Composable
  fun RowScope.legend() {
    if (legend == null) {
      DrawVerticalLegend(legendEntries)
    } else {
      legend(legendEntries)
    }
  }

  Column(Modifier.fillMaxWidth()) {
    if (data.legendPosition == LegendPosition.Top) {
      Row {
        legend()
      }
      Spacer(modifier = Modifier.requiredSize(legendOffset))
    }

    Row(
      modifier = modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      val entryColors = data.entries.mapNotNull { it.color }

      if (data.legendPosition == LegendPosition.Start) {
        legend()
        Spacer(modifier = Modifier.requiredSize(legendOffset))
      }

      ActualPieChart(
        modifier = Modifier.requiredSize(chartSize),
        chartSizePx = chartSizePx,
        sliceWidthPx = sliceWidthPx,
        sliceSpacingPx = sliceSpacingPx,
        fractions = fractions,
        composeColors = entryColors.takeIf { it.size == data.entries.size } ?: data.colors,
        animate = data.animate,
      )

      if (data.legendPosition == LegendPosition.End) {
        Spacer(modifier = Modifier.requiredSize(legendOffset))
        legend()
      }
    }

    if (data.legendPosition == LegendPosition.Bottom) {
      Spacer(modifier = Modifier.requiredSize(legendOffset))
      Row {
        legend()
      }
    }
  }
}
