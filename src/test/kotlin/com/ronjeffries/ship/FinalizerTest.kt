package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class FinalizerTest {
    @Test
    fun `asteroid dieOnCollision`() {
        val asteroid = Asteroid(Point.ZERO)
        val trans = Transaction()
        asteroid.dieDueToCollision(trans)
        val splits = trans.asteroids
        assertThat(splits.size).isEqualTo(2)
    }
}