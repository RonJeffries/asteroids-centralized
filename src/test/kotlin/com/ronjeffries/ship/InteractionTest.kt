package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class InteractionTest {
    val target = Point(100.0, 100.0)

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
        val missile = Missile(target)
        missile.position = target
        val ship = Ship(target)
        val trans = Transaction()
        Interaction(listOf(missile), listOf(ship), emptyList(), emptyList(), trans)
        assertThat(trans.removes).contains(ship)
        assertThat(trans.removes).contains(missile)
    }

    @Test
    fun `missile and saucer`() {
        val missile = Missile(target).also { it.position = target}
        val saucer = Saucer().also { it. position = target }
        val trans = Transaction()
        Interaction(listOf(missile), emptyList(), listOf(saucer), emptyList(), trans)
        assertThat(trans.removes).contains(saucer)
        assertThat(trans.removes).contains(missile)
    }

    @Test
    fun `missile and asteroid`() {
        val missile = Missile(target).also { it.position = target}
        val asteroid = Asteroid(target)
        val trans = Transaction()
        Interaction(listOf(missile), emptyList(), emptyList(), listOf(asteroid), trans)
        assertThat(trans.removes).contains(asteroid)
        assertThat(trans.removes).contains(missile)
    }

    @Test
    fun `ship and saucer`() {
        val ship = Ship(target)
        val saucer = Saucer().also { it.position = target }
        val trans = Transaction()
        Interaction(emptyList(), listOf(ship), listOf(saucer), emptyList(), trans)
        assertThat(trans.removes).contains(saucer)
        assertThat(trans.removes).contains(ship)
    }
}