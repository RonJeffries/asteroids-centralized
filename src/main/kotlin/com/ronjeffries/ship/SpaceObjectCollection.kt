package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()

    private val colliders = mutableListOf<Collider>()
    private val deferredActions = mutableListOf<DeferredAction>()
    private val spaceObjects = mutableListOf<SpaceObject>()


    fun add(deferredAction: DeferredAction) {
        deferredActions.add(deferredAction)
    }

    fun add (spaceObject: SpaceObject) {
        spaceObjects.add(spaceObject)
        if (spaceObject is Collider) colliders.add(spaceObject)
    }

    fun addScore(score: Int) {
        scoreKeeper.addScore(score)
    }

    fun any(predicate: (SpaceObject)-> Boolean): Boolean {
        return spaceObjects().any(predicate)
    }

    fun applyChanges(transaction: Transaction) = transaction.applyChanges(this)

    fun asteroids() = spaceObjects().filterIsInstance<Asteroid>()
    fun colliders() = colliders
    fun deferredActions() = deferredActions
    fun missiles() = spaceObjects.filterIsInstance<Missile>()
    fun saucers() = spaceObjects.filterIsInstance<Saucer>()
    fun ships() = spaceObjects.filterIsInstance<Ship>()
    fun spaceObjects():List<SpaceObject> = spaceObjects
    fun splats() = spaceObjects.filterIsInstance<Splat>()

    fun asteroidCount(): Int = asteroids().size

    fun clear() {
        scoreKeeper.clear()
        deferredActions.clear()
        spaceObjects.clear()
        colliders.clear()
    }

    fun forEach(action: (SpaceObject)->Unit) {
        deferredActions().forEach(action)
        spaceObjects().forEach(action)
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
        deferredActions.remove(spaceObject)
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
        return ships().isNotEmpty()
    }

    fun shipIsMissing(): Boolean {
        return ships().isEmpty()
    }

    val size get() = spaceObjects().size
}
