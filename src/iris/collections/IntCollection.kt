package iris.collections

import java.util.function.IntConsumer
import java.util.function.IntPredicate

/**
 * @created 16.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
interface IntCollection : PrimitiveCollection<Int> {
	fun contains(o: Int): Boolean

	fun indexOf(o: Int): Int
	fun indexOfRange(o: Int, start: Int, end: Int): Int
	fun lastIndexOf(o: Int): Int
	fun lastIndexOfRange(o: Int, start: Int, end: Int): Int
	fun toArray(): IntArray
	fun toArray(a: IntArray): IntArray
	operator fun get(index: Int): Int
	fun elementAt(index: Int): Int
	operator fun set(index: Int, element: Int)
	fun add(e: Int)
	operator fun plusAssign(e: Int)
	fun removeAt(index: Int): Int
	fun fastRemoveAt(index: Int)
	fun equalsRange(other: IntCollection, from: Int, to: Int): Boolean

	operator fun plusAssign(c: IntCollection)
	fun addAll(c: IntCollection)
	fun addAll(c: IntArray)
	fun iterator(): IntIterator

	interface IntIterator {
		fun hasNext(): Boolean
		fun next(): Int
		fun remove()
		fun forEachRemaining(action: IntConsumer)
	}

	fun forEachIndexed(action: (index: Int, e: Int) -> Unit)
	fun forEach(action: (e: Int) -> Unit)
	fun removeIf(filter: IntPredicate, i: Int, end: Int): Boolean
	fun removeIf(filter: IntPredicate): Boolean

	fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntTransform? = null): String
	fun joinTo(buffer: StringBuilder, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntTransform? = null): StringBuilder
	fun <A: Appendable>joinTo(buffer: A, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntTransform? = null): A

	interface IntTransform {
		fun invoke(el: Int, sb: Appendable)
		fun invoke(el: Int, sb: StringBuilder)
	}

	abstract class IntTransformAbstract : IntTransform {
		override fun invoke(el: Int, sb: StringBuilder) = invoke(el, sb as Appendable)
	}
}

