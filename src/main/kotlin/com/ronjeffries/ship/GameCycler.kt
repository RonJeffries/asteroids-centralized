package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class GameCycler(private val knownObjects: SpaceObjectCollection, numberOfAsteroidsToCreate: Int, ship: Ship, saucer: Saucer) {
    fun cycle(game: Game, deltaTime: Double, drawer: Drawer?) {
        tick(deltaTime)
        game.stranglerCycle(deltaTime, drawer)
    }

    fun tick(deltaTime: Double) {
        updateTimersFirst(deltaTime)
        thenUpdateSpaceObjects(deltaTime)
    }

    private fun updateTimersFirst(deltaTime: Double) {
        with (knownObjects) {
            performWithTransaction { trans ->
                deferredActions().forEach { it.update(deltaTime, trans) }
            }
        }
    }

    private fun thenUpdateSpaceObjects(deltaTime: Double) {
        with (knownObjects) {
            performWithTransaction { trans ->
                spaceObjects().forEach { it.update(deltaTime, trans) }
            }
        }
    }
}
