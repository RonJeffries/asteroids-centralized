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
        assertThat(trans.isEmpty()).isEqualTo(true)
    }

    @Test
    fun `missile does not kill ship if out of range`() {
        val ship = Ship(Point(100.0,100.0))
        val missile = Missile(Point(900.0,900.0))
        val int = Interaction()
        val trans = Transaction()
        int.interact(missile, ship, trans)
        assertThat(trans.isEmpty()).isEqualTo(true)
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

fun Transaction.isEmpty(): Boolean {
    if (adds.isNotEmpty()) return false
    if (removes.isNotEmpty()) return false
    if (shouldClear) return false
    if ( score > 0 ) return false
    return true
}

class TransactionChecker(val trans: Transaction) {
    fun removes(o:SpaceObject): Boolean = trans.removes.contains(o)
    inline fun <reified T> instances(): List<T> {
        return trans.adds.filterIsInstance<T>()
    }
}