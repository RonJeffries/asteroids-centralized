package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Game() {
    private var lastTime = 0.0
    private var cycler: GameCycler = GameCycler(SpaceObjectCollection(), 0, Controls())

    fun currentMix(): SpaceObjectCollection = cycler.currentMix()

    fun createInitialContents(controls: Controls) {
        initializeGame(controls, -1)
    }

    fun insertQuarter(controls: Controls) {
        initializeGame(controls, U.SHIPS_PER_QUARTER)
    }

    private fun initializeGame(controls: Controls, shipCount: Int) {
        val knownObjects = SpaceObjectCollection()
        knownObjects.performWithTransaction { trans ->
            createInitialObjects(trans,shipCount, controls, knownObjects)
        }
    }

    private fun createInitialObjects(
        trans: Transaction,
        shipCount: Int,
        controls: Controls,
        knownObjects: SpaceObjectCollection
    ) {
        knownObjects.scoreKeeper = ScoreKeeper(shipCount)
        cycler = GameCycler(knownObjects, U.ASTEROID_STARTING_COUNT, controls)
        trans.clear()
    }

    fun cycle(elapsedSeconds: Double, drawer: Drawer? = null) {
        val deltaTime = elapsedSeconds - lastTime
        lastTime = elapsedSeconds
        cycler.cycle(deltaTime, drawer)
    }
}
