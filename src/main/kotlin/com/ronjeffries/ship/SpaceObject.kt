package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface SpaceObject {
    val position: Point
    val killRadius: Double
    fun interact(asteroid: Asteroid, trans: Transaction)
    fun interact(missile: Missile, trans: Transaction)
    fun interact(saucer: Saucer, trans: Transaction)
    fun interact(ship: Ship, trans: Transaction)
    fun interactWith(other: SpaceObject, trans: Transaction)
    val collisionStrategy: Collider
    fun draw(drawer: Drawer)
    fun update(deltaTime: Double, trans: Transaction)
}
