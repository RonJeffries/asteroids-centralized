package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface SpaceObject: Collider {
    fun interactWith(other: SpaceObject, trans: Transaction)
    fun draw(drawer: Drawer)
    fun update(deltaTime: Double, trans: Transaction)
}
