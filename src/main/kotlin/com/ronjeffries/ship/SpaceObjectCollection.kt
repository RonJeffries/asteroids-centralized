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

    fun add(asteroid: Asteroid) {
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

    private fun colliders(): List<Collider> {
        val candidates = spaceObjects().filter { it is Collider }
        return candidates.map { it as Collider}
    }

    fun pairsToCheck(): List<Pair<Collider, Collider>> {
        val colliders = colliders()
        val pairs = mutableListOf<Pair<Collider, Collider>>()
        colliders.indices.forEach { i ->
            colliders.indices.minus(0..i).forEach { j ->
                pairs.add(colliders[i] to colliders[j])
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
