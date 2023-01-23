package com.ronjeffries.ship

import org.openrndr.draw.Drawer

interface SpaceObject {
    fun draw(drawer: Drawer)
    fun update(deltaTime: Double, trans: Transaction)
}
