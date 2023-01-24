package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game(val knownObjects:SpaceObjectCollection = SpaceObjectCollection()) {
    private var lastTime = 0.0
    private var numberOfAsteroidsToCreate = 0
    private var saucer = Saucer()
    lateinit var ship: Ship
    private var cycler: GameCycler = GameCycler(this, knownObjects, 0, Ship(U.CENTER_OF_UNIVERSE), saucer)
    private var scoreKeeper: ScoreKeeper = ScoreKeeper(-1)

    // all OneShot instances go here:
    private val allOneShots = emptyList<OneShot>()

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
        scoreKeeper = ScoreKeeper(shipCount)
        knownObjects.scoreKeeper = scoreKeeper
        val shipPosition = U.CENTER_OF_UNIVERSE
        ship = Ship(shipPosition, controls)
        saucer = Saucer()
        cycler = makeCycler()
        cycler.cancelAllOneShots()
        cancelAllOneShots()
        trans.clear()
    }

    fun makeCycler() = GameCycler(this, knownObjects, numberOfAsteroidsToCreate, ship, saucer)

    private fun cancelAllOneShots() {
        val ignored = Transaction()
        for (oneShot in allOneShots) {
            oneShot.cancel(ignored)
        }
    }

    fun cycle(elapsedSeconds: Double, drawer: Drawer? = null) {
        val deltaTime = elapsedSeconds - lastTime
        lastTime = elapsedSeconds
        cycler.cycle(deltaTime, drawer)
    }

    fun stranglerCycle(deltaTime: Double, drawer: Drawer?) {
    }

}
