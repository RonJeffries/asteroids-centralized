package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class ShipCheckerAndMakerTest {
    @Test
    fun `ship randomizes position on hyperspace entry`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val trans = Transaction()
        U.AsteroidTally  = 100 // hyperspace never fails
        ship.enterHyperspace(trans)
        assertThat(ship.position).isNotEqualTo(U.CENTER_OF_UNIVERSE)
    }

    @Test
    fun `ship goes to center on collision death`() {
        val ship = Ship(U.randomPoint())
        val trans = Transaction()
        ship.collision(trans)
        assertThat(trans.firstRemove()).isEqualTo(ship)
        ship.finalize(Transaction())
        assertThat(ship.position).isEqualTo(U.CENTER_OF_UNIVERSE)
    }

    @Test
    fun `hyperspace failure checks`() {
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        assertThat(ship.hyperspaceFailure(62, 19)).describedAs("roll 62 19 asteroids").isEqualTo(false)
        assertThat(ship.hyperspaceFailure(62, 18)).describedAs("roll 62 18 asteroids").isEqualTo(true)
        assertThat(ship.hyperspaceFailure(45, 0)).describedAs("roll 45 0 asteroids").isEqualTo(true)
        assertThat(ship.hyperspaceFailure(44, 0)).describedAs("roll 44 0 asteroids").isEqualTo(true)
        assertThat(ship.hyperspaceFailure(43, 0)).describedAs("roll 43 0 asteroids").isEqualTo(false)
    }
}