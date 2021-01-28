package hu.ma.charts.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.setContent
import hu.ma.charts.sample.theme.ChartsTheme

class SampleActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      ChartsTheme {
        RootScreen()
      }
    }
  }
}
