/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.layout

import androidx.compose.layout.LayoutOrientation.Horizontal
import androidx.compose.layout.LayoutOrientation.Vertical
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.util.fastForEach
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * [Row] will be [Horizontal], [Column] is [Vertical].
 */
internal enum class LayoutOrientation {
  Horizontal,
  Vertical
}

/**
 * Used to specify the alignment of a layout's children, in cross axis direction.
 */
@Immutable
internal sealed class CrossAxisAlignment {
  /**
   * Aligns to [size]. If this is a vertical alignment, [layoutDirection] should be
   * [LayoutDirection.Ltr].
   *
   * @param size The remaining space (total size - content size) in the container.
   * @param layoutDirection The layout direction of the content if horizontal or
   * [LayoutDirection.Ltr] if vertical.
   * @param placeable The item being aligned.
   * @param beforeCrossAxisAlignmentLine The space before the cross-axis alignment line if
   * an alignment line is being used or 0 if no alignment line is being used.
   */
  internal abstract fun align(
    size: Int,
    layoutDirection: LayoutDirection,
    placeable: Placeable,
    beforeCrossAxisAlignmentLine: Int
  ): Int

  /**
   * Returns `true` if this is [Relative].
   */
  internal open val isRelative: Boolean
    get() = false

  /**
   * Returns the alignment line position relative to the left/top of the space or `null` if
   * this alignment doesn't rely on alignment lines.
   */
  internal open fun calculateAlignmentLinePosition(placeable: Placeable): Int? = null

  companion object {
    /**
     * Place children such that their center is in the middle of the cross axis.
     */
    @Stable
    val Center: CrossAxisAlignment = CenterCrossAxisAlignment

    /**
     * Place children such that their start edge is aligned to the start edge of the cross
     * axis. TODO(popam): Consider rtl directionality.
     */
    @Stable
    val Start: CrossAxisAlignment = StartCrossAxisAlignment

    /**
     * Place children such that their end edge is aligned to the end edge of the cross
     * axis. TODO(popam): Consider rtl directionality.
     */
    @Stable
    val End: CrossAxisAlignment = EndCrossAxisAlignment

    /**
     * Align children by their baseline.
     */
    fun AlignmentLine(alignmentLine: AlignmentLine): CrossAxisAlignment =
      AlignmentLineCrossAxisAlignment(AlignmentLineProvider.Value(alignmentLine))

    /**
     * Align children relative to their siblings using the alignment line provided as a
     * parameter using [AlignmentLineProvider].
     */
    internal fun Relative(alignmentLineProvider: AlignmentLineProvider): CrossAxisAlignment =
      AlignmentLineCrossAxisAlignment(alignmentLineProvider)

    /**
     * Align children with vertical alignment.
     */
    internal fun vertical(vertical: Alignment.Vertical): CrossAxisAlignment =
      VerticalCrossAxisAlignment(vertical)

    /**
     * Align children with horizontal alignment.
     */
    internal fun horizontal(horizontal: Alignment.Horizontal): CrossAxisAlignment =
      HorizontalCrossAxisAlignment(horizontal)
  }

  private object CenterCrossAxisAlignment : CrossAxisAlignment() {
    override fun align(
      size: Int,
      layoutDirection: LayoutDirection,
      placeable: Placeable,
      beforeCrossAxisAlignmentLine: Int
    ): Int {
      return size / 2
    }
  }

  private object StartCrossAxisAlignment : CrossAxisAlignment() {
    override fun align(
      size: Int,
      layoutDirection: LayoutDirection,
      placeable: Placeable,
      beforeCrossAxisAlignmentLine: Int
    ): Int {
      return if (layoutDirection == LayoutDirection.Ltr) 0 else size
    }
  }

  private object EndCrossAxisAlignment : CrossAxisAlignment() {
    override fun align(
      size: Int,
      layoutDirection: LayoutDirection,
      placeable: Placeable,
      beforeCrossAxisAlignmentLine: Int
    ): Int {
      return if (layoutDirection == LayoutDirection.Ltr) size else 0
    }
  }

  private class AlignmentLineCrossAxisAlignment(
    val alignmentLineProvider: AlignmentLineProvider
  ) : CrossAxisAlignment() {
    override val isRelative: Boolean
      get() = true

    override fun calculateAlignmentLinePosition(placeable: Placeable): Int? {
      return alignmentLineProvider.calculateAlignmentLinePosition(placeable)
    }

    override fun align(
      size: Int,
      layoutDirection: LayoutDirection,
      placeable: Placeable,
      beforeCrossAxisAlignmentLine: Int
    ): Int {
      val alignmentLinePosition =
        alignmentLineProvider.calculateAlignmentLinePosition(placeable)
      return if (alignmentLinePosition != null) {
        val line = beforeCrossAxisAlignmentLine - alignmentLinePosition
        if (layoutDirection == LayoutDirection.Rtl) {
          size - line
        } else {
          line
        }
      } else {
        0
      }
    }
  }

  private class VerticalCrossAxisAlignment(
    val vertical: Alignment.Vertical
  ) : CrossAxisAlignment() {
    override fun align(
      size: Int,
      layoutDirection: LayoutDirection,
      placeable: Placeable,
      beforeCrossAxisAlignmentLine: Int
    ): Int {
      return vertical.align(0, size)
    }
  }

  private class HorizontalCrossAxisAlignment(
    val horizontal: Alignment.Horizontal
  ) : CrossAxisAlignment() {
    override fun align(
      size: Int,
      layoutDirection: LayoutDirection,
      placeable: Placeable,
      beforeCrossAxisAlignmentLine: Int
    ): Int {
      return horizontal.align(0, size, layoutDirection)
    }
  }
}

/**
 * Box [Constraints], but which abstract away width and height in favor of main axis and cross axis.
 */
internal data class OrientationIndependentConstraints(
  val mainAxisMin: Int,
  val mainAxisMax: Int,
  val crossAxisMin: Int,
  val crossAxisMax: Int
) {
  constructor(c: Constraints, orientation: LayoutOrientation) : this(
    if (orientation === LayoutOrientation.Horizontal) c.minWidth else c.minHeight,
    if (orientation === LayoutOrientation.Horizontal) c.maxWidth else c.maxHeight,
    if (orientation === LayoutOrientation.Horizontal) c.minHeight else c.minWidth,
    if (orientation === LayoutOrientation.Horizontal) c.maxHeight else c.maxWidth
  )

  // Creates a new instance with the same main axis constraints and maximum tight cross axis.
  fun stretchCrossAxis() = OrientationIndependentConstraints(
    mainAxisMin,
    mainAxisMax,
    if (crossAxisMax != Constraints.Infinity) crossAxisMax else crossAxisMin,
    crossAxisMax
  )

  // Given an orientation, resolves the current instance to traditional constraints.
  fun toBoxConstraints(orientation: LayoutOrientation) =
    if (orientation === LayoutOrientation.Horizontal) {
      Constraints(mainAxisMin, mainAxisMax, crossAxisMin, crossAxisMax)
    } else {
      Constraints(crossAxisMin, crossAxisMax, mainAxisMin, mainAxisMax)
    }

  // Given an orientation, resolves the max width constraint this instance represents.
  fun maxWidth(orientation: LayoutOrientation) =
    if (orientation === LayoutOrientation.Horizontal) {
      mainAxisMax
    } else {
      crossAxisMax
    }

  // Given an orientation, resolves the max height constraint this instance represents.
  fun maxHeight(orientation: LayoutOrientation) =
    if (orientation === LayoutOrientation.Horizontal) {
      crossAxisMax
    } else {
      mainAxisMax
    }
}

private val IntrinsicMeasurable.data: RowColumnParentData?
  get() = parentData as? RowColumnParentData

private val RowColumnParentData?.weight: Float
  get() = this?.weight ?: 0f

private val RowColumnParentData?.fill: Boolean
  get() = this?.fill ?: true

private val RowColumnParentData?.crossAxisAlignment: CrossAxisAlignment?
  get() = this?.crossAxisAlignment

private val RowColumnParentData?.isRelative: Boolean
  get() = this.crossAxisAlignment?.isRelative ?: false


private fun intrinsicSize(
  children: List<IntrinsicMeasurable>,
  intrinsicMainSize: IntrinsicMeasurable.(Int) -> Int,
  intrinsicCrossSize: IntrinsicMeasurable.(Int) -> Int,
  crossAxisAvailable: Int,
  layoutOrientation: LayoutOrientation,
  intrinsicOrientation: LayoutOrientation
) = if (layoutOrientation == intrinsicOrientation) {
  intrinsicMainAxisSize(children, intrinsicMainSize, crossAxisAvailable)
} else {
  intrinsicCrossAxisSize(children, intrinsicCrossSize, intrinsicMainSize, crossAxisAvailable)
}

private fun intrinsicMainAxisSize(
  children: List<IntrinsicMeasurable>,
  mainAxisSize: IntrinsicMeasurable.(Int) -> Int,
  crossAxisAvailable: Int
): Int {
  var weightUnitSpace = 0
  var fixedSpace = 0
  var totalWeight = 0f
  children.fastForEach { child ->
    val weight = child.data.weight
    val size = child.mainAxisSize(crossAxisAvailable)
    if (weight == 0f) {
      fixedSpace += size
    } else if (weight > 0f) {
      totalWeight += weight
      weightUnitSpace = max(weightUnitSpace, (size / weight).roundToInt())
    }
  }
  return (weightUnitSpace * totalWeight).roundToInt() + fixedSpace
}

private fun intrinsicCrossAxisSize(
  children: List<IntrinsicMeasurable>,
  mainAxisSize: IntrinsicMeasurable.(Int) -> Int,
  crossAxisSize: IntrinsicMeasurable.(Int) -> Int,
  mainAxisAvailable: Int
): Int {
  var fixedSpace = 0
  var crossAxisMax = 0
  var totalWeight = 0f
  children.fastForEach { child ->
    val weight = child.data.weight
    if (weight == 0f) {
      // Ask the child how much main axis space it wants to occupy. This cannot be more
      // than the remaining available space.
      val mainAxisSpace = min(
        child.mainAxisSize(Constraints.Infinity),
        mainAxisAvailable - fixedSpace
      )
      fixedSpace += mainAxisSpace
      // Now that the assigned main axis space is known, ask about the cross axis space.
      crossAxisMax = max(crossAxisMax, child.crossAxisSize(mainAxisSpace))
    } else if (weight > 0f) {
      totalWeight += weight
    }
  }

  // For weighted children, calculate how much main axis space weight=1 would represent.
  val weightUnitSpace = if (totalWeight == 0f) {
    0
  } else if (mainAxisAvailable == Constraints.Infinity) {
    Constraints.Infinity
  } else {
    (max(mainAxisAvailable - fixedSpace, 0) / totalWeight).roundToInt()
  }

  children.fastForEach { child ->
    val weight = child.data.weight
    // Now the main axis for weighted children is known, so ask about the cross axis space.
    if (weight > 0f) {
      crossAxisMax = max(
        crossAxisMax,
        child.crossAxisSize((weightUnitSpace * weight).roundToInt())
      )
    }
  }
  return crossAxisMax
}

internal class LayoutWeightImpl(
  val weight: Float,
  val fill: Boolean,
  inspectorInfo: InspectorInfo.() -> Unit
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {
  override fun Density.modifyParentData(parentData: Any?) =
    ((parentData as? RowColumnParentData) ?: RowColumnParentData()).also {
      it.weight = weight
      it.fill = fill
    }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    val otherModifier = other as? LayoutWeightImpl ?: return false
    return weight != otherModifier.weight &&
        fill != otherModifier.fill
  }

  override fun hashCode(): Int {
    var result = weight.hashCode()
    result = 31 * result + fill.hashCode()
    return result
  }

  override fun toString(): String =
    "LayoutWeightImpl(weight=$weight, fill=$fill)"
}

internal sealed class SiblingsAlignedModifier(
  inspectorInfo: InspectorInfo.() -> Unit
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {
  abstract override fun Density.modifyParentData(parentData: Any?): Any?

  internal class WithAlignmentLineBlock(
    val block: (Measured) -> Int,
    inspectorInfo: InspectorInfo.() -> Unit
  ) : SiblingsAlignedModifier(inspectorInfo) {
    override fun Density.modifyParentData(parentData: Any?): Any? {
      return ((parentData as? RowColumnParentData) ?: RowColumnParentData()).also {
        it.crossAxisAlignment =
          CrossAxisAlignment.Relative(AlignmentLineProvider.Block(block))
      }
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      val otherModifier = other as? WithAlignmentLineBlock ?: return false
      return block == otherModifier.block
    }

    override fun hashCode(): Int = block.hashCode()

    override fun toString(): String = "WithAlignmentLineBlock(block=$block)"
  }

  internal class WithAlignmentLine(
    val line: AlignmentLine,
    inspectorInfo: InspectorInfo.() -> Unit
  ) : SiblingsAlignedModifier(inspectorInfo) {
    override fun Density.modifyParentData(parentData: Any?): Any? {
      return ((parentData as? RowColumnParentData) ?: RowColumnParentData()).also {
        it.crossAxisAlignment =
          CrossAxisAlignment.Relative(AlignmentLineProvider.Value(line))
      }
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      val otherModifier = other as? WithAlignmentLine ?: return false
      return line == otherModifier.line
    }

    override fun hashCode(): Int = line.hashCode()

    override fun toString(): String = "WithAlignmentLine(line=$line)"
  }
}

internal class HorizontalAlignModifier(
  val horizontal: Alignment.Horizontal,
  inspectorInfo: InspectorInfo.() -> Unit
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {
  override fun Density.modifyParentData(parentData: Any?): RowColumnParentData {
    return ((parentData as? RowColumnParentData) ?: RowColumnParentData()).also {
      it.crossAxisAlignment = CrossAxisAlignment.horizontal(horizontal)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    val otherModifier = other as? HorizontalAlignModifier ?: return false
    return horizontal == otherModifier.horizontal
  }

  override fun hashCode(): Int = horizontal.hashCode()

  override fun toString(): String =
    "HorizontalAlignModifier(horizontal=$horizontal)"
}

internal class VerticalAlignModifier(
  val vertical: Alignment.Vertical,
  inspectorInfo: InspectorInfo.() -> Unit
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {
  override fun Density.modifyParentData(parentData: Any?): RowColumnParentData {
    return ((parentData as? RowColumnParentData) ?: RowColumnParentData()).also {
      it.crossAxisAlignment = CrossAxisAlignment.vertical(vertical)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    val otherModifier = other as? VerticalAlignModifier ?: return false
    return vertical == otherModifier.vertical
  }

  override fun hashCode(): Int = vertical.hashCode()

  override fun toString(): String =
    "VerticalAlignModifier(vertical=$vertical)"
}

/**
 * Parent data associated with children.
 */
internal data class RowColumnParentData(
  var weight: Float = 0f,
  var fill: Boolean = true,
  var crossAxisAlignment: CrossAxisAlignment? = null
)

/**
 * Provides the alignment line.
 */
internal sealed class AlignmentLineProvider {
  abstract fun calculateAlignmentLinePosition(placeable: Placeable): Int?
  data class Block(val lineProviderBlock: (Measured) -> Int) : AlignmentLineProvider() {
    override fun calculateAlignmentLinePosition(
      placeable: Placeable
    ): Int? {
      return lineProviderBlock(placeable)
    }
  }

  data class Value(val line: AlignmentLine) : AlignmentLineProvider() {
    override fun calculateAlignmentLinePosition(placeable: Placeable): Int? {
      return placeable[line]
    }
  }
}
