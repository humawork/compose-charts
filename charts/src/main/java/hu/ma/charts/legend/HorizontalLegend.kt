package hu.ma.charts.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import hu.ma.charts.internal.DefaultText

@Composable
fun DrawHorizontalLegend(
  legendEntries: List<LegendEntry>,
  text: @Composable (item: LegendEntry) -> Unit = { DefaultText(text = it.text) },
) {
  legendEntries.fastForEachIndexed { index, item ->
    Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .requiredSize(item.shape.size)
          .background(item.shape.color, item.shape.shape)
      )

      Spacer(modifier = Modifier.requiredSize(8.dp))
      text(item)
    }
    if (index != legendEntries.lastIndex) {
      Spacer(modifier = Modifier.requiredSize(8.dp))
    }
  }
}
