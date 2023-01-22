package com.ronjeffries.ship

class Transaction {
    private val spaceObjects = mutableListOf<SpaceObject>()
    val removes = mutableSetOf<SpaceObject>()
    val deferredActionAdds = mutableSetOf<DeferredAction>()
    val deferredActionRemoves = mutableSetOf<DeferredAction>()
    private var shouldClear = false
    private var score = 0

    fun add(deferredAction: DeferredAction) {
        deferredActionAdds.add(deferredAction)
    }

    fun add(spaceObject: SpaceObject) { spaceObjects.add(spaceObject) }

    fun addScore(scoreToAdd: Int) {
        score += scoreToAdd
    }

    fun applyChanges(spaceObjectCollection: SpaceObjectCollection) {
        if (shouldClear ) spaceObjectCollection.clear()
        spaceObjectCollection.addScore(score)
        removes.forEach { spaceObjectCollection.remove(it)}
        deferredActionRemoves.forEach { spaceObjectCollection.remove(it)}
        spaceObjects.forEach { spaceObjectCollection.add(it)}
        deferredActionAdds.forEach { spaceObjectCollection.add(it)}
    }

    fun clear() {
        shouldClear = true
    }

    fun remove(deferredAction: DeferredAction) {
        deferredActionRemoves.add(deferredAction)
    }

    fun remove(spaceObject: SpaceObject) {
        removes.add(spaceObject)
    }

    fun spaceObjects() = spaceObjects

    // testing
    fun firstRemove(): SpaceObject = removes.toList()[0]
}
