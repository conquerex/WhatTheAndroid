package com.example.part11_32

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교재에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
class ControlTest {
    fun testIf(no: Int): String {
        val result = if (no > 30) 30
        else if (no > 20) 20
        else 10

        // no == 25
        // testIf(): 20
        return "testIf(): $result"
    }

    fun testWhen(x: Any): String {
        val result = when (x) {
            is String -> x.startsWith("http")
            else -> false
        }
        // x == http://www.google.com
        // testWhen() : x is start with http - true
        return "testWhen() : x is start with http - $result"
    }

    fun testFor(): String {
        val list = listOf("Hello", "World")
        var result: String = "testFor():"
        for ((index, value) in list.withIndex()) {
            result += "$index is $value"
        }

        // result += "$index is $value"
        // testFor():
        // testFor():0 is Hello
        // testFor():0 is Hello1 is World
        return result
    }
}