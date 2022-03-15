package iris.collections

import java.util.function.IntConsumer
import java.util.function.IntPredicate

/**
 * @created 16.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
interface IntCollection {
	val size: Int
	fun contains(o: Int): Boolean
	fun containsAll(c: Collection<Int>): Boolean
	fun indexOf(o: Int): Int
	fun indexOfRange(o: Int, start: Int, end: Int): Int
	fun lastIndexOf(o: Int): Int
	fun lastIndexOfRange(o: Int, start: Int, end: Int): Int
	fun clone(): IntCollection
	fun clear()
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
	fun asCollection(): MutableCollection<Int>
	fun addAll(c: Collection<Int>)
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
}