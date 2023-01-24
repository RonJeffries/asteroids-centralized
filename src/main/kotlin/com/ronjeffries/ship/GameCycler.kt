package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class GameCycler(private val game: Game, private val knownObjects: SpaceObjectCollection, numberOfAsteroidsToCreate: Int, ship: Ship, saucer: Saucer) {

    fun cycle(deltaTime: Double, drawer: Drawer?) {
        tick(deltaTime)
        beforeInteractions()
        game.processInteractions()
        game.stranglerCycle(deltaTime, drawer)
    }

    private fun beforeInteractions() = knownObjects.saucers().forEach { it.beforeInteractions() }

    private fun tick(deltaTime: Double) {
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
