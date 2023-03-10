package com.ronjeffries.ship

class Controls {
    var accelerate = false
    var left = false
    var right = false
    var fire = false
    var hyperspace = false

    fun control(ship: Ship, deltaTime: Double, trans: Transaction) {
        if (hyperspace) {
            hyperspace = false
            ship.enterHyperspace(trans)
        }
        turn(ship, deltaTime)
        accelerate(ship, deltaTime)
        fire(ship).forEach { trans.add(it) }
    }

    private fun accelerate(ship:Ship, deltaTime: Double) {
        if (accelerate) {
            val deltaV = U.SHIP_ACCELERATION.rotate(ship.heading) * deltaTime
            ship.accelerate(deltaV)
        }
    }

    private fun fire(obj: Ship): List<Missile> = missilesToFire(obj).also { fire = false }

    private fun missilesToFire(obj: Ship): List<Missile> {
        return if (fire) {
            listOf(Missile(obj))
        } else {
            emptyList()
        }
    }

    private fun turn(obj: Ship, deltaTime: Double) {
        if (left) obj.turnBy(-U.SHIP_ROTATION_SPEED*deltaTime)
        if (right) obj.turnBy(U.SHIP_ROTATION_SPEED*deltaTime)
    }
}
