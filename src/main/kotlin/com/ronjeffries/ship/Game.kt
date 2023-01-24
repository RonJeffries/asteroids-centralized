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

    private val waveOneShot = OneShot(4.0) { makeWave(it) }
    private val saucerOneShot = OneShot( 7.0) { startSaucer(it) }

    private fun startSaucer(trans: Transaction) {
        saucer.start(trans)
    }

    private val shipOneShot = OneShot(U.SHIP_MAKER_DELAY, { canShipEmerge() }) {
       if ( scoreKeeper.takeShip() ) {
           startShipAtHome(it)
       }
    }

    private fun startShipAtHome(trans: Transaction) {
        ship.setToHome()
        trans.add(ship)
    }

    // all OneShot instances go here:
    private val allOneShots = listOf(waveOneShot, saucerOneShot, shipOneShot)

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
        cancelAllOneShots()
        trans.clear()
        scoreKeeper = ScoreKeeper(shipCount)
        knownObjects.scoreKeeper = scoreKeeper
        val shipPosition = U.CENTER_OF_UNIVERSE
        ship = Ship(shipPosition, controls)
        saucer = Saucer()
        cycler = makeCycler()
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
        createNewWaveIfNeeded()
        createSaucerIfNeeded()
        createShipIfNeeded()
        drawer?.let { draw(drawer) }
    }

    private fun createShipIfNeeded() {
        if ( knownObjects.shipIsMissing() ) {
            knownObjects.performWithTransaction { shipOneShot.execute(it) }
        }
    }

    private fun createNewWaveIfNeeded() {
        if ( U.AsteroidTally == 0 ) {
            knownObjects.performWithTransaction { waveOneShot.execute(it) }
        }
    }

    private fun createSaucerIfNeeded() {
        if ( knownObjects.saucerIsMissing() ) {
            knownObjects.performWithTransaction { saucerOneShot.execute(it) }
        }
    }

    private fun draw(drawer: Drawer) {
        knownObjects.forEachInteracting { drawer.isolated { it.draw(drawer) } }
        knownObjects.scoreKeeper.draw(drawer)
    }

    fun howMany(): Int {
        return numberOfAsteroidsToCreate.also {
            numberOfAsteroidsToCreate += 2
            if (numberOfAsteroidsToCreate > 11) numberOfAsteroidsToCreate = 11
        }
    }

    fun makeWave(it: Transaction) {
        for (i in 1..howMany()) {
            it.add(Asteroid(U.randomEdgePoint()))
        }
    }

    fun canShipEmerge(): Boolean {
        if (knownObjects.saucerIsPresent()) return false
        if (knownObjects.missiles().isNotEmpty()) return false
        for ( asteroid in knownObjects.asteroids() ) {
            val distance = asteroid.position.distanceTo(U.CENTER_OF_UNIVERSE)
            if ( distance < U.SAFE_SHIP_DISTANCE ) return false
        }
        return true
    }
}
