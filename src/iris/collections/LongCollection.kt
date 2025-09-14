package iris.collections

import java.util.function.LongConsumer
import java.util.function.LongPredicate
import kotlin.text.Appendable

/**
 * @created 16.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
interface LongCollection : PrimitiveCollection<Long> {
	fun contains(o: Long): Boolean
	fun containsAll(c: LongCollection): Boolean
	fun containsAny(c: LongCollection): Boolean
	fun indexOf(o: Long): Int
	fun indexOfRange(o: Long, start: Int, end: Int): Int
	fun lastIndexOf(o: Long): Int
	fun lastIndexOfRange(o: Long, start: Int, end: Int): Int
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
	operator fun plusAssign(c: LongCollection)
	fun addAll(c: LongCollection)
	fun addAll(c: LongArray)
	fun iterator(): LongIterator
	fun subList(start: Int, end: Int): LongCollection
	override fun clone(): LongCollection

	interface LongIterator {
		fun hasNext(): Boolean
		fun next(): Long
		fun remove()
		fun forEachRemaining(action: LongConsumer)
	}

	fun forEachIndexed(action: LongActionIndexed)
	fun forEach(action: LongAction)
	fun forEachUntil(action: LongActionUntil)

	fun removeIf(filter: LongPredicate, i: Int, end: Int): Boolean
	fun removeIf(filter: LongPredicate): Boolean

	fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: LongTransform? = null): String
	//fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: ((el: Long, a: Appendable) -> Unit)? = null): String
	//fun joinTo(buffer: StringBuilder, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: LongTransform? = null): StringBuilder
	fun <A: Appendable>joinTo(buffer: A, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: LongTransform? = null): A

	interface LongAction {
		fun invoke(el: Long)
	}

	interface LongActionUntil {
		fun invoke(el: Long): Boolean
	}

	interface LongActionIndexed {
		fun invoke(index: Int, el: Long): Boolean
	}

	interface LongTransform {
		fun invoke(el: Long, sb: Appendable): Boolean
		fun invoke(el: Long, sb: StringBuilder): Boolean
	}

	abstract class LongTransformAbstract : LongTransform {
		override fun invoke(el: Long, sb: StringBuilder) = invoke(el, sb as Appendable)
	}
}

inline fun LongCollection.forEach(crossinline action: (e: Long) -> Unit) {
	forEach(object : LongCollection.LongAction {
		override fun invoke(el: Long) {
			action(el)
		}
	})
}
inline fun LongCollection.forEachUntil(crossinline action: (e: Long) -> Boolean) {
	forEachUntil (object : LongCollection.LongActionUntil {
		override fun invoke(el: Long): Boolean {
			return action(el)
		}
	})
}

inline fun LongCollection.joinToString(separator: CharSequence, prefix: CharSequence, postfix: CharSequence, limit: Int, truncated: CharSequence, crossinline transform: ((el: Long, a: Appendable) -> Unit)): String {
	return joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, object : LongCollection.LongTransformAbstract() {
		override fun invoke(el: Long, sb: Appendable): Boolean {
			transform(el, sb)
			return true
		}
	}).toString()
}

inline fun <T> Iterable<T>.mapLongs(transform: (T) -> Long): LongList {
	return mapLongsTo(LongArrayList(collectionSizeOrDefault1(10)), transform)
}

inline fun <T, C : LongCollection> Iterable<T>.mapLongsTo(destination: C, transform: (T) -> Long): C {
	for (item in this)
		destination.add(transform(item))
	return destination
}

inline fun <T> Array<T>.mapLongs(transform: (T) -> Long): LongList {
	return mapLongsTo(LongArrayList(this.size), transform)
}

inline fun <T, C : LongCollection> Array<T>.mapLongsTo(destination: C, transform: (T) -> Long): C {
	for (item in this)
		destination.add(transform(item))
	return destination
}

inline fun <T> Iterable<T>.mapLongsNotNull(transform: (T) -> Long): LongList {
	return mapLongsNotNullTo(LongArrayList(collectionSizeOrDefault1(10)), Long.MIN_VALUE, transform)
}

inline fun <T> Iterable<T>.mapLongsNotNull(nullable: Long, transform: (T) -> Long): LongList {
	return mapLongsNotNullTo(LongArrayList(collectionSizeOrDefault1(10)), nullable, transform)
}

inline fun <T, C : LongCollection> Iterable<T>.mapLongsNotNullTo(destination: C, nullable: Long, transform: (T) -> Long): C {
	forEach { element -> transform(element).let { if (it != nullable) destination.add(it) } }
	return destination
}

inline fun <T> Iterable<T>.filterLongs(predicate: (T) -> Boolean, transform: (T) -> Long): LongList {
	return filterLongsTo(LongArrayList(collectionSizeOrDefault1(10)), predicate, transform)
}

inline fun <T, C : LongCollection> Iterable<T>.filterLongsTo(destination: C, predicate: (T) -> Boolean, transform: (T) -> Long): C {
	for (item in this)
		if (predicate(item))
			destination.add(transform(item))
	return destination
}

fun LongCollection.chunked(size: Int): List<LongCollection> {
	val thisSize = this.size
	val step = size
	val resultCapacity = thisSize / step + if (thisSize % step == 0) 0 else 1
	val result = ArrayList<LongCollection>(resultCapacity)
	var curArr = LongArrayList(size)
	forEach {
		curArr += it
		if (curArr.size == size) {
			result += curArr
			curArr = LongArrayList(size)
		}
	}
	if (curArr.isNotEmpty())
		result += curArr
	return result
}

public inline fun <R> LongCollection.map(crossinline transform: (Long) -> R): List<R> {
	return mapTo(ArrayList<R>(this.size), transform)
}

public inline fun <R, C : MutableCollection<in R>> LongCollection.mapTo(destination: C, crossinline transform: (Long) -> R): C {
	forEach {
		destination.add(transform(it))
	}
	return destination
}

public inline fun <R> LongCollection.mapNotNull(crossinline transform: (Long) -> R?): List<R> {
	return mapToNotNull(ArrayList(this.size), transform)
}

public inline fun <R, C : MutableCollection<in R>> LongCollection.mapToNotNull(destination: C, crossinline transform: (Long) -> R?): C {
	forEach {
		transform(it)?.let { destination += it }
	}
	return destination
}

inline fun LongCollection.filter(crossinline predicate: (Long) -> Boolean): LongCollection {
	return filterTo(LongArrayList(), predicate)
}

inline fun <C : LongCollection>LongCollection.filterTo(destination: C, crossinline predicate: (Long) -> Boolean): C {
	forEach {
		if (predicate(it))
			destination += it
	}
	return destination
}

operator fun LongCollection.minus(other: Collection<Long>): LongCollection {
	if (other.isEmpty())
		return this.clone()
	return this.filter { it !in other }
}

inline fun LongCollection.any(crossinline predicate: (Long) -> Boolean): Boolean {
	var notFound = true
	forEachUntil {
		if (predicate(it))
			notFound = false
		notFound
	}
	return !notFound
}

inline fun LongCollection.all(crossinline predicate: (Long) -> Boolean): Boolean {
	var res = true
	forEachUntil {
		if (!predicate(it))
			res = false
		res
	}
	return res
}