package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()

    val asteroids = mutableListOf<Asteroid>()
    val deferredActions = mutableListOf<DeferredAction>()
    val missiles = mutableListOf<Missile>()
    val saucers = mutableListOf<Saucer>()
    val ships = mutableListOf<Ship>()
    val splats = mutableListOf<Splat>()

    fun spaceObjects():List<SpaceObject> = asteroids + missiles + saucers + ships + splats
    // update function below if you add to these
    fun allCollections(): List<MutableList<out SpaceObject>> {
        return listOf (asteroids, deferredActions, missiles, saucers, ships, splats)
    }

    fun add(deferredAction: DeferredAction) {
        deferredActions.add(deferredAction)
    }

    fun add(spaceObject: SpaceObject) {
        addActualSpaceObjects(spaceObject)
    }

    private fun add(asteroid: Asteroid) {
        asteroids.add(asteroid)
    }

    fun add(missile: Missile) {
        missiles.add(missile)
    }

    fun add(saucer: Saucer) {
        saucers.add(saucer)
    }

    fun add(ship: Ship) {
        ships.add(ship)
    }

    fun add(splat: Splat) {
        splats.add(splat)
    }

    private fun addActualSpaceObjects(spaceObject: SpaceObject) {
        when (spaceObject) {
            is Missile -> add(spaceObject)
            is Asteroid -> add(spaceObject)
            is Ship -> add(spaceObject)
            is Saucer -> add(spaceObject)
            is Splat -> add(spaceObject)
        }
    }

    fun addScore(score: Int) {
        scoreKeeper.addScore(score)
    }

    fun any(predicate: (SpaceObject)-> Boolean): Boolean {
        return spaceObjects().any(predicate)
    }

    fun applyChanges(transaction: Transaction) = transaction.applyChanges(this)

    fun asteroidCount(): Int = asteroids.size

    fun clear() {
        scoreKeeper.clear()
        for ( coll in allCollections()) {
            coll.clear()
        }
    }

    fun forEachInteracting(action: (SpaceObject)->Unit) =
        spaceObjects().forEach(action)

    fun contains(obj:SpaceObject): Boolean {
        return spaceObjects().contains(obj)
    }

    fun pairsToCheck(): List<Pair<SpaceObject, SpaceObject>> {
        val pairs = mutableListOf<Pair<SpaceObject, SpaceObject>>()
        spaceObjects().indices.forEach { i ->
            spaceObjects().indices.minus(0..i).forEach { j ->
                pairs.add(spaceObjects()[i] to spaceObjects()[j])
            }
        }
        return pairs
    }

    fun performWithTransaction(action: (Transaction) -> Unit ) {
        val trans = Transaction()
        action(trans)
        applyChanges(trans)
    }

    fun remove(deferredAction: DeferredAction) {
        deferredActions.remove(deferredAction)
    }

    fun remove(spaceObject: SpaceObject) {
        for ( coll in allCollections()) {
            coll.remove(spaceObject)
        }
    }

    fun saucerIsPresent(): Boolean {
        return saucers.isNotEmpty()
    }

    fun saucerIsMissing(): Boolean {
        return saucers.isEmpty()
    }

    fun shipIsPresent(): Boolean {
        return ships.isNotEmpty()
    }

    fun shipIsMissing(): Boolean {
        return ships.isEmpty()
    }

    val size get() = spaceObjects().size
}
