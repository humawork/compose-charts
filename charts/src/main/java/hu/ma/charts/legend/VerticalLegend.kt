package hu.ma.charts.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import hu.ma.charts.internal.DefaultText
import hu.ma.charts.legend.data.LegendEntry

@Composable
fun VerticalLegend(
  modifier: Modifier = Modifier,
  legendEntries: List<LegendEntry>,
  text: @Composable (entry: LegendEntry) -> Unit = {
    DefaultText(text = it.text)
  },
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center
  ) {
    legendEntries.fastForEachIndexed { idx, item ->
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .requiredSize(item.shape.size)
            .background(item.shape.color, item.shape.shape)
        )

        Spacer(modifier = Modifier.requiredSize(8.dp))

        text(item)
      }

      if (idx != legendEntries.lastIndex)
        Spacer(modifier = Modifier.requiredSize(8.dp))
    }
  }
}
