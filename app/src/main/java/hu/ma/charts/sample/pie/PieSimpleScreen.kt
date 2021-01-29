package hu.ma.charts.sample.pie

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import hu.ma.charts.pie.PieChart
import hu.ma.charts.sample.ChartContainer
import hu.ma.charts.sample.PieSampleData
import hu.ma.charts.sample.ScreenContainer

@Composable
fun PieSimpleScreen() {
  ScreenContainer {
    PieSampleData.fastForEach {
      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .animateContentSize(),
        title = "Pie - Legend::${it.legendPosition.name}"
      ) {
        PieChart(data = it)
      }
    }
  }
}
