package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import kotlin.math.pow
import kotlin.random.Random

class Asteroid(
    override var position: Point,
    val velocity: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    val splitCount: Int = 2
) : SpaceObject, Collidable {
    override val killRadius: Double =
        when (splitCount) {
            2 -> U.ASTEROID_KILL_RADIUS
            1 -> U.ASTEROID_KILL_RADIUS/2
            else -> U.ASTEROID_KILL_RADIUS/4
        }

    private val view = AsteroidView()
    val heading: Double = Random.nextDouble(360.0)

    override fun update(deltaTime: Double, trans: Transaction) {
        position = (position + velocity * deltaTime).cap()
    }

    override fun draw(drawer: Drawer) {
        drawer.fill = null
        drawer.translate(position)
        drawer.scale(U.DRAW_SCALE, U.DRAW_SCALE)
        view.draw(this, drawer)
    }

    fun getScore(): Int {
        return when (splitCount) {
            2 -> 20
            1 -> 50
            0 -> 100
            else -> 0
        }
    }

    fun scale() =2.0.pow(splitCount)

    override val collisionStrategy: Collider
        get() = AsteroidCollisionStrategy(this)

    override fun interactWith(other: Collidable, trans: Transaction)
        = other.collisionStrategy.interact(this, trans)

    fun splitIfPossible(trans: Transaction) {
        if (splitCount >= 1) {
            trans.add(this.asSplit())
            trans.add(this.asSplit())
        }
    }

    private fun asSplit(): Asteroid =
        Asteroid(
            position = position,
            splitCount = splitCount - 1
        )
}
