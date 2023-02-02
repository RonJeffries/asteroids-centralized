package com.ronjeffries.ship

class DeferredAction(
    val delay: Double,
    val cond: () -> Boolean,
    initialTransaction: Transaction,
    private val action: (Transaction) -> Unit
)  {
    constructor(delay: Double, initialTransaction: Transaction, action: (Transaction) -> Unit):
            this(delay, { true }, initialTransaction, action)
    private var elapsedTime = 0.0

    init {
        elapsedTime = 0.0
        initialTransaction.add(this)
    }


    fun update(deltaTime: Double, trans: Transaction) {
        elapsedTime += deltaTime
        if (elapsedTime >= delay && cond() ) {
            action(trans)
            trans.remove(this)
        }
    }
}
