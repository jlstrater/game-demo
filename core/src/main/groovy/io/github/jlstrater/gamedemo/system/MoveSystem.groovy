package io.github.jlstrater.gamedemo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import io.github.jlstrater.gamedemo.component.Move
import io.github.jlstrater.gamedemo.component.Transform

class MoveSystem extends IteratingSystem {
    MoveSystem() {
        super(Family.all(Move, Transform).get())
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Move move = Move.MAPPER.get(entity)
        if (move.isRooted() || move.direction.isZero()) {
            return
        }

        Transform transform = Transform.MAPPER.get(entity)
        Vector2 position = transform.position
        position.set(
            (position.x + move.maxSpeed * move.direction.x * deltaTime).toFloat(),
            (position.y + move.maxSpeed * move.direction.y * deltaTime).toFloat()
        )

    }
}
