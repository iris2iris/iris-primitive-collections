package iris.collections

import kotlin.math.max

/**
 * @created 15.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
abstract class PrimitiveAbstractList<E> : PrimitiveCollection<E> {

	val lastIndex: Int get() = size - 1

	override fun isEmpty(): Boolean {
		return size == 0
	}

	override fun isNotEmpty() = !isEmpty()

	abstract fun trimToSize()
	abstract fun ensureCapacity(minCapacity: Int)

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