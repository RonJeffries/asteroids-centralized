package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class GameCycler(knownObjects: SpaceObjectCollection, numberOfAsteroidsToCreate: Int, ship: Ship, saucer: Saucer) {
    fun cycle(game: Game, deltaTime: Double, drawer: Drawer?) {
        game.stranglerCycle(deltaTime, drawer)
    }
}
