package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class FinalizerTest {
    @Test
    fun `asteroid dieOnCollision`() {
        val asteroid = Asteroid(Point.ZERO)
        val trans = Transaction()
        asteroid.dieDuetoCollision(trans)
        val splits = trans.adds
        assertThat(splits.size).isEqualTo(3) // split guys and splat
    }
}