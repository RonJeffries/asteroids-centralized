package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<SpaceObject>()
    val removes = mutableSetOf<SpaceObject>()
    var shouldClear = false
    var score = 0

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
        spaceObjectCollection.removeAndFinalizeAll(removes)
        spaceObjectCollection.addAll(adds)
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
