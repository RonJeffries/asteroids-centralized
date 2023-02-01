package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SaucerMakingTest {
    @Test
    fun `game-centric NO MAKER saucer appears after seven seconds`() {
        val mix = SpaceObjectCollection()
        mix.add(Ship(U.CENTER_OF_UNIVERSE))
        mix.add(Asteroid(Point(100.0,100.0)))
        val cycler = GameCycler(mix)
        cycler.cycle(0.1) // ELAPSED seconds
        assertThat(mix.spaceObjects().size).describedAs("mix size").isEqualTo(2)
        assertThat(mix.deferredActions().size).describedAs("deferred size").isEqualTo(1)
        val deferred = mix.deferredActions().find { it.delay == 7.0 }
        assertThat(deferred).isNotNull
        cycler.cycle(7.2)
        val saucer = mix.saucers.first()
        assertThat(saucer).isNotNull
    }
}