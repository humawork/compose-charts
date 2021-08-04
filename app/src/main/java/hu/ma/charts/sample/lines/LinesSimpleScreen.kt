package hu.ma.charts.sample.lines

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import hu.ma.charts.legend.LegendPosition
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
          .padding(horizontal = 16.dp, vertical = 8.dp),
        title = title
      ) {
        LineChart(
          chartHeight = 400.dp,
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
    item {
      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        title = "Chart with custom legend"
      ) {
        LineChart(
          chartHeight = 400.dp,
          data = LinesSampleData.first().second.copy(legendPosition = LegendPosition.Top),
          onDrillDown = { xIndex, allSeries ->
            Log.d(
              "LineChart",
              "You are drilling down at xIndex=$xIndex, series values at this index: ${
                allSeries.map { it.points.find { point -> point.x == xIndex } }
                  .map { it?.value }
                  .joinToString()
              }"
            )
          },
          legend = { position, entries ->
            Text(text = "Showing series: ")
            entries.fastForEachIndexed { index, item ->
              Text(text = "${item.text}")
              if (index != entries.lastIndex) {
                Text(text = ", ")
              }
            }
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
