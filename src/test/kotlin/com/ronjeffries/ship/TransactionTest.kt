package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class TransactionTest {
    private fun newShip(): Ship {
        return Ship(
            pos = U.randomPoint()
        )
    }

    @Test
    fun `transaction can add and remove`() {
        val coll = SpaceObjectCollection()
        val shipOne = newShip()
        coll.add(shipOne)
        val t = Transaction()
        val shipTwo = newShip()
        t.add(shipTwo)
        t.remove(shipOne)
        coll.applyChanges(t)
        assertThat(coll.spaceObjects()).contains(shipTwo)
        assertThat(coll.spaceObjects()).doesNotContain(shipOne)
        assertThat(coll.size).isEqualTo(1)
    }

    @Test
    fun `can clear collection`() {
        val coll = SpaceObjectCollection()
        val obj = Asteroid(U.CENTER_OF_UNIVERSE)
        val trans = Transaction()
        trans.add(obj)
        trans.applyChanges(coll)
        assertThat(coll.size).isEqualTo(1)
        val clearTrans = Transaction()
        clearTrans.clear()
        clearTrans.applyChanges(coll)
        assertThat(coll.size).isEqualTo(0)
    }

    @Test
    fun `accumulates score via member`() {
        val coll = SpaceObjectCollection()
        val scoreKeeper = ScoreKeeper(0)
        coll.scoreKeeper = scoreKeeper
        val trans = Transaction()
        trans.addScore(15)
        trans.addScore(27)
        coll.applyChanges(trans)
        assertThat(scoreKeeper.totalScore).isEqualTo(42)
    }

    @Test
    fun `clears score on clear`() {
        val coll = SpaceObjectCollection()
        val scoreKeeper = ScoreKeeper(0)
        coll.scoreKeeper = scoreKeeper
        val trans = Transaction()
        trans.addScore(15)
        trans.addScore(27)
        coll.applyChanges(trans)
        assertThat(scoreKeeper.totalScore).isEqualTo(42)
        val newTrans = Transaction()
        newTrans.clear()
        coll.applyChanges(newTrans)
        assertThat(scoreKeeper.totalScore).isEqualTo(0)
    }
}