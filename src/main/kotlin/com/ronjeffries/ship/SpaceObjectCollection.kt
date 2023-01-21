package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()

    private val colliders = mutableListOf<Collider>()
    val deferredActions = mutableListOf<DeferredAction>()
//    val missiles = mutableListOf<Missile>()
//    val saucers = mutableListOf<Saucer>()
    val ships = mutableListOf<Ship>()
    val splats = mutableListOf<Splat>()

    private val spaceObjects = mutableListOf<SpaceObject>()

    fun spaceObjects():List<SpaceObject> = spaceObjects
    // update function below if you add to these
    fun allCollections(): List<MutableList<out SpaceObject>> {
        return listOf (deferredActions, ships, splats)
    }

    fun add(deferredAction: DeferredAction) {
        deferredActions.add(deferredAction)
    }

    fun add(asteroid: Asteroid) {
        colliders.add(asteroid)
        spaceObjects.add(asteroid)
    }

    fun add(missile: Missile) {
        colliders.add(missile)
        spaceObjects.add(missile)
    }

    fun add(saucer: Saucer) {
        colliders.add(saucer)
        spaceObjects.add(saucer)
    }

    fun add(ship: Ship) {
        ships.add(ship)
        colliders.add(ship)
        spaceObjects.add(ship)
    }

    fun add(splat: Splat) {
        splats.add(splat)
        spaceObjects.add(splat)
    }

    fun addScore(score: Int) {
        scoreKeeper.addScore(score)
    }

    fun any(predicate: (SpaceObject)-> Boolean): Boolean {
        return spaceObjects().any(predicate)
    }

    fun applyChanges(transaction: Transaction) = transaction.applyChanges(this)

    fun asteroids() = spaceObjects().filterIsInstance<Asteroid>()
    fun missiles() = spaceObjects.filterIsInstance<Missile>()
    fun saucers() = spaceObjects.filterIsInstance<Saucer>()
    fun ships() = spaceObjects.filterIsInstance<Ship>()
    fun splats() = spaceObjects.filterIsInstance<Splat>()

    fun asteroidCount(): Int = asteroids().size

    fun clear() {
        scoreKeeper.clear()
        for ( coll in allCollections()) {
            coll.clear()
        }
        spaceObjects.clear()
        colliders.clear()
    }

    fun forEachInteracting(action: (SpaceObject)->Unit) =
        spaceObjects().forEach(action)

    fun contains(obj:SpaceObject): Boolean {
        return spaceObjects().contains(obj)
    }

    fun pairsToCheck(): List<Pair<Collider, Collider>> {
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
        spaceObjects.remove(spaceObject)
        if (spaceObject is Collider ) colliders.remove(spaceObject)
    }

    fun saucerIsPresent(): Boolean {
        return saucers().isNotEmpty()
    }

    fun saucerIsMissing(): Boolean {
        return saucers().isEmpty()
    }

    fun shipIsPresent(): Boolean {
        return ships.isNotEmpty()
    }

    fun shipIsMissing(): Boolean {
        return ships.isEmpty()
    }

    val size get() = spaceObjects().size
}
