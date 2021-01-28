package hu.ma.charts.pie

import androidx.compose.ui.unit.Dp
import hu.ma.charts.ChartShape
import hu.ma.charts.legend.LegendEntry
import hu.ma.charts.safeGet

internal fun PieChartData.createLegendEntries(
  shapeSize: Dp,
): List<LegendEntry> =
  entries.mapIndexed { index, item ->
    LegendEntry(
      text = item.label,
      value = item.value,
      percent = item.value * 100f / entries.map { it.value }.reduce { acc, i -> acc + i },
      shape = ChartShape(
        color = item.color ?: colors.safeGet(index),
        shape = legendShape,
        size = shapeSize,
      )
    )
  }

internal fun PieChartData.calculateFractions(
  minAngle: Float = 16f,
  maxAngle: Float = 360f
): List<Float> {
  val total = entries.sumByDouble { it.value.toDouble() }.toFloat()
  val entryCount = entries.size

  val hasMinAngle = minAngle != 0f && entryCount * minAngle <= maxAngle
  val minAngles = MutableList(entryCount) { 0f }

  val fractions = entries
    .map { it.value / total }
    .map { it * 360f }

  var offset = 0f
  var diff = 0f

  if (hasMinAngle) {
    fractions.forEachIndexed { idx, angle ->
      val temp = angle - minAngle

      if (temp <= 0) {
        offset += -temp
        minAngles[idx] = minAngle
      } else {
        minAngles[idx] = angle
        diff += temp
      }
    }

    fractions.forEachIndexed { idx, _ ->
      minAngles[idx] -= (minAngles[idx] - minAngle) / diff * offset
    }

    return minAngles
  }

  return fractions
}
