package hu.ma.charts.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import hu.ma.charts.table.DefaultText

@Composable
fun DrawHorizontalLegend(
  legendEntries: List<LegendEntry>,
  text: @Composable (item: LegendEntry) -> Unit = { DefaultText(text = it.text) },
) {
  FlowRow(
    mainAxisSpacing = 16.dp,
    crossAxisSpacing = 8.dp,
  ) {
    legendEntries.fastForEach { item ->
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .requiredSize(item.shape.size)
            .background(item.shape.color, item.shape.shape)
        )

        Spacer(modifier = Modifier.requiredSize(8.dp))

        text(item)
      }
    }
  }
}
