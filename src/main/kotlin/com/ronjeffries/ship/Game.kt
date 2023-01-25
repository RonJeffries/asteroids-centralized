package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class Game(val knownObjects:SpaceObjectCollection = SpaceObjectCollection()) {
    private var lastTime = 0.0
    private var numberOfAsteroidsToCreate = 0
    private var saucer = Saucer()
    lateinit var ship: Ship
    private var cycler: GameCycler = GameCycler(knownObjects, 0, Controls())

    fun createInitialContents(controls: Controls) {
        initializeGame(controls, -1)
    }

    fun insertQuarter(controls: Controls) {
        initializeGame(controls, U.SHIPS_PER_QUARTER)
    }

    private fun initializeGame(controls: Controls, shipCount: Int) {
        numberOfAsteroidsToCreate = U.ASTEROID_STARTING_COUNT
        knownObjects.performWithTransaction { trans ->
            createInitialObjects(trans,shipCount, controls)
        }
    }

    private fun createInitialObjects(
        trans: Transaction,
        shipCount: Int,
        controls: Controls
    ) {
        knownObjects.scoreKeeper = ScoreKeeper(shipCount)
        val shipPosition = U.CENTER_OF_UNIVERSE
        ship = Ship(shipPosition, controls)
        saucer = Saucer()
        cycler = makeCycler(controls)
        cycler.cancelAllOneShots()
        trans.clear()
    }

    fun makeCycler(controls: Controls = Controls()) = GameCycler(knownObjects, numberOfAsteroidsToCreate, controls)

    fun cycle(elapsedSeconds: Double, drawer: Drawer? = null) {
        val deltaTime = elapsedSeconds - lastTime
        lastTime = elapsedSeconds
        cycler.cycle(deltaTime, drawer)
    }
}
