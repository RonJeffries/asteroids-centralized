package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class AsteroidTest {
    private val tick = 1.0/60.0

    @Test
    fun `Asteroids Exist and Move`() {
        val asteroid = Asteroid(
            position = Point.ZERO,
            velocity = Velocity(15.0,30.0)
        )
        asteroid.update(tick*60, Transaction())
        checkVector(asteroid.position, Point(15.0, 30.0),"asteroid position")
    }

    @Test
    fun `asteroid splits on collision death`() {
        val full = Asteroid(
            position = Point.ZERO,
            velocity = Velocity.ZERO
        )
        val radius = full.killRadius
        val trans = Transaction()
        full.dieDuetoCollision(trans)
        val halfAdds = trans.adds
        assertThat(halfAdds.size).isEqualTo(3) // two asteroids and a splat
        val half = halfAdds.last()
        assertThat((half as Asteroid).killRadius).describedAs("half").isEqualTo(radius/2.0)
        val trans2 = Transaction()
        half.dieDuetoCollision(trans2)
        val quarterAdds = trans2.adds
        assertThat(quarterAdds.size).isEqualTo(3)
        val quarter = quarterAdds.last()
        assertThat((quarter as Asteroid).killRadius).describedAs("quarter").isEqualTo(radius/4.0)
        val trans3 = Transaction()
        quarter.dieDuetoCollision(trans3)
        val eighthAdds = trans3.adds
        assertThat(eighthAdds.size).describedAs("should not split third time").isEqualTo(1) // just splat
    }

    @Test
    fun `new split asteroids get new directions`() {
        val startingV = Vector2(U.ASTEROID_SPEED,0.0)
        val full = Asteroid(
            position = Vector2.ZERO,
            velocity = startingV
        )
        val fullV = full.velocity
        assertThat(fullV.length).isEqualTo(U.ASTEROID_SPEED, within(1.0))
        assertThat(fullV).isEqualTo(startingV)
        val trans = Transaction()
        full.dieDuetoCollision(trans)
        val halfSize = trans.adds
        var countSplits = 0
        halfSize.forEach {
            if ( it is Asteroid) {
                countSplits += 1
                val halfV = it.velocity
                assertThat(halfV.length).isEqualTo(U.ASTEROID_SPEED, within(1.0))
                assertThat(halfV).isNotEqualTo(startingV)
            }
        }
        assertThat(countSplits).describedAs("always two there are").isEqualTo(2)
    }
}