package iris.collections

import java.lang.Appendable

open class IntGenericCollection(protected val collection: IntCollection) : MutableCollection<Int> {
	override val size: Int
		get() = collection.size

	override fun contains(element: Int): Boolean {
		return collection.contains(element)
	}

	override fun containsAll(elements: Collection<Int>): Boolean {
		return collection.containsAll(elements)
	}

	override fun isEmpty(): Boolean {
		return collection.isEmpty()
	}

	override fun add(element: Int): Boolean {
		collection.add(element)
		return true
	}

	override fun addAll(elements: Collection<Int>): Boolean {
		collection.addAll(elements)
		return true
	}

	override fun clear() {
		collection.clear()
	}

	override fun iterator(): MutableIterator<Int> {
		return collection.genericIterator()
	}

	override fun remove(element: Int): Boolean {
		val ind = collection.indexOf(element)
		if (ind == -1) return false
		collection.removeAt(ind)
		return true
	}

	override fun removeAll(elements: Collection<Int>): Boolean {
		TODO("Not yet implemented")
	}

	override fun retainAll(elements: Collection<Int>): Boolean {
		TODO("Not yet implemented")
	}

	fun joinToString(separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntCollection.IntTransform? = null): String {
		return collection.joinToString(separator, prefix, postfix, limit, truncated, transform)
	}

	fun <A: Appendable>joinTo(buffer: A, separator: CharSequence = ", ", prefix: CharSequence = "", postfix: CharSequence = "", limit: Int = -1, truncated: CharSequence = "...", transform: IntCollection.IntTransform? = null): A {
		return collection.joinTo(buffer, separator, prefix, postfix, limit, truncated, transform)
	}
}