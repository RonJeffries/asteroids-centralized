package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface SpaceObject {
    val position: Point
    val killRadius: Double
    fun interactWith(other: SpaceObject, trans: Transaction)
    val collisionStrategy: Collider
    fun draw(drawer: Drawer)
    fun update(deltaTime: Double, trans: Transaction)
}
