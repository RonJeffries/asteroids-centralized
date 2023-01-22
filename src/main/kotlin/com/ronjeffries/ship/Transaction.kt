package com.ronjeffries.ship

class Transaction {
    private val asteroids = mutableListOf<Asteroid>()
    private val missiles = mutableListOf<Missile>()
    private val saucers = mutableListOf<Saucer>()
    private val ships = mutableListOf<Ship>()
    val splats = mutableListOf<Splat>()
    val removes = mutableSetOf<SpaceObject>()
    val deferredActionAdds = mutableSetOf<DeferredAction>()
    val deferredActionRemoves = mutableSetOf<DeferredAction>()
    var shouldClear = false
    var score = 0

    fun add(asteroid: Asteroid) {asteroids.add(asteroid)}
    fun add(missile: Missile) {missiles.add(missile)}
    fun add(saucer: Saucer) {saucers.add(saucer)}
    fun add(ship: Ship) {ships.add(ship)}
    fun add(splat: Splat) {splats.add(splat)}

    fun add(deferredAction: DeferredAction) {
        deferredActionAdds.add(deferredAction)
    }

    fun spaceObjects() = asteroids+missiles+saucers+ships+splats

    fun remove(deferredAction: DeferredAction) {
        deferredActionRemoves.add(deferredAction)
    }

    fun addScore(scoreToAdd: Int) {
        score += scoreToAdd
    }

    fun applyChanges(spaceObjectCollection: SpaceObjectCollection) {
        if (shouldClear ) spaceObjectCollection.clear()
        spaceObjectCollection.addScore(score)
        removes.forEach { spaceObjectCollection.remove(it)}
        deferredActionRemoves.forEach { spaceObjectCollection.remove(it)}
        asteroids.forEach { spaceObjectCollection.add(it)}
        missiles.forEach { spaceObjectCollection.add(it)}
        saucers.forEach { spaceObjectCollection.add(it)}
        ships.forEach { spaceObjectCollection.add(it)}
        splats.forEach { spaceObjectCollection.add(it)}
        deferredActionAdds.forEach { spaceObjectCollection.add(it)}
    }

    fun clear() {
        shouldClear = true
    }

    fun remove(spaceObject: SpaceObject) {
        removes.add(spaceObject)
    }

    // testing
    fun firstRemove(): SpaceObject = removes.toList()[0]
}
