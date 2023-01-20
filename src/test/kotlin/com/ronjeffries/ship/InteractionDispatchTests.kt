package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test


class InteractionDispatchTests {
    @Test
    fun `asteroid dispatch`() {
        val asteroid = Asteroid(U.CENTER_OF_UNIVERSE)
        val missile = Missile(Point(0.0, 0.0))
        missile.position = U.CENTER_OF_UNIVERSE
        val removeMissile = Transaction()
        asteroid.interactWith(missile, removeMissile)
        assertThat(removeMissile.removes).contains(missile)
        val addAsteroids = Transaction()
        missile.interactWith(asteroid, addAsteroids)
        assertThat(addAsteroids.asteroids.size).isEqualTo(2)
    }
}