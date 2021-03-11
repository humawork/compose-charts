package hu.ma.charts.internal

internal fun <T> List<T>.safeGet(idx: Int): T = when {
  idx in 0..lastIndex -> this[idx]
  idx > lastIndex -> this[idx - size]
  else -> error("Can't get a color at $idx")
}

internal const val DEG2RAD = Math.PI / 180.0
internal const val FDEG2RAD = Math.PI.toFloat() / 180f
internal val FLOAT_EPSILON = Float.fromBits(1)
