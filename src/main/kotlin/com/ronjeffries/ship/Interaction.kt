package com.ronjeffries.ship

class Interaction(
    private val missiles: List<Missile>,
    private val ships: List<Ship>,
    private val saucers: List<Saucer>,
    private val asteroids: List<Asteroid>,
    private val trans: Transaction
) {

    init {
        missilesVsShipSaucerAsteroids()
        shipVsSaucerAsteroids()
        saucerVsAsteroids()
    }

    private fun saucerVsAsteroids() {
        saucers.forEach { saucer->
            asteroids.forEach { asteroid ->
                saucer.subscriptions.interactWithAsteroid(asteroid, trans)
                asteroid.subscriptions.interactWithSaucer(saucer, trans)
            }
        }
    }

    private fun shipVsSaucerAsteroids() {
        ships.forEach { ship ->
            saucers.forEach { saucer ->
                ship.subscriptions.interactWithSaucer(saucer, trans)
                saucer.subscriptions.interactWithShip(ship, trans)
            }
            asteroids.forEach { asteroid ->
                ship.subscriptions.interactWithAsteroid(asteroid, trans)
                asteroid.subscriptions.interactWithShip(ship, trans)
            }
        }
    }

    private fun missilesVsShipSaucerAsteroids() {
        missiles.forEach { missile ->
            ships.forEach {  ship ->
                ship.subscriptions.interactWithMissile(missile, trans)
                missile.subscriptions.interactWithShip(ship, trans)
            }
            saucers.forEach {  saucer ->
                saucer.subscriptions.interactWithMissile(missile, trans)
                missile.subscriptions.interactWithSaucer(saucer, trans)
            }
            asteroids.forEach {  asteroid ->
                asteroid.subscriptions.interactWithMissile(missile, trans)
                missile.subscriptions.interactWithAsteroid(asteroid, trans)
            }
        }
    }

}
