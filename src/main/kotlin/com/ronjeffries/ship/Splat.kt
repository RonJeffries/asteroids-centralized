package com.ronjeffries.ship

import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer

class Splat(
    override var position: Point,
    val scale: Double = 1.0,
    val color: ColorRGBa = ColorRGBa.WHITE,
    val velocity: Velocity = Velocity.ZERO
) : SpaceObject, Collidable, Collider {
    constructor(ship: Ship) : this(ship.position, 2.0, ColorRGBa.WHITE, ship.velocity*0.5)
    constructor(missile: Missile) : this(missile.position, 0.5, missile.color, missile.velocity*0.5)
    constructor(saucer: Saucer) : this(saucer.position, 2.0, ColorRGBa.GREEN, saucer.velocity*0.5)
    constructor(asteroid: Asteroid) : this(asteroid.position, 2.0, ColorRGBa.WHITE, asteroid.velocity*0.5)

    override val collisionStrategy: Collider
        get() = this
    override val killRadius: Double
        get() = 0.0
    var elapsedTime = 0.0
    private val lifetime = U.SPLAT_LIFETIME
    private val view = SplatView(lifetime)

    override fun interactWith(other: Collidable, trans: Transaction) = Unit
    override fun interact(asteroid: Asteroid, trans: Transaction) = Unit
    override fun interact(missile: Missile, trans: Transaction) = Unit
    override fun interact(saucer: Saucer, trans: Transaction) = Unit
    override fun interact(ship: Ship, trans: Transaction) = Unit

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime > lifetime) trans.remove(this)
        position = (position + velocity * deltaTime).cap()
    }

    override fun draw(drawer: Drawer) {
        drawer.translate(position)
        view.draw(this, drawer)
    }
}
