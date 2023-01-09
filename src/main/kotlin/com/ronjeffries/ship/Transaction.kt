package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<SpaceObject>()
    val removes = mutableSetOf<SpaceObject>()
    var shouldClear = false

    fun accumulate(t: Transaction) {
        t.adds.forEach {add(it)}
        t.removes.forEach {remove(it)}
    }

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
    fun hasAdd(so:SpaceObject): Boolean = adds.contains(so)
    fun hasRemove(so:SpaceObject): Boolean = removes.contains(so)
}
