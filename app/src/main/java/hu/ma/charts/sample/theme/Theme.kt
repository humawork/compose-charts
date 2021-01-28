package hu.ma.charts.sample.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ChartsTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colors = CustomColorPalette,
  ) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colors.background
    ) {
      content()
    }
  }
}
