package com.ronjeffries.ship

class Interaction {
    // spike sketch of centralized interaction
    fun interact(attacker: SpaceObject, target: SpaceObject, trans: Transaction) {
        if (attacker == target) return
        if (outOfRange(attacker, target)) {
            println("out of range $attacker, $target")
            return
        }
        println("in range")
        if (attacker is Missile) {
            if (target is Ship) {
                remove(attacker, trans)
                explode(target, trans)
            } else if (target is Saucer) {
                remove(attacker, trans)
                explode(target, trans)
            } else if (target is Asteroid) {
                remove(attacker, trans)
                if (attacker.missileIsFromShip) addScore(target)
                split(target)
            }
        } else if (attacker is Ship) {
            if (target is Saucer) {
                explode(attacker, trans)
                remove(attacker, trans)
                addScore(target)
                remove(target, trans)
            } else if (target is Asteroid) {
                explode(attacker, trans)
                remove(attacker, trans)
                addScore(target)
                split(target)
            }
        } else { // attacker is Saucer
            if ( target is Ship ) {} // already done
            else { // target is Asteroid
                explode(attacker, trans)
                remove(attacker, trans)
                split(target)
            }
        }
    }

    private fun outOfRange(a: SpaceObject, b: SpaceObject): Boolean {
        return ! Collision(a as Collider).hit(b as Collider)
    }
    private fun remove(o: SpaceObject, trans: Transaction) {
        trans.remove(o)
    }
    private fun explode(o: SpaceObject, trans: Transaction) {
        trans.add(splatFor(o))
        remove(o, trans)
    }
    private fun splatFor(o: SpaceObject): Splat {
        return when (o) {
            is Asteroid -> Splat(o)
            is Missile -> Splat(o)
            is Ship -> Splat(o)
            is Saucer -> Splat(o)
            else -> Splat(Point(-100.0, -100.0))
        }
    }
    private fun split(o: SpaceObject) {}
    private fun addScore(o: SpaceObject) {}

}
