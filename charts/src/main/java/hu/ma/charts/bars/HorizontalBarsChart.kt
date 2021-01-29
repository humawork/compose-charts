package hu.ma.charts.bars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import hu.ma.charts.bars.data.HorizontalBarsData
import hu.ma.charts.bars.data.StackedBarData
import hu.ma.charts.bars.data.StackedBarItem
import hu.ma.charts.legend.DrawHorizontalLegend
import hu.ma.charts.legend.LegendEntry
import hu.ma.charts.safeGet
import hu.ma.charts.table.DefaultText
import kotlin.math.min

internal val MinimumBarWidth = 24.dp
internal val OptionalBarOffset = 75.dp
internal val MaximumBarWidth = 275.dp

typealias TextRowFactory = @Composable RowScope.(title: AnnotatedString) -> Unit

internal val DropdownDefaultModifier = Modifier
  .width(176.dp)
  .padding(16.dp)

@Composable
fun HorizontalBarsChart(
  data: HorizontalBarsData,
  modifier: Modifier = Modifier,
  divider: @Composable (() -> Unit)? = null,
  legend: @Composable (ColumnScope.(entries: List<LegendEntry>) -> Unit)? = null,
  legendOffset: Dp = 4.dp,
  dropdownModifier: Modifier = DropdownDefaultModifier,
  dropdownContent: @Composable (StackedBarData) -> Unit = {
    DropdownContent(data = it, colors = data.colors)
  },
  keyContent: TextRowFactory = { DefaultText(text = it) },
  valueContent: TextRowFactory = { DefaultText(text = it) },
) {
  val legendEntries = remember(data) {
    data.customLegendEntries.takeIf { it.isNotEmpty() } ?: data.legendEntries()
  }
  var popupState: PopupState by remember { mutableStateOf(PopupState.Idle) }

  Column(modifier = modifier) {
    if (legend != null) {
      legend(legendEntries)
    } else {
      DrawHorizontalLegend(legendEntries = legendEntries)
    }

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(legendOffset)
    )

    val maxValue = data.bars.maxByOrNull { bar -> bar.count }?.count ?: 0

    data.bars.forEachIndexed { idx, bar ->
      DropdownMenu(
        toggle = {
          StackedHorizontalBar(
            modifier = Modifier.clickable(
              enabled = data.isPopupEnabled,
              indication = null,
              onClick = { popupState = PopupState.Showing(bar) },
            ),
            colors = data.colors,
            data = bar,
            maxBarValue = maxValue,
            shouldDrawDivider = idx != data.bars.lastIndex,
            divider = divider,
            title = keyContent,
            value = valueContent,
          )
        },
        dropdownOffset = DpOffset(0.dp, (-48).dp),
        dropdownModifier = dropdownModifier,
        expanded = popupState.let { it is PopupState.Showing && it.data == bar },
        onDismissRequest = { popupState = PopupState.Idle }
      ) {
        popupState.let {
          if (it is PopupState.Showing)
            dropdownContent(it.data)
        }
      }
    }
  }
}

@Composable
internal fun StackedHorizontalBar(
  modifier: Modifier = Modifier,
  colors: List<Color>,
  data: StackedBarData,
  maxBarValue: Int,
  shouldDrawDivider: Boolean,
  divider: @Composable (() -> Unit)? = null,
  title: TextRowFactory = {},
  value: TextRowFactory = {},
) {
  val entries = data.entries.mapIndexed { idx, item ->
    StackedBarItem(
      text = item.text,
      value = item.value,
      color = item.color ?: colors.safeGet(idx)
    )
  }

  Column(modifier) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(12.dp)
    )
    Row(
      modifier = Modifier.preferredHeightIn(min = 24.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        // Trim the width of text to [container.width - 50.dp]
        Modifier.layout { measurable, constraints ->
          val width = constraints.maxWidth - 50.dp.toIntPx()
          val placeable = measurable.measure(constraints.copy(maxWidth = width, minWidth = width))
          layout(width, placeable.height) {
            placeable.placeRelative(0, 0)
          }
        }
      ) {
        title(data.title)
      }

      Spacer(
        modifier = Modifier
          .weight(1f)
          .height(24.dp)
      )

      value(AnnotatedString(data.count.toString()))
    }

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(4.dp)
    )

    WithConstraints {
      val rightPadding = with(AmbientDensity.current) { OptionalBarOffset.toIntPx() }
      val maxWidthPx = with(AmbientDensity.current) { MaximumBarWidth.toIntPx() }
      val maxBarWidthPx = min(maxWidthPx, constraints.maxWidth - rightPadding)
      val minWidthPx = with(AmbientDensity.current) { MinimumBarWidth.toIntPx() }

      val percentage = data.count / maxBarValue.toFloat()
      val width = maxBarWidthPx * percentage

      DrawStackedBar(
        entries = entries,
        widthPx = if (width < minWidthPx) width + minWidthPx else width
      )
    }

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(12.dp)
    )

    if (shouldDrawDivider && divider != null) {
      divider()
    }
  }
}
