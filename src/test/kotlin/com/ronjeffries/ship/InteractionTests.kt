package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class InteractionTests {
    @Test
    fun `can do an Interaction`() {
        val ship = Ship(Point(100.0,100.0))
        val int = Interaction()
        val trans = Transaction()
        int.interact(ship, ship, trans)
        val checker = TransactionChecker(trans)
        assertThat(checker.isEmpty()).isEqualTo(true)
    }

    @Test
    fun `missile does not kill ship if out of range`() {
        val ship = Ship(Point(100.0,100.0))
        val missile = Missile(Point(900.0,900.0))
        val int = Interaction()
        val trans = Transaction()
        int.interact(missile, ship, trans)
        val checker = TransactionChecker(trans)
        assertThat(checker.isEmpty()).isEqualTo(true)
    }

    @Test
    fun `missile does kill ship if in range`() {
        val ship = Ship(Point(100.0,100.0))
        val missile = missileAt(Point(110.0,100.0))
        val int = Interaction()
        val trans = Transaction()
        int.interact(missile, ship, trans)
        val checker = TransactionChecker(trans)
        checker.removes(ship)
        checker.removes(missile)
        val splats = checker.instances<Splat>()
        assertThat(splats.size).isEqualTo(1)
        val splat = splats.first()
        assertThat(splat.scale).isEqualTo(2.0)
    }
}

fun missileAt(p: Point): Missile {
    return Missile(p).also{ it. position = p}
}

class TransactionChecker(val trans: Transaction) {
    fun removes(o:SpaceObject) {
        assertThat(trans.removes).contains(o)
    }

    inline fun <reified T> instances(): List<T> {
        return trans.adds.filterIsInstance<T>()
    }

    fun isEmpty(): Boolean {
        if (trans.adds.isNotEmpty()) return false
        if (trans.removes.isNotEmpty()) return false
        if (trans.shouldClear) return false
        if ( trans.score > 0 ) return false
        return true
    }
}