package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SaucerMakingTest {
    @Test
    fun `game-centric NO MAKER saucer appears after seven seconds`() {
        // cycle receives ELAPSED TIME!
        val mix = SpaceObjectCollection()
        mix.add(Ship(U.CENTER_OF_UNIVERSE))
        mix.add(Asteroid(Point(100.0,100.0)))
        val game = Game(mix) // makes game without the standard init
        game.cycle(0.1) // ELAPSED seconds
        assertThat(mix.size).describedAs("mix size").isEqualTo(2)
        assertThat(mix.deferredActions.size).describedAs("deferred size").isEqualTo(1)
        val deferred = mix.deferredActions.find { it.delay == 7.0 }
        assertThat(deferred).isNotNull
        game.cycle(7.2) //ELAPSED seconds
        val saucer = mix.saucers.first()
        assertThat(saucer).isNotNull
    }
}