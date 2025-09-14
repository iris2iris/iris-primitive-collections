package iris.collections

import java.util.*
import java.util.function.IntConsumer
import java.util.function.IntPredicate

/**
 * @created 16.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
interface IntCollection : PrimitiveCollection<Int> {
	fun contains(o: Int): Boolean
	fun containsAll(c: IntCollection): Boolean
	fun containsAny(c: IntCollection): Boolean
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
	override fun clone(): IntCollection

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

	fun forEachIndexed(action: IntActionIndexed)
	fun forEach(action: IntAction)
	fun forEachUntil(action: IntActionUntil)
	fun removeIf(filter: IntPredicate, i: Int, end: Int): Boolean
	fun removeIf(filter: IntPredicate): Boolean

	fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntTransform? = null): String
	fun joinTo(buffer: StringBuilder, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntTransform? = null): StringBuilder
	fun <A: Appendable>joinTo(buffer: A, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntTransform? = null): A

	interface IntActionUntil {
		fun invoke(el: Int): Boolean
	}

	interface IntAction {
		fun invoke(el: Int)
	}

	interface IntActionIndexed {
		fun invoke(index: Int, el: Int): Boolean
	}

	interface IntTransform {
		fun invoke(el: Int, sb: Appendable): Boolean
		fun invoke(el: Int, sb: StringBuilder): Boolean
	}

	abstract class IntTransformAbstract : IntTransform {
		override fun invoke(el: Int, sb: StringBuilder): Boolean = invoke(el, sb as Appendable)
	}


}

inline fun IntCollection.forEachUntil(crossinline action: (e: Int) -> Boolean) {
	forEachUntil(object : IntCollection.IntActionUntil {
		override fun invoke(el: Int): Boolean {
			return action(el)
		}
	})
}

inline fun IntCollection.forEach(crossinline action: (e: Int) -> Unit) {
	forEach(object : IntCollection.IntAction {
		override fun invoke(el: Int) {
			action(el)
		}
	})
}

inline fun IntCollection.forEachIndexed(crossinline action: (index: Int, e: Int) -> Boolean) {
	forEachIndexed(object : IntCollection.IntActionIndexed {
		override fun invoke(index: Int, el: Int): Boolean {
			return action(index, el)
		}
	})
}

fun <T> Iterable<T>.collectionSizeOrDefault1(default: Int): Int = if (this is Collection<*>) this.size else default

inline fun <T> Iterable<T>.mapInts(transform: (T) -> Int): IntList {
	return mapIntsTo(IntArrayList(collectionSizeOrDefault1(10)), transform)
}

inline fun <T, C : IntCollection> Iterable<T>.mapIntsTo(destination: C, transform: (T) -> Int): C {
	for (item in this)
		destination.add(transform(item))
	return destination
}

inline fun <T> Iterable<T>.mapIntsNotNull(transform: (T) -> Int): IntList {
	return mapIntsNotNullTo(IntArrayList(collectionSizeOrDefault1(10)), Int.MIN_VALUE, transform)
}

inline fun <T> Iterable<T>.mapIntsNotNull(nullable: Int, transform: (T) -> Int): IntList {
	return mapIntsNotNullTo(IntArrayList(collectionSizeOrDefault1(10)), nullable, transform)
}

inline fun <T, C : IntCollection> Iterable<T>.mapIntsNotNullTo(destination: C, nullable: Int, transform: (T) -> Int): C {
	forEach { element -> transform(element).let { if (it != nullable) destination.add(it) } }
	return destination
}

inline fun <T> Array<T>.mapInts(transform: (T) -> Int): IntList {
	return mapIntsTo(IntArrayList(this.size), transform)
}

inline fun <T, C : IntCollection> Array<T>.mapIntsTo(destination: C, transform: (T) -> Int): C {
	for (item in this)
		destination.add(transform(item))
	return destination
}

inline fun <T> Iterable<T>.filterInts(predicate: (T) -> Boolean, transform: (T) -> Int): IntList {
	return filterIntsTo(IntArrayList(collectionSizeOrDefault1(10)), predicate, transform)
}

inline fun <T, C : IntCollection> Iterable<T>.filterIntsTo(destination: C, predicate: (T) -> Boolean, transform: (T) -> Int): C {
	for (item in this)
		if (predicate(item))
			destination.add(transform(item))
	return destination
}

fun IntCollection.chunked(size: Int): List<IntCollection> {
	val thisSize = this.size
	val step = size
	val resultCapacity = thisSize / step + if (thisSize % step == 0) 0 else 1
	val result = ArrayList<IntCollection>(resultCapacity)
	var curArr = IntArrayList(size)
	forEach {
		curArr += it
		if (curArr.size == size) {
			result += curArr
			curArr = IntArrayList(size)
		}
		true
	}
	if (curArr.isNotEmpty())
		result += curArr
	return result
}

public inline fun <R> IntCollection.map(crossinline transform: (Int) -> R): List<R> {
	return mapTo(ArrayList<R>(this.size), transform)
}

public inline fun <R, C : MutableCollection<in R>> IntCollection.mapTo(destination: C, crossinline transform: (Int) -> R): C {
	forEach {
		destination.add(transform(it))
	}
	return destination
}

public inline fun <R> IntCollection.mapNotNull(crossinline transform: (Int) -> R?): List<R> {
	return mapToNotNull(ArrayList(this.size), transform)
}

public inline fun <R, C : MutableCollection<in R>> IntCollection.mapToNotNull(destination: C, crossinline transform: (Int) -> R?): C {
	forEach {
		transform(it)?.let { destination += it }
	}
	return destination
}

inline fun IntCollection.filter(crossinline predicate: (Int) -> Boolean): IntCollection {
	return filterTo(IntArrayList(), predicate)
}

inline fun <C : IntCollection>IntCollection.filterTo(destination: C, crossinline predicate: (Int) -> Boolean): C {
	forEach {
		if (predicate(it))
			destination += it
	}
	return destination
}

operator fun IntCollection.minus(other: Collection<Int>): IntCollection {
	if (other.isEmpty())
		return this.clone()
	return this.filter { it !in other }
}

inline fun IntCollection.any(crossinline predicate: (Int) -> Boolean): Boolean {
	var notFound = true
	forEachUntil {
		if (predicate(it))
			notFound = false
		notFound
	}
	return !notFound
}

inline fun IntCollection.all(crossinline predicate: (Int) -> Boolean): Boolean {
	var res = true
	forEachUntil {
		if (!predicate(it))
			res = false
		res
	}
	return res
}