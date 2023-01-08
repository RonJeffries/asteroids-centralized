package com.ronjeffries.ship

class Transaction {
    val adds = mutableSetOf<InteractingSpaceObject>()
    val removes = mutableSetOf<InteractingSpaceObject>()
    var shouldClear = false

    fun accumulate(t: Transaction) {
        t.adds.forEach {add(it)}
        t.removes.forEach {remove(it)}
    }

    fun add(spaceObject: InteractingSpaceObject) {
        adds.add(spaceObject)
    }

    fun addAll(adds: List<InteractingSpaceObject>) {
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

    fun remove(spaceObject: InteractingSpaceObject) {
        removes.add(spaceObject)
    }

    // testing
    fun firstAdd(): InteractingSpaceObject = adds.toList()[0]
    fun firstRemove(): InteractingSpaceObject = removes.toList()[0]
    fun hasAdd(so:InteractingSpaceObject): Boolean = adds.contains(so)
    fun hasRemove(so:InteractingSpaceObject): Boolean = removes.contains(so)
}
