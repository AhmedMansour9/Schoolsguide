package com.eaapps.schoolsguide

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun testChange() {
        val listFields = arrayListOf<Value>(
            Value("1", "Ahmed"),
            Value("2", "islam"),
            Value("3", "Ahmed"),
            Value("4", "islam"),
            Value("5", "Ahmed"),
            Value("6", "islam")
        )

        val fieldHash = HashSet<String>()

        listFields.forEach {
            fieldHash.add(it.field)
        }

        val newFieldList = HashSet<Value>()
        fieldHash.forEach { set ->
            var valueCompact = ""
            listFields.forEachIndexed { index, value ->
                if (value.field == set) {
                    valueCompact += value.value + ","
                }
            }
            newFieldList.add(Value(valueCompact.substring(0, valueCompact.length - 1), set))
        }

        // print result
        newFieldList.forEach {
            println(it)

        }


    }

}

data class Value(var value: String = "", var field: String = "")
