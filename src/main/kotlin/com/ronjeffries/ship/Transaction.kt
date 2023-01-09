package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<SpaceObject>()
    val removes = mutableSetOf<SpaceObject>()
    private var shouldClear = false

    fun add(spaceObject: SpaceObject) {
        adds.add(spaceObject)
    }

    fun addAll(adds: List<SpaceObject>) {
        adds.forEach { add(it) }
    }

    fun applyChanges(spaceObjectCollection: SpaceObjectCollection) {
        if (shouldClear ) spaceObjectCollection.clear()
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
