package hu.ma.charts.sample.table

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.ma.charts.sample.ChartContainer
import hu.ma.charts.sample.TableSampleData
import hu.ma.charts.table.Table

@Composable
fun TableSimpleScreen() {
  ScrollableColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(24.dp),
  ) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(16.dp)
    )

    TableSampleData.forEach { (title, entries) ->
      ChartContainer(
        modifier = Modifier.padding(16.dp),
        title = title
      ) {
        Table(data = entries)
      }
    }

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(24.dp)
    )
  }
}
