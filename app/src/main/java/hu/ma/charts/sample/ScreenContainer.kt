package hu.ma.charts.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenContainer(
  content: LazyListScope.() -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(24.dp),
    contentPadding = PaddingValues(
      top = 24.dp,
      bottom = 24.dp,
    ),
  ) {
    content()
  }
}
