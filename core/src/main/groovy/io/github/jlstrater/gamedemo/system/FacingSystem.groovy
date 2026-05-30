package io.github.jlstrater.gamedemo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import io.github.jlstrater.gamedemo.component.Facing
import io.github.jlstrater.gamedemo.component.Move

class FacingSystem extends IteratingSystem {

    FacingSystem() {
        super(Family.all(Facing, Move).get())
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Move move = Move.MAPPER.get(entity)
        Vector2 moveDirection = move.direction
        if (moveDirection.isZero()) {
            return
        }

        Facing facing = Facing.MAPPER.get(entity)
        if (moveDirection.y > 0f) {
            facing.direction = Facing.FacingDirection.UP
        } else if (moveDirection.y < 0f) {
            facing.direction = Facing.FacingDirection.DOWN
        } else if (moveDirection.x > 0f) {
            facing.direction = Facing.FacingDirection.RIGHT
        } else if (moveDirection.x < 0f) {
            facing.direction = Facing.FacingDirection.LEFT
        }
    }
}
