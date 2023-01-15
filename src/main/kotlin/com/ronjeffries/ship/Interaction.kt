package com.ronjeffries.ship

class Interaction(
    missiles: List<Missile>,
    ships: List<Ship>,
    saucers: List<Saucer>,
    asteroids: List<Asteroid>,
    trans: Transaction
) {

    init {
        missiles.forEach { missile ->
            ships.forEach {  ship ->
                ship.subscriptions.interactWithMissile(missile, trans)
                missile.subscriptions.interactWithShip(ship, trans)
            }
        }
    }

}
