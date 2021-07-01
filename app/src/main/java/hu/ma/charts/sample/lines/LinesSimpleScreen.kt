package hu.ma.charts.sample.lines

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.ma.charts.line.LineChart
import hu.ma.charts.sample.ChartContainer
import hu.ma.charts.sample.LinesSampleData
import hu.ma.charts.sample.ScreenContainer

@Composable
fun LinesSimpleScreen() {
  ScreenContainer {
    items(LinesSampleData) { (title, data) ->
      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .height(400.dp),
        title = title
      ) {
        LineChart(
          modifier = Modifier.fillMaxSize(),
          data = data,
          onDrillDown = { xIndex, allSeries ->
            Log.d(
              "LineChart",
              "You are drilling down at xIndex=$xIndex, series values at this index: ${
              allSeries.map { it.points.find { point -> point.x == xIndex } }
                .map { it?.value }
                .joinToString()
              }"
            )
          }
        )
      }
    }
  }
}

@Preview
@Composable
fun LinesSimpleScreenPreview() {
  LinesSimpleScreen()
}
