package iris.collections

/**
 * @created 16.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
interface PrimitiveCollection<E> {
	val size: Int
	fun isEmpty(): Boolean
	fun isNotEmpty(): Boolean
	fun clone(): PrimitiveCollection<E>
	fun clear()
	fun asGeneric(): MutableCollection<E>
}