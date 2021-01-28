package hu.ma.charts.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
internal fun TableRow(
  modifier: Modifier = Modifier,
  entry: TableEntry,
  shapeModifier: Modifier,
  keyText: @Composable RowScope.(key: AnnotatedString?) -> Unit,
  valueText: @Composable RowScope.(value: AnnotatedString?) -> Unit,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (entry.drawShape && entry.shape != null && entry.shapeColor != null) {
      Box(
        modifier = shapeModifier.background(entry.shapeColor, entry.shape)
      )
      Spacer(modifier = Modifier.size(8.dp))
    }

    keyText(entry.key)

    Spacer(modifier = Modifier.weight(1f))

    valueText(entry.value)
  }
}
