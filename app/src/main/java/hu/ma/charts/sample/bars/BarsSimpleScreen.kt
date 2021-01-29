package hu.ma.charts.sample.bars

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.ma.charts.bars.HorizontalBarsChart
import hu.ma.charts.sample.BarsSampleData
import hu.ma.charts.sample.ChartContainer
import hu.ma.charts.sample.ScreenContainer

@Composable
fun BarsSimpleScreen() {
  ScreenContainer {
    items(BarsSampleData) { (title, data) ->
      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .animateContentSize(),
        title = title
      ) {
        HorizontalBarsChart(data = data)
      }
    }
  }
}
