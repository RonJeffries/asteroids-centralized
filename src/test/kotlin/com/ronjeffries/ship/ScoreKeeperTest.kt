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
}