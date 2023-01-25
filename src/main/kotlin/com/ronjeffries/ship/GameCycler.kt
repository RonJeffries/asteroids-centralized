package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class GameCycler(
    private val knownObjects: SpaceObjectCollection,
    initialNumberOfAsteroidsToCreate: Int = -1,
    private val controls: Controls = Controls(),
) {
    val saucer = Saucer()
    private var numberOfAsteroidsToCreate = initialNumberOfAsteroidsToCreate

    private val waveOneShot = OneShot(4.0) { makeWave(it) }
    private val saucerOneShot = OneShot( 7.0) { startSaucer(it) }
    private val shipOneShot = OneShot(U.SHIP_MAKER_DELAY, { canShipEmerge() }) {
        if ( knownObjects.scoreKeeper.takeShip() ) {
            startShipAtHome(it)
        }
    }

    fun canShipEmerge(): Boolean {
        return knownObjects.canShipEmerge()
    }

    private fun startShipAtHome(trans: Transaction) {
        val ship = Ship(U.CENTER_OF_UNIVERSE,controls)
        trans.add(ship)
    }

    private fun startSaucer(trans: Transaction) {
        saucer.start(trans)
    }

    fun cycle(deltaTime: Double, drawer: Drawer?= null) {
        tick(deltaTime)
        beforeInteractions()
        processInteractions()
        U.AsteroidTally = knownObjects.asteroidCount()
        createNewWaveIfNeeded()
        createSaucerIfNeeded()
        createShipIfNeeded()
        drawer?.let { draw(drawer) }
    }

    private fun draw(drawer: Drawer) {
        knownObjects.forEachInteracting { drawer.isolated { it.draw(drawer) } }
        knownObjects.scoreKeeper.draw(drawer)
    }

    private fun createShipIfNeeded() {
        if ( knownObjects.shipIsMissing() ) {
            knownObjects.performWithTransaction { shipOneShot.execute(it) }
        }
    }

    private fun createSaucerIfNeeded() {
        if ( knownObjects.saucerIsMissing() ) {
            knownObjects.performWithTransaction { saucerOneShot.execute(it) }
        }
    }

    private fun createNewWaveIfNeeded() {
        if ( U.AsteroidTally == 0 ) {
            knownObjects.performWithTransaction { waveOneShot.execute(it) }
        }
    }

    fun makeWave(it: Transaction) {
        for (i in 1..howMany()) {
            it.add(Asteroid(U.randomEdgePoint()))
        }
    }

    fun howMany(): Int {
        return numberOfAsteroidsToCreate.also {
            numberOfAsteroidsToCreate += 2
            if (numberOfAsteroidsToCreate > 11) numberOfAsteroidsToCreate = 11
        }
    }

    private fun beforeInteractions() = knownObjects.saucers().forEach { it.beforeInteractions() }

    fun processInteractions() = knownObjects.applyChanges(changesDueToInteractions())

    fun changesDueToInteractions(): Transaction {
        val trans = Transaction()
        knownObjects.pairsToCheck().forEach {
            it.first.interactWith(it.second, trans)
            it.second.interactWith(it.first, trans)
        }
        return trans
    }

    private fun tick(deltaTime: Double) {
        updateTimersFirst(deltaTime)
        thenUpdateSpaceObjects(deltaTime)
    }

    private fun updateTimersFirst(deltaTime: Double) {
        with (knownObjects) {
            performWithTransaction { trans ->
                deferredActions().forEach { it.update(deltaTime, trans) }
            }
        }
    }

    private fun thenUpdateSpaceObjects(deltaTime: Double) {
        with (knownObjects) {
            performWithTransaction { trans ->
                spaceObjects().forEach { it.update(deltaTime, trans) }
            }
        }
    }
}
