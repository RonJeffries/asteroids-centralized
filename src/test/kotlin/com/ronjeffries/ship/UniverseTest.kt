package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class UniverseTest {

    @Test
    fun `collision calculation`() {
        val ship = Ship(
            pos=Vector2.ZERO
        )
        val asteroid = Asteroid(
            pos = Vector2.ZERO,
            velocity = Vector2.ZERO
        )
        val trans = Transaction()
        ship.interact(asteroid, trans)
        assertThat(trans.removes.size).describedAs("on top").isEqualTo(1)
        val tooFar = Vector2(ship.killRadius + asteroid.killRadius + 1, 0.0)
        var rotated = tooFar.rotate(37.0)
        ship.position = rotated
        val trans2 = Transaction()
        ship.interact(asteroid, trans2)
        assertThat(trans2.removes.size).describedAs("too far").isEqualTo(0)
        val closeEnough = Vector2(ship.killRadius + asteroid.killRadius - 1, 0.0)
        rotated = closeEnough.rotate(37.0)
        ship.position = rotated
        ship.interact(asteroid, trans2)
        assertThat(trans2.removes.size).describedAs("too close").isEqualTo(1)
    }
//
//    @Test
//    fun `randomInsideDouble between 1000 and 9000`() {
//        for (i in 1..100000) {
//            var rd = U.randomInsideDouble()
//            assertThat(rd).isBetween(1000.0, 9000.0)
//        }
//    }
}