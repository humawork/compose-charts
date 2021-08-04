package hu.ma.charts.bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import hu.ma.charts.bars.data.StackedBarItem

internal typealias EntryPathFactory = (entry: EntryDrawShape, size: Size) -> Path

internal val EntrySpacing = 2.dp

@Composable
internal fun DrawStackedBar(
  entries: List<StackedBarItem>,
  widthPx: Float,
  entryPathFactory: EntryPathFactory = { entry, size -> createBarEntryShape(entry, size) },
) {
  val total = remember(entries) { entries.sumOf { it.value.toDouble() }.toFloat() }
  val width = with(LocalDensity.current) { widthPx.toDp() }
  val spacingPx = with(LocalDensity.current) { EntrySpacing.roundToPx() }

  val totalSpacing = (entries.size - 1) * spacingPx

  val values = remember(entries) {
    entries.map {
      it.copy(value = (widthPx - totalSpacing) * it.value / total)
    }
  }

  Row(
    modifier = Modifier.requiredWidth(width),
    horizontalArrangement = Arrangement.spacedBy(EntrySpacing)
  ) {
    values.forEachIndexed { idx, item ->
      val shape = when {
        idx == 0 && values.size == 1 -> EntryDrawShape.Single
        idx == 0 -> EntryDrawShape.First
        idx == values.lastIndex -> EntryDrawShape.Last
        else -> EntryDrawShape.Middle
      }

      Box(
        modifier = Modifier
          .requiredWidth(with(LocalDensity.current) { item.value.toDp() })
          .requiredHeight(8.dp)
          .drawBehind {
            drawPath(entryPathFactory(shape, size), item.color)
          }
      )
    }
  }
}
