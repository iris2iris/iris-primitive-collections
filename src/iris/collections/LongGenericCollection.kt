package iris.collections

open class LongGenericCollection(protected val collection: LongCollection) : MutableCollection<Long> {
	override val size: Int
		get() = collection.size

	override fun contains(element: Long): Boolean {
		return collection.contains(element)
	}

	override fun containsAll(elements: Collection<Long>): Boolean {
		return collection.containsAll(elements)
	}

	override fun isEmpty(): Boolean {
		return collection.isEmpty()
	}

	override fun add(element: Long): Boolean {
		collection.add(element)
		return true
	}

	override fun addAll(elements: Collection<Long>): Boolean {
		collection.addAll(elements)
		return true
	}

	override fun clear() {
		collection.clear()
	}

	override fun iterator(): MutableIterator<Long> {
		return collection.genericIterator()
	}

	override fun remove(element: Long): Boolean {
		val ind = collection.indexOf(element)
		if (ind == -1) return false
		collection.removeAt(ind)
		return true
	}

	override fun removeAll(elements: Collection<Long>): Boolean {
		TODO("Not yet implemented")
	}

	override fun retainAll(elements: Collection<Long>): Boolean {
		TODO("Not yet implemented")
	}
}