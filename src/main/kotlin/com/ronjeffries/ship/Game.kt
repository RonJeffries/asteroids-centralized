package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated

class Game(val knownObjects:SpaceObjectCollection = SpaceObjectCollection()) {
    private var lastTime = 0.0
    private var numberOfAsteroidsToCreate = 0
    private val saucer = Saucer()
    private lateinit var ship: Ship
    private var scoreKeeper: ScoreKeeper = ScoreKeeper(-1)

    private val waveOneShot = OneShot(4.0) { makeWave(it) }
    private val saucerOneShot = OneShot( 7.0) {it.add(saucer)}
    private val shipOneShot = OneShot(U.SHIP_MAKER_DELAY, { canShipEmerge() }) {
       if ( scoreKeeper.takeShip() ) it.add(ship)
    }
    // all OneShot instances go here:
    private val allOneShots = listOf(waveOneShot, saucerOneShot, shipOneShot)

    fun add(newObject: InteractingSpaceObject) = knownObjects.add(newObject)

    fun changesDueToInteractions(): Transaction {
        val trans = Transaction()
        knownObjects.pairsToCheck().forEach { p ->
            p.first.callOther(p.second, trans)
            p.second.callOther(p.first, trans)
        }
        return trans
    }

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
        saucer.initialize()
        scoreKeeper = ScoreKeeper(shipCount)
        knownObjects.scoreKeeper = scoreKeeper
        val shipPosition = U.CENTER_OF_UNIVERSE
        ship = Ship(shipPosition, controls)
    }

    private fun cancelAllOneShots() {
        val ignored = Transaction()
        for (oneShot in allOneShots) {
            oneShot.cancel(ignored)
        }
    }

    fun cycle(elapsedSeconds: Double, drawer: Drawer? = null) {
        val deltaTime = elapsedSeconds - lastTime
        lastTime = elapsedSeconds
        tick(deltaTime)
        beginInteractions()
        processInteractions()
        finishInteractions()
        U.AsteroidTally = knownObjects.asteroidCount()
        createNewWaveIfNeeded()
        createSaucerIfNeeded()
        createShipIfNeeded()
        drawer?.let { draw(drawer) }
    }

    private fun createShipIfNeeded() {
        if ( knownObjects.shipIsPresent() ) return
        val trans = Transaction()
        shipOneShot.execute(trans)
        knownObjects.applyChanges(trans)
    }

    private fun createNewWaveIfNeeded() {
        if ( U.AsteroidTally == 0 ) {
            val trans = Transaction()
            waveOneShot.execute(trans)
            knownObjects.applyChanges(trans)
        }
    }

    private fun createSaucerIfNeeded() {
        if ( knownObjects.saucerMissing() ) {
            val trans = Transaction()
            saucerOneShot.execute(trans)
            knownObjects.applyChanges(trans)
        }
    }

    // spike sketch of centralized interaction
    fun interact(attacker: InteractingSpaceObject, target: InteractingSpaceObject, trans: Transaction) {
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

    private fun outOfRange(a: InteractingSpaceObject, b: InteractingSpaceObject) = false
    private fun remove(o: InteractingSpaceObject) {}
    private fun explode(o: InteractingSpaceObject) {}
    private fun split(o: InteractingSpaceObject) {}
    private fun addScore(o: InteractingSpaceObject) {}

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

    fun canShipEmerge(): Boolean {
        for ( target in knownObjects.targets ) {
            if ( target is Saucer ) return false
            if ( target is Asteroid ) {
                val distance = target.position.distanceTo(U.CENTER_OF_UNIVERSE)
                if ( distance < U.SAFE_SHIP_DISTANCE ) return false
            }
        }
        for ( attacker in knownObjects.attackers ) {
            if ( attacker is Missile ) return false
            if ( attacker is Saucer ) return false // already checked but hey
        }
        return true
    }
}
