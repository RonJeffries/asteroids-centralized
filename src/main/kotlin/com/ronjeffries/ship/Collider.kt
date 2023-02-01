package com.ronjeffries.ship

interface Collider {
    val position: Point
    val killRadius: Double
    fun interact(asteroid: Asteroid, trans: Transaction)
    fun interact(missile: Missile, trans: Transaction)
    fun interact(saucer: Saucer, trans: Transaction)
    fun interact(ship: Ship, trans: Transaction)
}

interface Collidable: SpaceObject {
    val position: Point
    val killRadius: Double
    fun interactWith(other: Collidable, trans: Transaction)
    val collisionStrategy: Collider
}