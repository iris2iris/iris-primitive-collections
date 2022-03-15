package iris.collections

import java.util.ArrayList

/**
 * @created 15.03.2022
 * @author [Ivan Ivanov](https://t.me/irisism)
 */

object IntTest {
	@JvmStatic
	fun main(args: Array<String>) {
		mapTo()
	}

	fun test() {
		val t = IntArrayList()
		t += 1
		t += 2
		t += 3
		t.addAll(intArrayOf(5, 6, 8))
		t += intArrayOf(5, 6, 8)
		t[1] = -1
		println(t)
		t.removeAt(1)
		println(t)
		t.removeAt(t.size - 1)

		println(t)
	}

	fun mapTo() {
		val dd = ArrayList<Int>().also { repeat(10) { i -> it += -i } }
		val arr = IntArrayList()
		dd.mapTo(arr.asCollection()) { it }
		println(arr)
		arr.forEach {
			println(it)
		}

		arr.forEachIndexed { i, el ->
			println("$i: $el")
		}
	}
}