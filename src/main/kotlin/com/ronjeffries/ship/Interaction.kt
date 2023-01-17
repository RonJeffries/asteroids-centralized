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
                saucer.interact(asteroid, trans)
                asteroid.interact(saucer, trans)
            }
        }
    }

    private fun shipVsSaucerAsteroids() {
        ships.forEach { ship ->
            saucers.forEach { saucer ->
                ship.interact(saucer, trans)
                saucer.interact(ship, trans)
            }
            asteroids.forEach { asteroid ->
                ship.interact(asteroid, trans)
                asteroid.interact(ship, trans)
            }
        }
    }

    private fun missilesVsMissileShipSaucerAsteroids() {
        missiles.forEach { missile ->
            missiles.forEach { other ->
                if (other !== missile ) {
                    missile.interact(other, trans)
                    other.interact(missile, trans)
                }
            }
            ships.forEach {  ship ->
                ship.interact(missile, trans)
                missile.interact(ship, trans)
            }
            saucers.forEach {  saucer ->
                saucer.interact(missile, trans)
                missile.interact(saucer, trans)
            }
            asteroids.forEach {  asteroid ->
                asteroid.interact(missile, trans)
                missile.interact(asteroid, trans)
            }
        }
    }
}
