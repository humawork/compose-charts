package hu.ma.charts.sample.bars

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import hu.ma.charts.bars.HorizontalBarsChart
import hu.ma.charts.sample.BarsSampleData
import hu.ma.charts.sample.ChartContainer
import hu.ma.charts.sample.ScreenContainer

@Composable
fun BarsScreen() {
  ScreenContainer {
    BarsSampleData.fastForEach { (title, data) ->
      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .border(BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(16.dp))
          .padding(16.dp)
          .animateContentSize(),
        title = title
      ) {
        HorizontalBarsChart(data = data)
      }
    }
  }
}
