package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class MissileTest {
    @Test
    fun `missile and splat death`() {
        val mix = SpaceObjectCollection()
        val ship = Ship(
            position = U.randomPoint()
        )
        val missile = Missile(ship)
        mix.add(missile)
        val game = Game(mix)
        assertThat(mix.contains(missile)).isEqualTo(true)
        assertThat(mix.missiles()).contains(missile)
        game.cycle(0.0)
        assertThat(mix.deferredActions().any { it is DeferredAction }).describedAs("deferred action should be present").isEqualTo(true)
        game.cycle(U.MISSILE_LIFETIME - 0.1)
        game.cycle(U.MISSILE_LIFETIME + 0.1)
        assertThat(mix.contains(missile)).describedAs("missile should be dead").isEqualTo(false)
        assertThat(mix.missiles()).doesNotContain(missile)
        assertThat(mix.splats()).describedAs("splat should be present").isNotEmpty()
        assertThat(mix.any { it is Splat }).describedAs("splat should be present").isEqualTo(true)
        game.cycle(U.MISSILE_LIFETIME + 0.2) // needs a tick to init
        game.cycle(U.MISSILE_LIFETIME + 2.3) // Splat lifetime is 2.0
        assertThat(mix.splats()).describedAs("splat should be gone").isEmpty()
    }
}