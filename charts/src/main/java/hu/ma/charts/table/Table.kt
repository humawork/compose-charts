package hu.ma.charts.table

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import hu.ma.charts.internal.DefaultText

@Composable
fun Table(
  data: List<TableEntry>,
  modifier: Modifier = Modifier,
  shapeModifier: Modifier = Modifier.requiredSize(8.dp),
  keyText: @Composable RowScope.(key: AnnotatedString?) -> Unit = { DefaultText(text = it) },
  valueText: @Composable RowScope.(value: AnnotatedString?) -> Unit = { DefaultText(text = it) },
  divider: @Composable (() -> Unit)? = null,
) {
  Column(modifier) {
    data.forEachIndexed { idx, item ->
      TableRow(
        entry = item,
        shapeModifier = shapeModifier,
        keyText = keyText,
        valueText = valueText,
      )
      if (idx != data.lastIndex && divider != null)
        divider()
    }
  }
}

