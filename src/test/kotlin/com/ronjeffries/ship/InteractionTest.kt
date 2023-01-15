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

    @Test
    fun `missile and ship`() {
        val missile = Missile(Point(100.0, 100.0))
        missile.position = Point(100.0, 100.0)
        val ship = Ship(Point(100.0, 100.0))
        val trans = Transaction()
        Interaction(listOf(missile), listOf(ship), emptyList(), emptyList(), trans)
        assertThat(trans.removes).contains(ship)
        assertThat(trans.removes).contains(missile)
    }

    @Test
    fun `missile and saucer`() {
        val missile = Missile(Point(100.0, 100.0)).also { it.position = Point(100.0, 100.0)}
        val saucer = Saucer().also { it. position = Point(100.0, 100.0) }
        val trans = Transaction()
        Interaction(listOf(missile), emptyList(), listOf(saucer), emptyList(), trans)
        assertThat(trans.removes).contains(saucer)
        assertThat(trans.removes).contains(missile)
    }
}