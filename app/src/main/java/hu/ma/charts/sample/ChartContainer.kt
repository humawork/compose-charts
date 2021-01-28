package hu.ma.charts.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ChartContainer(
  modifier: Modifier = Modifier,
  title: String,
  chartOffset: Dp = 12.dp,
  content: @Composable () -> Unit,
) {
  Column(modifier = modifier) {
    Text(title, style = MaterialTheme.typography.h6)
    Spacer(modifier = Modifier.size(chartOffset))
    content()
  }
}
