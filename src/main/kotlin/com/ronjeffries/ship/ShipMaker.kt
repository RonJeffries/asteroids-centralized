package com.ronjeffries.ship

import kotlin.random.Random

class ShipMaker(val ship: Ship, val scoreKeeper: ScoreKeeper = ScoreKeeper()) : ISpaceObject, InteractingSpaceObject {
    var safeToEmerge = true
    private var elapsedTime = 0.0

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
    }

    override fun callOther(other: InteractingSpaceObject, trans: Transaction) =
        other.subscriptions.interactWithShipMaker(this, trans)

    override val subscriptions = Subscriptions (
        beforeInteractions = { safeToEmerge = true },
        interactWithMissile = { _, _ -> safeToEmerge = false },
        interactWithAsteroid = { asteroid, _ -> safeToEmerge = isAnythingInTheWay(asteroid) },
        interactWithSaucer = { _, _ -> safeToEmerge = false },
        afterInteractions = { trans->
            if (elapsedTime > U.SHIP_MAKER_DELAY && safeToEmerge) {
                replaceTheShip(trans)
            }
        }
    )

    private fun isAnythingInTheWay(collider: Collider) = safeToEmerge && !tooClose(collider)

    private fun tooClose(collider: Collider): Boolean {
        return ship.position.distanceTo(collider.position) < U.SAFE_SHIP_DISTANCE
    }

    private fun replaceTheShip(trans: Transaction) {
        trans.remove(this)
        trans.add(ShipChecker(ship, scoreKeeper))
        trans.add(ship)
        ship.dropIn()
    }
}
