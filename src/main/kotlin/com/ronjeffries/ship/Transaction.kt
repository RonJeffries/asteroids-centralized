package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<SpaceObject>()
    val removes = mutableSetOf<SpaceObject>()
    val deferredActionAdds = mutableSetOf<DeferredAction>()
    val deferredActionRemoves = mutableSetOf<DeferredAction>()
    var shouldClear = false
    var score = 0

    fun add(deferredAction: DeferredAction) {
        deferredActionAdds.add(deferredAction)
    }

    fun remove(deferredAction: DeferredAction) {
        deferredActionRemoves.add(deferredAction)
    }

    fun add(spaceObject: SpaceObject) {
        adds.add(spaceObject)
    }

    fun addAll(adds: List<SpaceObject>) {
        adds.forEach { add(it) }
    }

    fun addScore(scoreToAdd: Int) {
        score += scoreToAdd
    }

    fun applyChanges(spaceObjectCollection: SpaceObjectCollection) {
        if (shouldClear ) spaceObjectCollection.clear()
        spaceObjectCollection.addScore(score)
        removes.forEach { spaceObjectCollection.remove(it)}
        deferredActionRemoves.forEach { spaceObjectCollection.remove(it)}
        adds.forEach { spaceObjectCollection.add(it)}
        deferredActionAdds.forEach { spaceObjectCollection.add(it)}
    }

    fun clear() {
        shouldClear = true
    }

    fun remove(spaceObject: SpaceObject) {
        removes.add(spaceObject)
    }

    // testing
    fun firstAdd(): SpaceObject = adds.toList()[0]
    fun firstRemove(): SpaceObject = removes.toList()[0]
}
