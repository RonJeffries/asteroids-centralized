package com.ronjeffries.ship

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrndr.math.Vector2

class SpaceObjectCollectionTest {
    @Test
    fun `create flyers instance`() {
        val spaceObjectCollection = SpaceObjectCollection()
        val a = Asteroid(Vector2(100.0, 100.0))
        spaceObjectCollection.add(a)
        val s = Ship(
            position = Vector2(100.0, 150.0)
        )
        spaceObjectCollection.add(s)
        assertThat(spaceObjectCollection.size).isEqualTo(2)
    }

    @Test
    fun `collision detection`() {
        val game = Game()
        val a = Asteroid(Vector2(100.0, 100.0))
        game.currentMix().add(a)
        val s = Ship(
            position = Vector2(100.0, 150.0)
        )
        game.currentMix().add(s)
        assertThat(game.currentMix().size).isEqualTo(2)
        val colliders = GameCycler(game.currentMix()).changesDueToInteractions()
        assertThat(colliders.removes.size).isEqualTo(2)
    }

    @Test
    fun `stringent colliders`() {
        val p1 = Vector2(100.0, 100.0)
        val p2 = Vector2(1250.0, 100.0)
        val game = Game()
        val a0 = Asteroid(p1) // yes
        game.currentMix().add(a0)
        val m1 = Ship(position = p1) // yes
        game.currentMix().add(m1)
        val s2 = Ship(
            position = p1
        ) // yes kr=150
        game.currentMix().add(s2)
        val a3 = Asteroid(p2) // no
        game.currentMix().add(a3)
        val a4 = Asteroid(p2) // no
        game.currentMix().add(a4)
        val colliders = GameCycler(game.currentMix()).changesDueToInteractions()
        assertThat(colliders.removes.size).isEqualTo(3)
    }

    @Test
    fun `performWithTransaction works`() {
        val s = SpaceObjectCollection()
        val saucer = Saucer()
        val ship = Ship(Point.ZERO)
        s.performWithTransaction { trans-> trans.add(saucer); trans.add(ship) }
        assertThat(s.ships).contains(ship)
        assertThat(s.saucers).contains(saucer)
    }

    @Test
    fun `collection isolates DeferredObject instances`() {
        val s = SpaceObjectCollection()
        assertThat(s.deferredActions().size).describedAs("deferred before").isEqualTo(0)
        assertThat(s.spaceObjects().size).describedAs("all before").isEqualTo(0)
        val deferred = DeferredAction(3.0, Transaction()) {}
        s.add(deferred)
        assertThat(s.deferredActions().size).describedAs("deferred after add").isEqualTo(1)
        assertThat(s.spaceObjects().size).describedAs("all after add").isEqualTo(0) // DA's not added to mix
        s.remove(deferred)
        assertThat(s.deferredActions().size).describedAs("deferred after remove").isEqualTo(0)
        assertThat(s.spaceObjects().size).describedAs("all after remove").isEqualTo(0)
    }

    @Test
    fun `can count asteroids`() {
        val s = SpaceObjectCollection()
        s.add(Asteroid(Point.ZERO))
        s.add(Ship(U.CENTER_OF_UNIVERSE))
        s.add(Asteroid(Point.ZERO))
        s.add(Asteroid(Point.ZERO))
        assertThat(s.size).isEqualTo(4)
        assertThat(s.asteroidCount()).isEqualTo(3)
    }

    @Test
    fun `can detect saucer`() {
        val s = SpaceObjectCollection()
        assertThat(s.saucerIsMissing()).isEqualTo(true)
        s.add(Saucer())
        assertThat(s.saucerIsMissing()).isEqualTo(false)
    }

    @Test
    fun `clear clears all sub-collections`() {
        val s = SpaceObjectCollection()
        s.add(Missile(Ship(U.CENTER_OF_UNIVERSE)))
        s.add(Asteroid(Point.ZERO))
        val deferredAction = DeferredAction(3.0, Transaction()) {}
        s.add(deferredAction)
        s.clear()
        assertThat(s.spaceObjects()).isEmpty()
        assertThat(s.deferredActions()).isEmpty()
    }

    @Test
    fun `asteroid collection works`() {
        val s = SpaceObjectCollection()
        val a = Asteroid(U.CENTER_OF_UNIVERSE)
        s.add(a)
        assertThat(s.asteroids.size).isEqualTo(1)
        assertThat(s.contains(a)).isEqualTo(true)
        s.remove(a)
        assertThat(s.asteroids.size).isEqualTo(0)
        assertThat(s.contains(a)).isEqualTo(false)
    }

    @Test
    fun `missile collection works`() {
        val s = SpaceObjectCollection()
        val m = Missile(U.CENTER_OF_UNIVERSE)
        s.add(m)
        assertThat(s.missiles.size).isEqualTo(1)
        s.remove(m)
        assertThat(s.missiles.size).isEqualTo(0)
    }

    @Test
    fun `splats collection works`() {
        val s = SpaceObjectCollection()
        val splat = Splat(U.CENTER_OF_UNIVERSE)
        s.add(splat)
        assertThat(s.splats.size).isEqualTo(1)
        s.remove(splat)
        assertThat(s.splats.size).isEqualTo(0)
    }

    @Test
    fun `ship collection works`() {
        val s = SpaceObjectCollection()
        val ship = Ship(U.CENTER_OF_UNIVERSE)
        s.add(ship)
        assertThat(s.ships.size).isEqualTo(1)
        s.remove(ship)
        assertThat(s.ships.size).isEqualTo(0)
    }

    @Test
    fun `saucer collection works`() {
        val s = SpaceObjectCollection()
        val saucer = Saucer()
        s.add(saucer)
        assertThat(s.saucers.size).isEqualTo(1)
        s.remove(saucer)
        assertThat(s.saucers.size).isEqualTo(0)
    }

// Needs replacement?
//    @Test
//    fun `removeAll removes from all collections`() {
//        val s = SpaceObjectCollection()
//        val toRemove: MutableSet<InteractingSpaceObject> = mutableSetOf()
//        for ( coll in s.allCollections()) {
//            val toAdd = Asteroid(U.CENTER_OF_UNIVERSE)
//            toRemove.add(toAdd)
//            coll.add(toAdd)
//        }
//        s.removeAll(toRemove)
//        for ( coll in s.allCollections()) {
//            assertThat(coll).isEmpty()
//        }
//    }

// Score isn't in any collections
//    @Test
//    fun `removeAndFinalizeAll removes from all collections`() {
//        val s = SpaceObjectCollection()
//        val toRemove: MutableSet<InteractingSpaceObject> = mutableSetOf()
//        for ( coll in s.allCollections()) {
//            val toAdd = Score(666)
//            toRemove.add(toAdd)
//            coll.add(toAdd)
//        }
//        s.removeAndFinalizeAll(toRemove)
//        for ( coll in s.allCollections()) {
//            assertThat(coll).isEmpty()
//        }
//    }
}