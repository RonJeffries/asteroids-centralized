package com.ronjeffries.ship

class SpaceObjectCollection {
    var scoreKeeper = ScoreKeeper()
    val spaceObjects = mutableListOf<ISpaceObject>()
    val attackers = mutableListOf<ISpaceObject>()
    val targets = mutableListOf<ISpaceObject>()
    val deferredActions = mutableListOf<ISpaceObject>()
    // update function below if you add to these
    fun allCollections(): List<MutableList<ISpaceObject>> {
        return listOf (spaceObjects, attackers, targets, deferredActions)
    }

    fun add(spaceObject: ISpaceObject) {
        if ( spaceObject is Score ) {
            scoreKeeper.addScore(spaceObject.score)
            return
        }
        if ( spaceObject is DeferredAction) {
            deferredActions.add(spaceObject)
            return
        }
        spaceObjects.add(spaceObject)
        if (spaceObject is Missile) attackers.add(spaceObject)
        if (spaceObject is Ship) {
            attackers.add(spaceObject)
            targets.add(spaceObject)
        }
        if (spaceObject is Saucer)  {
            attackers.add(spaceObject)
            targets.add(spaceObject)
        }
        if (spaceObject is Asteroid) targets.add(spaceObject)
    }
    
    fun addAll(newbies: Collection<ISpaceObject>) {
        newbies.forEach{ add(it) }
    }

    fun any(predicate: (ISpaceObject)-> Boolean): Boolean {
        return spaceObjects.any(predicate)
    }

    fun applyChanges(transaction: Transaction) = transaction.applyChanges(this)

    fun asteroidCount(): Int = targets.filterIsInstance<Asteroid>().size

    fun clear() {
        for ( coll in allCollections()) {
            coll.clear()
        }
    }

    fun forEach(spaceObject: (ISpaceObject)->Unit) = spaceObjects.forEach(spaceObject)

    fun contains(obj:ISpaceObject): Boolean {
        return spaceObjects.contains(obj)
    }

    fun pairsToCheck(): List<Pair<ISpaceObject, ISpaceObject>> {
        val pairs = mutableListOf<Pair<ISpaceObject, ISpaceObject>>()
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

    fun removeAndFinalizeAll(moribund: Set<ISpaceObject>) {
        moribund.forEach { addAll(it.subscriptions.finalize()) }
        removeAll(moribund)
    }

    fun removeAll(moribund: Set<ISpaceObject>) {
        spaceObjects.removeAll(moribund)
        attackers.removeAll(moribund)
        targets.removeAll(moribund)
        deferredActions.removeAll(moribund)
    }

    fun remove(spaceObject: ISpaceObject) {
        removeAll(setOf(spaceObject))
    }

    fun saucerMissing(): Boolean {
        return targets.filterIsInstance<Saucer>().isEmpty()
    }

    val size get() = spaceObjects.size
}
