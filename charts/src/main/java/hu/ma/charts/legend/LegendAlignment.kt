package hu.ma.charts.legend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

enum class LegendAlignment {
  Start, Center, End;

  fun toVerticalArrangement(): Arrangement.Vertical =
    when (this) {
      Start -> Arrangement.Top
      End -> Arrangement.Bottom
      Center -> Arrangement.Center
    }

  fun toVerticalAlignment(): Alignment.Vertical =
    when (this) {
      Start -> Alignment.Top
      End -> Alignment.Bottom
      Center -> Alignment.CenterVertically
    }

  fun toHorizontalArrangement(): Arrangement.Horizontal =
    when (this) {
      Start -> Arrangement.Start
      End -> Arrangement.End
      Center -> Arrangement.Center
    }
}
