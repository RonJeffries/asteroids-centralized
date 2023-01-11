package com.ronjeffries.ship

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class DeferredActionTest {
    var done = false
    @Test
    fun `triggers after n seconds`() {
        val trans = Transaction()
        DeferredAction(2.0, trans) { _ -> done = true}
        val tmw = trans.deferredActionAdds.first()
        val newTrans = Transaction()
        tmw.update(1.1, newTrans)
        assertThat(done).isEqualTo(false)
        assertThat(newTrans.adds).isEmpty()
        assertThat(newTrans.removes).isEmpty()
        tmw.update(1.1, newTrans)
        assertThat(done).isEqualTo(true)
        assertThat(newTrans.adds).isEmpty()
        assertThat(newTrans.deferredActionRemoves).contains(tmw)
    }

    @Test
    fun `conditional action triggers after n seconds and true condition`() {
        val trans = Transaction()
        var ready = false
        val cond = { ready }
        val dca = DeferredAction(2.0, cond, trans) { _ -> done = true }
        val newTrans = Transaction()
        dca.update(1.1, newTrans)
        assertThat(done).describedAs("not time yet").isEqualTo(false)
        assertThat(newTrans.adds).isEmpty()
        assertThat(newTrans.removes).isEmpty()
        dca.update(1.1, newTrans)
        assertThat(done).describedAs("not ready yet").isEqualTo(false)
        ready = true
        dca.update(0.1, newTrans)
        assertThat(done).describedAs("time up and ready").isEqualTo(true)
        assertThat(newTrans.adds).isEmpty()
        assertThat(newTrans.deferredActionRemoves).contains(dca)
    }
}