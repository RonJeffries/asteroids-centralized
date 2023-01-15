package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class InteractionTest {
    @Test
    fun `empty returns empty`() {
        val missiles = mutableListOf<Missile>()
        val ships = mutableListOf<Ship>()
        val saucers = mutableListOf<Saucer>()
        val asteroids = mutableListOf<Asteroid>()
        val trans = Transaction()
        Interaction(missiles, ships, saucers, asteroids, trans)
        assertThat(trans.adds).isEmpty()
        assertThat(trans.removes).isEmpty()
    }
}