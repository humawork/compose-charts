package hu.ma.charts.sample.table

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.ma.charts.sample.ChartContainer
import hu.ma.charts.sample.ScreenContainer
import hu.ma.charts.sample.TableSampleData
import hu.ma.charts.table.Table

@Composable
fun TableSimpleScreen() {
  ScreenContainer {
    items(TableSampleData) { (title, entries) ->
      ChartContainer(
        modifier = Modifier.padding(16.dp),
        title = title
      ) {
        Table(data = entries)
      }
    }
  }
}
