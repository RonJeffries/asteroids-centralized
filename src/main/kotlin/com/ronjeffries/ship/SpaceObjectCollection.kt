package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()

    private val deferredActions = mutableListOf<DeferredAction>()
    private val spaceObjects = mutableListOf<SpaceObject>()


    fun add(deferredAction: DeferredAction) {
        deferredActions.add(deferredAction)
    }

    fun add(spaceObject: SpaceObject) {
        spaceObjects.add(spaceObject)
    }

    fun addScore(score: Int) {
        scoreKeeper.addScore(score)
    }

    fun applyChanges(transaction: Transaction) = transaction.applyChanges(this)

    val asteroids get() = spaceObjects.filterIsInstance<Asteroid>()
    fun deferredActions() = deferredActions
    val missiles get() = spaceObjects.filterIsInstance<Missile>()
    val saucers get() = spaceObjects.filterIsInstance<Saucer>()
    val ships get() = spaceObjects.filterIsInstance<Ship>()
    fun spaceObjects(): List<SpaceObject> = spaceObjects
    val splats get() = spaceObjects.filterIsInstance<Splat>()

    fun asteroidCount(): Int = asteroids.size

    fun canShipEmerge(): Boolean {
        if (saucerIsPresent()) return false
        if (missiles.isNotEmpty()) return false
        if (asteroids.any { it.distanceToCenter() < U.SAFE_SHIP_DISTANCE}) return false
        return true
    }

    fun clear() {
        scoreKeeper.clear()
        deferredActions.clear()
        spaceObjects.clear()
    }

    fun forEachInteracting(action: (SpaceObject) -> Unit) =
        spaceObjects().forEach(action)

    fun contains(obj: SpaceObject): Boolean {
        return spaceObjects().contains(obj)
    }

    fun pairsToCheck(): List<Pair<SpaceObject, SpaceObject>> {
        val pairs = mutableListOf<Pair<SpaceObject, SpaceObject>>()
        spaceObjects.indices.forEach { i ->
            spaceObjects.indices.minus(0..i).forEach { j ->
                pairs.add(spaceObjects[i] to spaceObjects[j])
            }
        }
        return pairs
    }

    fun performWithTransaction(action: (Transaction) -> Unit) {
        val trans = Transaction()
        action(trans)
        applyChanges(trans)
    }

    fun remove(deferredAction: DeferredAction) {
        deferredActions.remove(deferredAction)
    }

    fun remove(spaceObject: SpaceObject) {
        spaceObjects.remove(spaceObject)
    }

    private fun saucerIsPresent(): Boolean {
        return saucers.isNotEmpty()
    }

    fun saucerIsMissing(): Boolean {
        return saucers.isEmpty()
    }

    fun shipIsMissing(): Boolean {
        return ships.isEmpty()
    }

    val size get() = spaceObjects().size
}
