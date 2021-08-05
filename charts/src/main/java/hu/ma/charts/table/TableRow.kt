package hu.ma.charts.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import hu.ma.charts.table.data.TableEntry

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
    if (entry.drawShape != null) {
      Box(
        modifier = shapeModifier.background(entry.drawShape.color, entry.drawShape.shape)
      )
      Spacer(modifier = Modifier.requiredSize(entry.drawShape.size))
    }

    keyText(entry.key)

    Spacer(modifier = Modifier.weight(1f))

    valueText(entry.value)
  }
}
