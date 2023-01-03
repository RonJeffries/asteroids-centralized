package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game(val knownObjects:SpaceObjectCollection = SpaceObjectCollection()) {
    private var lastTime = 0.0
    private var numberOfAsteroidsToCreate = 0
    private val oneShot = OneShot(4.0) { makeWave(it) }

    fun add(newObject: ISpaceObject) = knownObjects.add(newObject)

    fun changesDueToInteractions(): Transaction {
        val trans = Transaction()
        knownObjects.pairsToCheck().forEach { p ->
            p.first.callOther(p.second, trans)
            p.second.callOther(p.first, trans)
        }
        return trans
    }

    fun createInitialContents(controls: Controls) {
        initializeGame(controls, 0)
    }

    fun insertQuarter(controls: Controls) {
        initializeGame(controls, U.SHIPS_PER_QUARTER)
    }

    private fun initializeGame(controls: Controls, shipCount: Int) {
        numberOfAsteroidsToCreate = U.SHIPS_PER_QUARTER
        knownObjects.performWithTransaction { trans ->
            createInitialObjects(trans,shipCount, controls)
        }
    }

    private fun createInitialObjects(
        trans: Transaction,
        shipCount: Int,
        controls: Controls
    ) {
        oneShot.cancel(Transaction())
        trans.clear()
        val scoreKeeper = ScoreKeeper(shipCount)
        knownObjects.scoreKeeper = scoreKeeper
        trans.add(SaucerMaker())
        val shipPosition = U.CENTER_OF_UNIVERSE
        val ship = Ship(shipPosition, controls)
        val shipChecker = ShipChecker(ship, scoreKeeper)
        trans.add(shipChecker)
    }

    fun cycle(elapsedSeconds: Double, drawer: Drawer? = null) {
        val deltaTime = elapsedSeconds - lastTime
        lastTime = elapsedSeconds
        tick(deltaTime)
        beginInteractions()
        processInteractions()
        finishInteractions()
        createNewWaveIfNeeded()
        drawer?.let { draw(drawer) }
    }

    private fun createNewWaveIfNeeded() {
        if ( knownObjects.asteroidCount() == 0 ) {
            val trans = Transaction()
            oneShot.execute(trans)
            knownObjects.applyChanges(trans)
        }
    }

    // spike sketch of centralized interaction
    fun interact(attacker: ISpaceObject, target: ISpaceObject, trans: Transaction) {
        if (attacker == target) return
        if (outOfRange(attacker, target)) return
        if (attacker is Missile) {
            if (target is Ship) {
                remove(attacker)
                explode(target)
            } else if (target is Saucer) {
                remove(attacker)
                explode(target)
            } else if (target is Asteroid) {
                remove(attacker)
                if (attacker.missileIsFromShip) addScore(target)
                split(target)
            }
        } else if (attacker is Ship) {
        } else { // attacker is Saucer
        }
    }

    private fun outOfRange(a: ISpaceObject, b: ISpaceObject) = false
    private fun remove(o: ISpaceObject) {}
    private fun explode(o: ISpaceObject) {}
    private fun split(o: ISpaceObject) {}
    private fun addScore(o: ISpaceObject) {}

    private fun beginInteractions() = knownObjects.forEach { it.subscriptions.beforeInteractions() }

    private fun finishInteractions() {
        val buffer = Transaction()
        knownObjects.forEach { it.subscriptions.afterInteractions(buffer) }
        knownObjects.applyChanges(buffer)
    }

    private fun draw(drawer: Drawer) {
        knownObjects.forEach { drawer.isolated { it.subscriptions.draw(drawer) } }
        knownObjects.scoreKeeper.draw(drawer)
    }

    private fun howMany(): Int {
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

    fun processInteractions() = knownObjects.applyChanges(changesDueToInteractions())

    fun tick(deltaTime: Double) {
        val trans = Transaction()
        knownObjects.deferredActions.forEach { it.update(deltaTime, trans)}
        knownObjects.forEach { it.update(deltaTime, trans) }
        knownObjects.applyChanges(trans)
    }
}
