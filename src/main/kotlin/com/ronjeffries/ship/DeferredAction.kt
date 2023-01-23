package com.ronjeffries.ship

import org.openrndr.draw.Drawer

class DeferredAction(
    val delay: Double,
    val cond: () -> Boolean,
    initialTransaction: Transaction,
    private val action: (Transaction) -> Unit
) : SpaceObject {
    constructor(delay: Double, initialTransaction: Transaction, action: (Transaction) -> Unit):
            this(delay, { true }, initialTransaction, action)
    private var elapsedTime = 0.0

    init {
        elapsedTime = 0.0
        initialTransaction.add(this)
    }

    override fun draw(drawer: Drawer) {}

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime >= delay && cond() ) {
            action(trans)
            trans.remove(this)
        }
    }
}
