package iris.collections

import kotlin.math.max

/**
 * @created 15.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
abstract class PrimiveAbstractList<E> {
	abstract val size: Int

	val lastIndex: Int get() = size - 1

	/**
	 * Returns `true` if this list contains no elements.
	 *
	 * @return `true` if this list contains no elements
	 */
	fun isEmpty(): Boolean {
		return size == 0
	}

	abstract fun trimToSize()
	abstract fun ensureCapacity(minCapacity: Int)
	abstract fun clone(): PrimiveAbstractList<E>
	abstract fun clear()
	abstract fun asCollection(): MutableCollection<E>

	protected fun newLength(oldLength: Int, minGrowth: Int, prefGrowth: Int): Int {
		val newLength = max(minGrowth, prefGrowth) + oldLength
		return if (newLength - IntArrayList.MAX_ARRAY_LENGTH <= 0) {
			newLength
		} else hugeLength(oldLength, minGrowth)
	}

	private fun hugeLength(oldLength: Int, minGrowth: Int): Int {
		val minLength = oldLength + minGrowth
		if (minLength < 0) { // overflow
			throw OutOfMemoryError("Required array length too large")
		}
		return if (minLength <= IntArrayList.MAX_ARRAY_LENGTH) {
			IntArrayList.MAX_ARRAY_LENGTH
		} else Int.MAX_VALUE
	}


	abstract operator fun plusAssign(c: Collection<E>)
}