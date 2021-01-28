package hu.ma.charts.sample

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenContainer(
  content: @Composable () -> Unit,
) {
  ScrollableColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(24.dp),
  ) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )

    content()

    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
    )
  }
}
