package hu.ma.charts

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.InteractionState
import androidx.compose.ui.graphics.drawscope.ContentDrawScope

internal fun <T> List<T>.safeGet(idx: Int): T = when {
  idx in 0..lastIndex -> this[idx]
  idx > lastIndex -> this[idx - size]
  else -> error("Can't get a color at $idx")
}

// Alternative to passing `indication = null` to clickable before 1.0.0-alpha10
internal val EmptyIndication = object : Indication {
  override fun createInstance(): IndicationInstance = object : IndicationInstance {
    override fun ContentDrawScope.drawIndication(interactionState: InteractionState) {
      drawContent()
    }
  }
}
