package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()
    val spaceObjects = mutableListOf<SpaceObject>()
    val attackers = mutableListOf<SpaceObject>()
    val targets = mutableListOf<SpaceObject>()
    val deferredActions = mutableListOf<DeferredAction>()
    val missiles = mutableListOf<Missile>()
    // update function below if you add to these
    fun allCollections(): List<MutableList<out SpaceObject>> {
        return listOf (spaceObjects, attackers, targets, deferredActions)
    }

    fun add(deferredAction: DeferredAction) {
        deferredActions.add(deferredAction)
    }

    fun add(spaceObject: SpaceObject) {
        addActualSpaceObjects(spaceObject)
    }

    fun add(missile: Missile) {
        spaceObjects.add(missile)
        missiles.add(missile)
        attackers.add(missile)
    }

    private fun addActualSpaceObjects(spaceObject: SpaceObject) {
        when (spaceObject) {
            is Missile -> add(spaceObject)
            is Asteroid -> {
                spaceObjects.add(spaceObject)
                targets.add(spaceObject)
            }
            is Ship -> {
                spaceObjects.add(spaceObject)
                attackers.add(spaceObject)
                targets.add(spaceObject)
            }
            is Saucer -> {
                spaceObjects.add(spaceObject)
                attackers.add(spaceObject)
                targets.add(spaceObject)
            }
            is Splat -> {
                spaceObjects.add(spaceObject)
            }
        }
    }

    fun addScore(score: Int) {
        scoreKeeper.addScore(score)
    }

    fun any(predicate: (SpaceObject)-> Boolean): Boolean {
        return spaceObjects.any(predicate)
    }

    fun applyChanges(transaction: Transaction) = transaction.applyChanges(this)

    fun asteroidCount(): Int = targets.filterIsInstance<Asteroid>().size

    fun clear() {
        scoreKeeper.clear()
        for ( coll in allCollections()) {
            coll.clear()
        }
    }

    fun forEachInteracting(action: (SpaceObject)->Unit) =
        spaceObjects.forEach(action)

    fun contains(obj:SpaceObject): Boolean {
        return spaceObjects.contains(obj)
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

    fun performWithTransaction(action: (Transaction) -> Unit ) {
        val trans = Transaction()
        action(trans)
        applyChanges(trans)
    }

    fun remove(deferredAction: DeferredAction) {
        deferredActions.remove(deferredAction)
    }

    fun remove(spaceObject: SpaceObject) {
        spaceObjects.remove(spaceObject)
        attackers.remove(spaceObject)
        targets.remove(spaceObject)
    }

    fun saucerMissing(): Boolean {
        return targets.filterIsInstance<Saucer>().isEmpty()
    }

    fun shipIsPresent(): Boolean {
        return attackers.filterIsInstance<Ship>().isNotEmpty()
    }

    val size get() = spaceObjects.size
}
