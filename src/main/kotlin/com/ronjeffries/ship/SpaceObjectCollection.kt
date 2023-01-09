package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()
    val spaceObjects = mutableListOf<InteractingSpaceObject>()
    val attackers = mutableListOf<InteractingSpaceObject>()
    val targets = mutableListOf<InteractingSpaceObject>()
    val deferredActions = mutableListOf<DeferredAction>()
    // update function below if you add to these
    fun allCollections(): List<MutableList<out SpaceObject>> {
        return listOf (spaceObjects, attackers, targets, deferredActions)
    }

    fun add(spaceObject: SpaceObject) {
        when (spaceObject) {
            is DeferredAction -> deferredActions.add(spaceObject)
            else -> addActualSpaceObjects(spaceObject as InteractingSpaceObject)
        }
    }

    private fun addActualSpaceObjects(spaceObject: InteractingSpaceObject) {
        spaceObjects.add(spaceObject)
        when (spaceObject) {
            is Missile -> attackers.add(spaceObject)
            is Asteroid -> targets.add(spaceObject)
            is Ship -> {
                attackers.add(spaceObject)
                targets.add(spaceObject)
            }
            is Saucer -> {
                attackers.add(spaceObject)
                targets.add(spaceObject)
            }
        }
    }

    fun addAll(newbies: Collection<SpaceObject>) {
        newbies.forEach{ add(it) }
    }

    fun addScore(score: Int) {
        scoreKeeper.addScore(score)
    }

    fun any(predicate: (InteractingSpaceObject)-> Boolean): Boolean {
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

    fun forEachInteracting(action: (InteractingSpaceObject)->Unit) =
        spaceObjects.forEach(action)

    fun contains(obj:InteractingSpaceObject): Boolean {
        return spaceObjects.contains(obj)
    }

    fun pairsToCheck(): List<Pair<InteractingSpaceObject, InteractingSpaceObject>> {
        val pairs = mutableListOf<Pair<InteractingSpaceObject, InteractingSpaceObject>>()
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

    fun removeAndFinalizeAll(moribund: Set<SpaceObject>) {
        moribund.forEach {
            if ( it is InteractingSpaceObject) addAll(it.subscriptions.finalize())
        }
        removeAll(moribund)
    }

    fun removeAll(moribund: Set<SpaceObject>) {
        spaceObjects.removeAll(moribund)
        attackers.removeAll(moribund)
        targets.removeAll(moribund)
        deferredActions.removeAll(moribund)
    }

    fun remove(spaceObject: SpaceObject) {
        removeAll(setOf(spaceObject))
    }

    fun saucerMissing(): Boolean {
        return targets.filterIsInstance<Saucer>().isEmpty()
    }

    fun shipIsPresent(): Boolean {
        return attackers.filterIsInstance<Ship>().isNotEmpty()
    }

    val size get() = spaceObjects.size
}
