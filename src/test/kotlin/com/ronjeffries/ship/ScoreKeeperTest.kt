package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class ScoreKeeperTest {
    @Test
    fun `scorekeeper starts low`() {
        val keeper = ScoreKeeper()
        assertThat(keeper.totalScore).isEqualTo(0)
    }

    @Test
    fun `scorekeeper formats interestingly`() {
        val keeper = ScoreKeeper()
        keeper.totalScore = 123
        assertThat(keeper.formatted()).isEqualTo("00123")
    }

    @Test
    fun `ScoreKeeper provides ships to be made`() {
        val keeper = ScoreKeeper(2)
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        val checker = ShipChecker(ship, keeper)
        val t1 = Transaction()
        checker.subscriptions.beforeInteractions()
        checker.subscriptions.afterInteractions(t1)
        assertThat(t1.adds.size).isEqualTo(1)
        val t2 = Transaction()
        checker.subscriptions.beforeInteractions()
        checker.subscriptions.afterInteractions(t2)
        assertThat(t2.adds.size).isEqualTo(1)
        val t3 = Transaction()
        checker.subscriptions.beforeInteractions()
        checker.subscriptions.afterInteractions(t3)
        assertThat(t3.adds.size).isEqualTo(0)
    }
}