package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

interface DoSomething {
    fun doingIt(input: Int): Int
    fun doingOtherThing(input: Int) :Int
    fun register(client: DoerClient)
}

interface DoerClient {
    fun provideNumber(): Int
}

class BasicDoer(): DoSomething {
    private var client: DoerClient? = null
    override fun doingIt(input: Int): Int = input + (client?.provideNumber() ?: 0)
    override fun doingOtherThing(input: Int): Int = input + 7
    override fun register(client: DoerClient) { this.client = client }
}

class OneDoer(doer: BasicDoer = BasicDoer()): DoerClient, DoSomething by doer {
    init { register(this) }

    override fun provideNumber(): Int {
        return 600
    }
}

class TwoDoer: DoSomething by BasicDoer() {
    override fun doingIt(input: Int): Int = 3*input
}

class DelegationTests {
    @Test
    fun `doers do it`() {
        assertThat(OneDoer().doingIt(2)).isEqualTo(602)
        assertThat(TwoDoer().doingIt(2)).isEqualTo(6)
        assertThat(OneDoer().doingOtherThing(1)).isEqualTo(8)
        assertThat(OneDoer().doingOtherThing(-5)).isEqualTo(2)
    }
}