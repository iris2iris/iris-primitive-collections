package iris.collections

import java.util.ArrayList

/**
 * @created 15.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */
object LongTest {
	@JvmStatic
	fun main(args: Array<String>) {
		test()
		mapTo()
	}

	fun test() {
		val t = LongArrayList()
		t += 1
		t += 2
		t += 3
		t.addAll(longArrayOf(5, 6, 8))
		t += longArrayOf(5, 6, 8)
		t[1] = -1
		println(t)
		t.removeAt(1)
		println(t)
		t.removeAt(t.size - 1)

		println(t)
	}

	fun mapTo() {
		val dd = ArrayList<Long>().also { repeat(10) { i -> it += (-i).toLong() } }
		val arr = LongArrayList()
		dd.mapTo(arr.asCollection()) { it }
		println(arr)
		arr.forEach {
			println(it)
		}
	}
}