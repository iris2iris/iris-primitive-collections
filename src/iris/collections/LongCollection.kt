package iris.collections

import java.util.function.Consumer
import java.util.function.LongConsumer
import java.util.function.LongPredicate
import java.util.function.Predicate

/**
 * @created 16.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
interface LongCollection {
	val size: Int
	fun contains(o: Long): Boolean
	fun containsAll(c: Collection<Long>): Boolean
	fun indexOf(o: Long): Int
	fun indexOfRange(o: Long, start: Int, end: Int): Int
	fun lastIndexOf(o: Long): Int
	fun lastIndexOfRange(o: Long, start: Int, end: Int): Int
	fun clone(): LongCollection
	fun clear()
	fun toArray(): LongArray
	fun toArray(a: LongArray): LongArray
	operator fun get(index: Int): Long
	fun elementAt(index: Int): Long
	operator fun set(index: Int, element: Long)
	fun add(e: Long)
	operator fun plusAssign(e: Long)
	fun removeAt(index: Int): Long
	fun fastRemoveAt(index: Int)
	fun equalsRange(other: LongCollection, from: Int, to: Int): Boolean
	fun asCollection(): MutableCollection<Long>
	fun addAll(c: Collection<Long>)
	operator fun plusAssign(c: LongCollection)
	fun addAll(c: LongCollection)
	fun addAll(c: LongArray)
	fun iterator(): LongIterator

	interface LongIterator {
		fun hasNext(): Boolean
		fun next(): Long
		fun remove()
		fun forEachRemaining(action: LongConsumer)
	}

	fun forEachIndexed(action: (index: Int, e: Long) -> Unit)
	fun forEach(action: (e: Long) -> Unit)
	fun removeIf(filter: LongPredicate, i: Int, end: Int): Boolean
	fun removeIf(filter: LongPredicate): Boolean
}