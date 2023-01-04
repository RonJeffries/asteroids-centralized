package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class SaucerMakingTest {
    @Test
    fun `game-centric NO MAKER saucer appears after seven seconds`() {
        // cycle receives ELAPSED TIME!
        val mix = SpaceObjectCollection()
        val game = Game(mix) // makes game without the standard init
        game.cycle(0.1) // ELAPSED seconds
        assertThat(mix.size).describedAs("mix size").isEqualTo(0)
        assertThat(mix.deferredActions.size).describedAs("deferred size").isEqualTo(2)
        val deferred = mix.deferredActions.find { (it as DeferredAction).delay == 7.0 }
        assertThat(deferred).isNotNull
        game.cycle(7.2) //ELAPSED seconds
        val saucer = mix.targets.find { it is Saucer }
        assertThat(saucer).isNotNull
    }

    @Test
    fun `saucer changes direction`() {
        // test ensures that we cache the saucer rather than create new ones
        // cycle receives ELAPSED TIME!
        val mix = SpaceObjectCollection()
        val game = Game(mix) // makes game without the standard init
        game.cycle(0.1) // ELAPSED seconds
        game.cycle(7.2) //ELAPSED seconds
        val saucer = mix.targets.first() as Saucer
        assertThat(saucer.velocity.x > 0.0)
        mix.performWithTransaction { it.remove(saucer) }
        game.cycle(7.3)
        game.cycle(14.4)
        game.cycle(14.5)
        val nextSaucer = mix.targets.find { it is Saucer}
        val actual = nextSaucer as Saucer
        assertThat(actual.velocity.x < 0.0)
    }
}