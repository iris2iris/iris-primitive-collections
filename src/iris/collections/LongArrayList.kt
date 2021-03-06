package iris.collections

import java.io.*
import java.lang.Appendable
import java.util.*
import java.util.function.Consumer
import java.util.function.LongConsumer
import java.util.function.LongPredicate
import kotlin.math.max
import kotlin.math.min

/**
 * @created 15.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class LongArrayList(initialCapacity: Int = DEFAULT_CAPACITY, collection: LongArrayList? = null) : LongList, PrimitiveAbstractList<Long>() {

	companion object {
		private const val DEFAULT_CAPACITY = 10
		const val MAX_ARRAY_LENGTH = Int.MAX_VALUE - 8
		private val EMPTY_ELEMENTDATA = LongArray(0)
	}

	constructor(init: LongArrayList): this(init.size, init)
	constructor(init: Collection<Long>): this(max(DEFAULT_CAPACITY, init.size)) {
		addAll(init)
	}

	private var elementData: LongArray

	init {
		val initialCapacity = max(initialCapacity, collection?.size ?: DEFAULT_CAPACITY)
		when {
			initialCapacity > 0 -> {
				elementData = LongArray(initialCapacity)
				collection?.run(::addAll)
			}
			initialCapacity == 0 -> elementData = EMPTY_ELEMENTDATA
			else -> throw IllegalArgumentException("Illegal Capacity: $initialCapacity")
		}
	}

	override var size = 0
		private set

	/**
	 * Trims the capacity of this `ArrayList` instance to be the
	 * list's current size.  An application can use this operation to minimize
	 * the storage of an `ArrayList` instance.
	 */
	override fun trimToSize() {
		if (size < elementData.size) {
			elementData = if (size == 0) EMPTY_ELEMENTDATA else elementData.copyOf(size)
		}
	}

	/**
	 * Increases the capacity of this `ArrayList` instance, if
	 * necessary, to ensure that it can hold at least the number of elements
	 * specified by the minimum capacity argument.
	 *
	 * @param minCapacity the desired minimum capacity
	 */
	override fun ensureCapacity(minCapacity: Int) {
		if (minCapacity > elementData.size)
			elementData = grow(minCapacity)
	}

	/**
	 * Increases the capacity to ensure that it can hold at least the
	 * number of elements specified by the minimum capacity argument.
	 *
	 * @param minCapacity the desired minimum capacity
	 * @throws OutOfMemoryError if minCapacity is less than zero
	 */
	private fun grow(minCapacity: Int): LongArray {
		val oldCapacity = elementData.size
		return if (oldCapacity > 0) {
			val newCapacity = newLength(
				oldCapacity,
				minCapacity - oldCapacity,  /* minimum growth */
				oldCapacity shr 1 /* preferred growth */
			)
			elementData.copyOf(newCapacity).also { elementData = it }
		} else {
			LongArray(max(DEFAULT_CAPACITY, minCapacity)).also { elementData = it }
		}
	}

	private fun grow(): LongArray {
		return grow(size + 1)
	}

	/**
	 * Returns `true` if this list contains the specified element.
	 * More formally, returns `true` if and only if this list contains
	 * at least one element `e` such that
	 * `Objects.equals(o, e)`.
	 *
	 * @param o element whose presence in this list is to be tested
	 * @return `true` if this list contains the specified element
	 */
	override operator fun contains(o: Long): Boolean {
		return indexOf(o) >= 0
	}

	override fun containsAll(c: Collection<Long>): Boolean {
		return c.all(::contains)
	}

	/**
	 * Returns the index of the first occurrence of the specified element
	 * in this list, or -1 if this list does not contain the element.
	 * More formally, returns the lowest index `i` such that
	 * `Objects.equals(o, get(i))`,
	 * or -1 if there is no such index.
	 */
	override fun indexOf(o: Long): Int {
		return indexOfRange(o, 0, size)
	}

	override fun indexOfRange(o: Long, start: Int, end: Int): Int {
		val es = elementData
		for (i in start until end) {
			if (o == es[i]) {
				return i
			}
		}
		return -1
	}

	/**
	 * Returns the index of the last occurrence of the specified element
	 * in this list, or -1 if this list does not contain the element.
	 * More formally, returns the highest index `i` such that
	 * `Objects.equals(o, get(i))`,
	 * or -1 if there is no such index.
	 */
	override fun lastIndexOf(o: Long): Int {
		return lastIndexOfRange(o, 0, size)
	}

	override fun lastIndexOfRange(o: Long, start: Int, end: Int): Int {
		val es = elementData
		for (i in end - 1 downTo start) {
			if (o == es[i]) {
				return i
			}
		}
		return -1
	}

	/**
	 * Returns a shallow copy of this `ArrayList` instance.  (The
	 * elements themselves are not copied.)
	 *
	 * @return a clone of this `ArrayList` instance
	 */
	override fun clone(): LongArrayList {
		return LongArrayList(this)
	}

	/**
	 * Returns an array containing all of the elements in this list
	 * in proper sequence (from first to last element).
	 *
	 *
	 * The returned array will be "safe" in that no references to it are
	 * maintained by this list.  (In other words, this method must allocate
	 * a new array).  The caller is thus free to modify the returned array.
	 *
	 *
	 * This method acts as bridge between array-based and collection-based
	 * APIs.
	 *
	 * @return an array containing all of the elements in this list in
	 * proper sequence
	 */
	override fun toArray(): LongArray {
		return elementData.copyOf(size)
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence (from first to last element); the runtime type of the returned
	 * array is that of the specified array.  If the list fits in the
	 * specified array, it is returned therein.  Otherwise, a new array is
	 * allocated with the runtime type of the specified array and the size of
	 * this list.
	 *
	 *
	 * If the list fits in the specified array with room to spare
	 * (i.e., the array has more elements than the list), the element in
	 * the array immediately following the end of the collection is set to
	 * `null`.  (This is useful in determining the length of the
	 * list *only* if the caller knows that the list does not contain
	 * any null elements.)
	 *
	 * @param a the array into which the elements of the list are to
	 * be stored, if it is big enough; otherwise, a new array of the
	 * same runtime type is allocated for this purpose.
	 * @return an array containing the elements of the list
	 * @throws ArrayStoreException if the runtime type of the specified array
	 * is not a supertype of the runtime type of every element in
	 * this list
	 * @throws NullPointerException if the specified array is null
	 */
	override fun toArray(a: LongArray): LongArray {
		return elementData.copyInto(a, 0, 0, min(a.size, size))
	}

	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param  index index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	override operator fun get(index: Int): Long {
		Objects.checkIndex(index, size)
		return elementData[index]
	}

	override fun elementAt(index: Int) = get(index)

	private fun elementAt(elementData: LongArray, index: Int): Long {
		Objects.checkIndex(index, size)
		return elementData[index]
	}

	/**
	 * Replaces the element at the specified position in this list with
	 * the specified element.
	 *
	 * @param index index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	override operator fun set(index: Int, element: Long) {
		Objects.checkIndex(index, size)
		elementData[index] = element
	}

	/**
	 * This helper method split out from add(E) to keep method
	 * bytecode size under 35 (the -XX:MaxInlineSize default value),
	 * which helps when add(E) is called in a C1-compiled loop.
	 */
	/*private fun add(e: Int*//*, elementData: LongArray*//*, s: Int) {
		//var elementData = elementData
		if (s == elementData.size) elementData = grow()
		elementData[s] = e
		size = s + 1
	}*/

	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param e element to be appended to this list
	 * @return `true` (as specified by [Collection.add])
	 */
	override fun add(e: Long) {
		if (size == elementData.size)
			elementData = grow()
		elementData[size++] = e
	}

	override operator fun plusAssign(e: Long) = add(e)


	/**
	 * Removes the element at the specified position in this list.
	 * Shifts any subsequent elements to the left (subtracts one from their
	 * indices).
	 *
	 * @param index the index of the element to be removed
	 * @return the element that was removed from the list
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	override fun removeAt(index: Int): Long {
		Objects.checkIndex(index, size)
		val es = elementData
		val oldValue = es[index]
		fastRemove(es, index)
		return oldValue
	}

	override fun fastRemoveAt(index: Int) {
		fastRemove(elementData, index)
	}

	override fun equalsRange(other: LongCollection, from: Int, to: Int): Boolean {
		var from = from
		val es = elementData
		val oit = other.iterator()
		while (from < to) {
			if (!oit.hasNext() || es[from] != oit.next()) {
				return false
			}
			from++
		}
		return !oit.hasNext()
	}

	private fun equalsArrayList(other: LongArrayList): Boolean {
		val s = size
		if (s != other.size) return false

		val otherEs = other.elementData
		val es = elementData
		for (i in 0 until s) {
			if (es[i] != otherEs[i])
				return false
		}
		return true
	}

	private fun hashCodeRange(from: Int, to: Int): Int {
		val es = elementData
		var hashCode = 1
		for (i in from until to) {
			val e = es[i]
			hashCode = 31 * hashCode + e.hashCode()
		}
		return hashCode
	}

	/**
	 * Private remove method that skips bounds checking and does not
	 * return the value removed.
	 */
	private fun fastRemove(es: LongArray, i: Int) {
		val newSize = --size
		if (newSize > i)
			System.arraycopy(es, i + 1, es, i, newSize - i)
	}

	/**
	 * Removes all of the elements from this list.  The list will
	 * be empty after this call returns.
	 */
	override fun clear() {
		size = 0
	}

	override operator fun plusAssign(c: Collection<Long>) {
		addAll(c)
	}

	override fun addAll(c: Collection<Long>) {
		val a = c.toLongArray()
		val numNew = a.size
		if (numNew == 0) return
		var elementData: LongArray
		val s: Int
		if (numNew > this.elementData.also { elementData = it }.size - size.also { s = it })
			elementData = grow(s + numNew)

		for (i in a.indices) {
			elementData[size + i] = a[i]
		}
		size = s + numNew
		return
	}

	operator fun plusAssign(c: LongArrayList) {
		addAll(c)
	}

	override operator fun plusAssign(c: LongCollection) {
		addAll(c)
	}

	override fun addAll(c: LongCollection) {
		if (c is LongArrayList) {
			addAll(c)
			return
		}
		ensureCapacity(size + c.size)
		val itr = c.iterator()
		while (itr.hasNext())
			add(itr.next())
	}

	fun addAll(c: LongArrayList) {
		val a: LongArray = c.elementData
		val numNew = c.size
		if (numNew == 0) return
		var elementData: LongArray
		val s: Int
		if (numNew > this.elementData.also { elementData = it }.size - size.also { s = it })
			elementData = grow(s + numNew)

		for (i in 0 until numNew) {
			elementData[size + i] = a[i]
		}
		size = s + numNew
	}

	operator fun plusAssign(c: LongArray) {
		addAll(c)
	}

	override fun addAll(c: LongArray) {
		val a: LongArray = c
		val numNew = c.size
		if (numNew == 0) return
		var elementData: LongArray
		val s: Int
		if (numNew > this.elementData.also { elementData = it }.size - size.also { s = it })
			elementData = grow(s + numNew)

		for (i in 0 until numNew) {
			elementData[size + i] = a[i]
		}
		size = s + numNew
	}

	/**
	 * Inserts all of the elements in the specified collection into this
	 * list, starting at the specified position.  Shifts the element
	 * currently at that position (if any) and any subsequent elements to
	 * the right (increases their indices).  The new elements will appear
	 * in the list in the order that they are returned by the
	 * specified collection's iterator.
	 *
	 * @param index index at which to insert the first element from the
	 * specified collection
	 * @param c collection containing elements to be added to this list
	 * @return `true` if this list changed as a result of the call
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 * @throws NullPointerException if the specified collection is null
	 */
	// TODO
	/*fun addAll(index: Int, c: Collection<Int>): Boolean {
		rangeCheckForAdd(index)
		val a: Array<Any> = c.toTypedArray()
		modCount++
		val numNew = a.size
		if (numNew == 0) return false
		var elementData: Array<Any?>?
		val s: Int
		if (numNew > this.elementData.also { elementData = it }.size - size.also { s = it }) elementData =
			grow(s + numNew)
		val numMoved = s - index
		if (numMoved > 0) System.arraycopy(
			elementData, index,
			elementData, index + numNew,
			numMoved
		)
		System.arraycopy(a, 0, elementData, index, numNew)
		size = s + numNew
		return true
	}*/

	/**
	 * Removes from this list all of the elements whose index is between
	 * `fromIndex`, inclusive, and `toIndex`, exclusive.
	 * Shifts any succeeding elements to the left (reduces their index).
	 * This call shortens the list by `(toIndex - fromIndex)` elements.
	 * (If `toIndex==fromIndex`, this operation has no effect.)
	 *
	 * @throws IndexOutOfBoundsException if `fromIndex` or
	 * `toIndex` is out of range
	 * (`fromIndex < 0 ||
	 * toIndex > size() ||
	 * toIndex < fromIndex`)
	 */
	private fun removeRange(fromIndex: Int, toIndex: Int) {
		if (fromIndex > toIndex) {
			throw IndexOutOfBoundsException(
				outOfBoundsMsg(fromIndex, toIndex)
			)
		}
		shiftTailOverGap(elementData, fromIndex, toIndex)
	}

	/** Erases the gap from lo to hi, by sliding down following elements.  */
	private fun shiftTailOverGap(es: LongArray, lo: Int, hi: Int) {
		System.arraycopy(es, hi, es, lo, size - hi)
		val to = size
		var i = hi - lo.let { size -= it; size }
	}

	/**
	 * A version of rangeCheck used by add and addAll.
	 */
	/*private fun rangeCheckForAdd(index: Int) {
		if (index > size || index < 0) throw IndexOutOfBoundsException(outOfBoundsMsg(index))
	}*/

	/**
	 * Constructs an IndexOutOfBoundsException detail message.
	 * Of the many possible refactorings of the error handling code,
	 * this "outlining" performs best with both server and client VMs.
	 */
	/*private fun outOfBoundsMsg(index: Int): String {
		return "Index: $index, Size: $size"
	}*/

	/**
	 * A version used in checking (fromIndex > toIndex) condition
	 */
	private fun outOfBoundsMsg(fromIndex: Int, toIndex: Int): String {
		return "From Index: $fromIndex > To Index: $toIndex"
	}
	/**
	 * Returns an iterator over the elements in this list in proper sequence.
	 *
	 *
	 * The returned iterator is [*fail-fast*](#fail-fast).
	 *
	 * @return an iterator over the elements in this list in proper sequence
	 */
	override fun iterator(): LongArrayIterator {
		return LongArrayIterator()
	}

	inner class LongArrayIterator : LongCollection.LongIterator {
		var cursor = 0
		var lastRet = -1

		override fun hasNext(): Boolean {
			return cursor != size
		}

		override fun next(): Long {
			val i = cursor
			if (i >= size) throw NoSuchElementException()
			val elementData: LongArray = this@LongArrayList.elementData
			if (i >= elementData.size) throw ConcurrentModificationException()
			cursor = i + 1
			return elementData[i.also { lastRet = it }]
		}

		override fun remove() {
			check(lastRet >= 0)
			try {
				this@LongArrayList.removeAt(lastRet)
				cursor = lastRet
				lastRet = -1
			} catch (ex: IndexOutOfBoundsException) {
				throw ConcurrentModificationException()
			}
		}

		override fun forEachRemaining(action: LongConsumer) {
			Objects.requireNonNull(action)
			val size: Int = this@LongArrayList.size
			var i = cursor
			if (i < size) {
				val es = elementData
				if (i >= es.size) throw ConcurrentModificationException()
				while (i < size) {
					action.accept(elementAt(es, i))
					i++
				}
				// update once at end to reduce heap write traffic
				cursor = i
				lastRet = i - 1
			}
		}
	}


	/**
	 * An optimized version of AbstractList.Itr
	 */
	private inner class Itr : MutableIterator<Long> {
		var cursor = 0
		var lastRet = -1

		override fun hasNext(): Boolean {
			return cursor != size
		}

		override fun next(): Long {
			val i = cursor
			if (i >= size) throw NoSuchElementException()
			val elementData: LongArray = this@LongArrayList.elementData
			if (i >= elementData.size) throw ConcurrentModificationException()
			cursor = i + 1
			return elementData[i.also { lastRet = it }]
		}

		override fun remove() {
			check(lastRet >= 0)
			try {
				this@LongArrayList.removeAt(lastRet)
				cursor = lastRet
				lastRet = -1
			} catch (ex: IndexOutOfBoundsException) {
				throw ConcurrentModificationException()
			}
		}

		override fun forEachRemaining(action: Consumer<in Long>) {
			Objects.requireNonNull(action)
			val size: Int = this@LongArrayList.size
			var i = cursor
			if (i < size) {
				val es = elementData
				if (i >= es.size) throw ConcurrentModificationException()
				while (i < size) {
					action.accept(elementAt(es, i))
					i++
				}
				// update once at end to reduce heap write traffic
				cursor = i
				lastRet = i - 1
			}
		}
	}

	/**
	 * @throws NullPointerException {@inheritDoc}
	 */
	override fun forEach(action: (e: Long) -> Unit) {
		val es = elementData
		val size = size
		var i = 0
		while (i < size) {
			action(es[i])
			i++
		}
	}

	override fun forEachIndexed(action: (index: Int, e: Long) -> Unit) {
		val es = elementData
		val size = size
		var i = 0
		while (i < size) {
			action(i, es[i])
			i++
		}
	}

	// A tiny bit set implementation
	private fun nBits(n: Int): LongArray {
		return LongArray((n - 1 shr 6) + 1)
	}

	private fun setBit(bits: LongArray, i: Int) {
		bits[i shr 6] = bits[i shr 6] or (1L shl i)
	}

	private fun isClear(bits: LongArray, i: Int): Boolean {
		return bits[i shr 6] and (1L shl i) == 0L
	}

	/**
	 * @throws NullPointerException {@inheritDoc}
	 */
	override fun removeIf(filter: LongPredicate): Boolean {
		return removeIf(filter, 0, size)
	}

	/**
	 * Removes all elements satisfying the given predicate, from index
	 * i (inclusive) to index end (exclusive).
	 */
	override fun removeIf(filter: LongPredicate, i: Int, end: Int): Boolean {
		var i = i
		Objects.requireNonNull(filter)
		val es = elementData
		// Optimize for initial run of survivors
		while (i < end && !filter.test(elementAt(es, i))) {
			i++
		}
		// Tolerate predicates that reentrantly access the collection for
		// read (but writers still get CME), so traverse once to find
		// elements to delete, a second pass to physically expunge.
		return if (i < end) {
			val beg = i
			val deathRow = nBits(end - beg)
			deathRow[0] = 1L // set bit 0
			i = beg + 1
			while (i < end) {
				if (filter.test(elementAt(es, i))) setBit(deathRow, i - beg)
				i++
			}
			var w = beg
			i = beg
			while (i < end) {
				if (isClear(deathRow, i - beg)) es[w++] = es[i]
				i++
			}
			shiftTailOverGap(es, w, end)
			true
		} else {
			false
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as LongArrayList
		return equalsArrayList(other)
	}

	override fun hashCode(): Int {
		var result = hashCodeRange(0, size)
		result = 31 * result + size
		return result
	}

	override fun toString(): String {
		return joinToString(limit = 100, prefix = "[", postfix = "]")
	}

	override fun asGeneric(): LongGenericList {
		return LongGenericList(this)
	}

	override fun genericIterator(): MutableIterator<Long> {
		return Itr()
	}

	override fun joinToString(separator: CharSequence, prefix: CharSequence, postfix: CharSequence, limit: Int, truncated: CharSequence, transform: LongCollection.LongTransform?): String {
		return joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
	}

	override fun <A: Appendable>joinTo(buffer: A, separator: CharSequence, prefix: CharSequence, postfix: CharSequence, limit: Int, truncated: CharSequence, transform: LongCollection.LongTransform?): A {
		if (buffer is StringBuilder)
			return joinTo(buffer, separator, prefix, postfix, limit, truncated, transform) as A

		buffer.append(prefix)
		var count = 0
		for (i in 0 until size) {
			val element = elementData[i]
			if (++count > 1) buffer.append(separator)
			if (limit < 0 || count <= limit) {
				appendElement(buffer, element, transform)
			} else break
		}
		if (limit >= 0 && count > limit) buffer.append(truncated)
		buffer.append(postfix)
		return buffer
	}

	override fun joinTo(buffer: StringBuilder, separator: CharSequence, prefix: CharSequence, postfix: CharSequence, limit: Int, truncated: CharSequence, transform: LongCollection.LongTransform?): StringBuilder {
		buffer.append(prefix)
		var count = 0
		for (i in 0 until size) {
			val element = elementData[i]
			if (++count > 1) buffer.append(separator)
			if (limit < 0 || count <= limit) {
				appendElement(buffer, element, transform)
			} else break
		}
		if (limit >= 0 && count > limit) buffer.append(truncated)
		buffer.append(postfix)
		return buffer
	}

	private fun appendElement(buffer: StringBuilder, element: Long, transform: LongCollection.LongTransform?) {
		when (transform) {
			null -> buffer.append(element)
			else -> transform.invoke(element, buffer)
		}
	}

	private fun appendElement(buffer: Appendable, element: Long, transform: LongCollection.LongTransform?) {
		when (transform) {
			null -> buffer.append(element.toString())
			else -> transform.invoke(element, buffer)
		}
	}
}

