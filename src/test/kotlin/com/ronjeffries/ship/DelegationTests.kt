package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

interface DoSomething {
    fun doingIt(input: Int): Int
    fun doingOtherThing(input: Int) :Int
}

class BasicDoer: DoSomething {
    override fun doingIt(input: Int): Int = input

    override fun doingOtherThing(input: Int): Int = input + 7
}

class OneDoer: DoSomething by BasicDoer() {
}

class TwoDoer: DoSomething by BasicDoer() {
    override fun doingIt(input: Int): Int = 3*input
}

class DelegationTests {
    @Test
    fun `doers do it`() {
        assertThat(OneDoer().doingIt(2)).isEqualTo(2)
        assertThat(TwoDoer().doingIt(2)).isEqualTo(6)
        assertThat(OneDoer().doingOtherThing(1)).isEqualTo(8)
        assertThat(OneDoer().doingOtherThing(-5)).isEqualTo(2)
    }
}