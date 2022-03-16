package iris.collections

class IntGenericList(collection: IntList) : IntGenericCollection(collection), MutableList<Int> {

	override fun get(index: Int) = collection.get(index)

	override fun indexOf(element: Int) = collection.indexOf(element)

	override fun lastIndexOf(element: Int) = collection.lastIndexOf(element)

	override fun add(index: Int, element: Int) {
		TODO("Not yet implemented")
	}

	override fun addAll(index: Int, elements: Collection<Int>): Boolean {
		TODO("Not yet implemented")
	}

	override fun listIterator(): MutableListIterator<Int> {
		TODO("Not yet implemented")
	}

	override fun listIterator(index: Int): MutableListIterator<Int> {
		TODO("Not yet implemented")
	}

	override fun removeAt(index: Int) = collection.removeAt(index)

	override fun set(index: Int, element: Int): Int {
		val prev = collection[index]
		collection[index] = element
		return prev
	}

	override fun subList(fromIndex: Int, toIndex: Int): MutableList<Int> {
		TODO("Not yet implemented")
	}
}