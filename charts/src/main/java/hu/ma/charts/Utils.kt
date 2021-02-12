package hu.ma.charts

internal fun <T> List<T>.safeGet(idx: Int): T = when {
  idx in 0..lastIndex -> this[idx]
  idx > lastIndex -> this[idx - size]
  else -> error("Can't get a color at $idx")
}
