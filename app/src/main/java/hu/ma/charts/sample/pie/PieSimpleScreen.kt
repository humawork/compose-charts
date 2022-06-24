package hu.ma.charts.sample.pie

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import hu.ma.charts.legend.data.LegendPosition
import hu.ma.charts.pie.PieChart
import hu.ma.charts.pie.data.PieChartData
import hu.ma.charts.pie.data.PieChartEntry
import hu.ma.charts.sample.Categories
import hu.ma.charts.sample.ChartContainer
import hu.ma.charts.sample.PieSampleData
import hu.ma.charts.sample.ScreenContainer
import hu.ma.charts.sample.SimpleColors

@Composable
fun PieSimpleScreen() {
  ScreenContainer {
    items(PieSampleData) {
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

    item {
      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .animateContentSize(),
        title = "Full Pie"
      ) {
        PieChart(data = PieSampleData[0], sliceWidth = 1000.dp)
      }
    }

    item {
      val data = PieChartData(
        entries = listOf(430f).mapIndexed { idx, value ->
          PieChartEntry(
            value = value,
            label = AnnotatedString(Categories[idx])
          )
        },
        colors = SimpleColors,
        legendPosition = LegendPosition.Start,
        legendShape = CircleShape,
      )

      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .animateContentSize(),
        title = "One Slice"
      ) {
        PieChart(data = data)
      }
    }

    item {
      val data = PieChartData(
        entries = listOf(100f, 0f).mapIndexed { idx, value ->
          PieChartEntry(
            value = value,
            label = AnnotatedString(Categories[idx])
          )
        },
        colors = SimpleColors,
        legendPosition = LegendPosition.Start,
        legendShape = CircleShape,
      )

      ChartContainer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .animateContentSize(),
        title = "No Slice"
      ) {
        PieChart(data = data, useMinimumSliceAngle = false, sliceSpacing = 0.dp)
      }
    }
  }
}
