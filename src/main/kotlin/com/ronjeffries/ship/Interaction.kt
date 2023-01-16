package com.ronjeffries.ship

class Interaction(
    private val missiles: List<Missile>,
    private val ships: List<Ship>,
    private val saucers: List<Saucer>,
    private val asteroids: List<Asteroid>,
    private val trans: Transaction
) {

    init {
        missilesVsMissileShipSaucerAsteroids()
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

    private fun missilesVsMissileShipSaucerAsteroids() {
        missiles.forEach { missile ->
            missiles.forEach { other ->
                if (other != missile ) {
                    missile.interactWithMissile(other, trans)
                    other.interactWithMissile(missile, trans)
                }
            }
            ships.forEach {  ship ->
                ship.interactWithMissile(missile, trans)
                missile.interactWithShip(ship, trans)
            }
            saucers.forEach {  saucer ->
                saucer.interactWithMissile(missile, trans)
                missile.interactWithSaucer(saucer, trans)
            }
            asteroids.forEach {  asteroid ->
                asteroid.subscriptions.interactWithMissile(missile, trans)
                missile.subscriptions.interactWithAsteroid(asteroid, trans)
            }
        }
    }
}
