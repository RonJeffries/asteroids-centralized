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
        full.dieDueToCollision(trans)
        val halfAdds = trans.asteroids()
        assertThat(halfAdds.size).isEqualTo(2) // two asteroids and a splat
        assertThat(trans.splats().size).isEqualTo(1)
        val half = halfAdds.last()
        assertThat((half).killRadius).describedAs("half").isEqualTo(radius/2.0)
        val trans2 = Transaction()
        half.dieDueToCollision(trans2)
        val quarterAdds = trans2.asteroids()
        assertThat(quarterAdds.size).isEqualTo(2)
        val quarter = quarterAdds.last()
        assertThat((quarter).killRadius).describedAs("quarter").isEqualTo(radius/4.0)
        val trans3 = Transaction()
        quarter.dieDueToCollision(trans3)
        val eighthAdds = trans3.asteroids()
        assertThat(eighthAdds.size).describedAs("should not split third time").isEqualTo(0)
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
        full.dieDueToCollision(trans)
        val halfSize = trans.asteroids()
        var countSplits = 0
        halfSize.forEach {
            countSplits += 1
            val halfV = it.velocity
            assertThat(halfV.length).isEqualTo(U.ASTEROID_SPEED, within(1.0))
            assertThat(halfV).isNotEqualTo(startingV)
        }
        assertThat(countSplits).describedAs("always two there are").isEqualTo(2)
    }

    @Test
    fun `asteroid dieOnCollision`() {
        val asteroid = Asteroid(Point.ZERO)
        val trans = Transaction()
        asteroid.dieDueToCollision(trans)
        val splits = trans.asteroids()
        assertThat(splits.size).isEqualTo(2)
    }
}