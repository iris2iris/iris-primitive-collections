package iris.collections

class LongGenericList(collection: LongList) : LongGenericCollection(collection), MutableList<Long> {

	override fun get(index: Int) = collection.get(index)

	override fun indexOf(element: Long) = collection.indexOf(element)

	override fun lastIndexOf(element: Long) = collection.lastIndexOf(element)

	override fun add(index: Int, element: Long) {
		TODO("Not yet implemented")
	}

	override fun addAll(index: Int, elements: Collection<Long>): Boolean {
		TODO("Not yet implemented")
	}

	override fun listIterator(): MutableListIterator<Long> {
		TODO("Not yet implemented")
	}

	override fun listIterator(index: Int): MutableListIterator<Long> {
		TODO("Not yet implemented")
	}

	override fun removeAt(index: Int) = collection.removeAt(index)

	override fun set(index: Int, element: Long): Long {
		val prev = collection[index]
		collection[index] = element
		return prev
	}

	override fun subList(fromIndex: Int, toIndex: Int): MutableList<Long> {
		TODO("Not yet implemented")
	}
}