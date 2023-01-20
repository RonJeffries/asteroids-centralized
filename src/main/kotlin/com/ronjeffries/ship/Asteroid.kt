package com.ronjeffries.ship

import org.openrndr.draw.Drawer
import kotlin.math.pow
import kotlin.random.Random

class Asteroid(
    override var position: Point,
    val velocity: Velocity = U.randomVelocity(U.ASTEROID_SPEED),
    override val killRadius: Double = U.ASTEROID_KILL_RADIUS,
    private val splitCount: Int = 2
) : SpaceObject, Collider {
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

    private fun splitIfPossible(trans: Transaction) {
        if (splitCount >= 1) {
            trans.add(asSplit(this))
            trans.add(asSplit(this))
        }
    }

    private fun asSplit(asteroid: Asteroid): Asteroid =
        Asteroid(
            position = asteroid.position,
            killRadius = asteroid.killRadius / 2.0,
            splitCount = splitCount - 1
        )

    fun getScore(): Int {
        return when (splitCount) {
            2 -> 20
            1 -> 50
            0 -> 100
            else -> 0
        }
    }

    fun scale() =2.0.pow(splitCount)

    override fun interact(asteroid: Asteroid, trans: Transaction) {}

    override fun interact(missile: Missile, trans: Transaction) {
        checkCollision(missile, trans)
    }

    override fun interact(saucer: Saucer, trans: Transaction) {
        checkCollision(saucer, trans)
    }

    override fun interact(ship: Ship, trans: Transaction) {
        checkCollision(ship, trans)
    }

    override fun interactWith(other: Collider, trans: Transaction) {
        other.interact(this, trans)
    }

    private fun checkCollision(other: Collider, trans: Transaction) {
        Collision(this).executeOnHit(other) {
            dieDueToCollision(trans)
        }
    }

    fun dieDueToCollision(trans: Transaction) {
        trans.remove(this)
        trans.add(Splat(this))
        splitIfPossible(trans)
    }
}
