package com.ronjeffries.ship

class DeferredAction(
    val delay: Double,
    val cond: () -> Boolean,
    initialTransaction: Transaction,
    private val action: (Transaction) -> Unit
) : InteractingSpaceObject, SpaceObject {
    constructor(delay: Double, initialTransaction: Transaction, action: (Transaction) -> Unit):
            this(delay, { true }, initialTransaction, action)
    var elapsedTime = 0.0

    init {
        elapsedTime = 0.0
        initialTransaction.add(this)
    }

    override fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime >= delay && cond() ) {
            action(trans)
            trans.remove(this)
        }
    }

    override val subscriptions: Subscriptions = Subscriptions()
    override fun callOther(other: InteractingSpaceObject, trans: Transaction) {}
}
