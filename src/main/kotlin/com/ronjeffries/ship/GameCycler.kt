package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class GameCycler(private val game: Game, private val knownObjects: SpaceObjectCollection, val initialNumberOfAsteroidsToCreate: Int, ship: Ship, saucer: Saucer) {

    var numberOfAsteroidsToCreate = initialNumberOfAsteroidsToCreate
    private val waveOneShot = OneShot(4.0) { makeWave(it) }

    fun cycle(deltaTime: Double, drawer: Drawer?) {
        tick(deltaTime)
        beforeInteractions()
        processInteractions()
        U.AsteroidTally = knownObjects.asteroidCount()
        createNewWaveIfNeeded()
        game.stranglerCycle(deltaTime, drawer)
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
