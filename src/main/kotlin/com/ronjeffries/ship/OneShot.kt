package com.ronjeffries.ship

class OneShot(private val delay: Double, val cond: ()->Boolean, private val action: (Transaction)->Unit) {
    constructor (delay: Double, action: (Transaction)->Unit):
            this(delay, { true }, action)

    private var deferred: DeferredAction? = null

    fun execute(trans: Transaction) {
        deferred = deferred ?: DeferredAction(delay, cond, trans) {
            deferred = null
            action(it)
        }
    }

    fun cancel(trans: Transaction) {
        deferred?.let { trans.remove(it); deferred = null }
    }
}