package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class FinalizerTest {
    @Test
    fun `asteroid finalizer`() {
        val asteroid = Asteroid(Point.ZERO)
        val trans = Transaction()
        asteroid.subscriptions.finalize(trans)
        val splits = trans.adds
        assertThat(splits.size).isEqualTo(2) // split guys and no score
    }
}