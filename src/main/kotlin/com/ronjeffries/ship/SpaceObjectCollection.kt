package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()

    val asteroids = mutableListOf<Asteroid>()
    val attackers = mutableListOf<SpaceObject>()
    val deferredActions = mutableListOf<DeferredAction>()
    val missiles = mutableListOf<Missile>()
    val saucers = mutableListOf<Saucer>()
    val ships = mutableListOf<Ship>()
    val splats = mutableListOf<Splat>()
    val targets = mutableListOf<SpaceObject>()

    fun spaceObjects():List<SpaceObject> = asteroids + missiles + saucers + ships + splats
    // update function below if you add to these
    fun allCollections(): List<MutableList<out SpaceObject>> {
        return listOf (asteroids, attackers, deferredActions, missiles, saucers, ships, splats, targets)
    }

    fun add(deferredAction: DeferredAction) {
        deferredActions.add(deferredAction)
    }

    fun add(spaceObject: SpaceObject) {
        addActualSpaceObjects(spaceObject)
    }

    private fun add(asteroid: Asteroid) {
//        spaceObjects.add(asteroid)
        asteroids.add(asteroid)
        targets.add(asteroid)
    }

    fun add(missile: Missile) {
//        spaceObjects.add(missile)
        attackers.add(missile)
        missiles.add(missile)
    }

    fun add(saucer: Saucer) {
//        spaceObjects.add(saucer)
        attackers.add(saucer)
        targets.add(saucer)
        saucers.add(saucer)
    }

    fun add(ship: Ship) {
//        spaceObjects.add(ship)
        attackers.add(ship)
        targets.add(ship)
        ships.add(ship)
    }

    fun add(splat: Splat) {
//        spaceObjects.add(splat)
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

    fun saucerMissing(): Boolean {
        return targets.filterIsInstance<Saucer>().isEmpty()
    }

    fun shipIsPresent(): Boolean {
        return attackers.filterIsInstance<Ship>().isNotEmpty()
    }

    val size get() = spaceObjects().size
}
