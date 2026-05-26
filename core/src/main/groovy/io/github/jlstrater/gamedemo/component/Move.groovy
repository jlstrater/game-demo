package io.github.jlstrater.gamedemo.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector2

class Move implements Component {
    public static final ComponentMapper<Move> MAPPER = ComponentMapper.getFor(Move.class)

    float maxSpeed
    Vector2 direction
    boolean isRooted

    Move(float maxSpeed) {
        this.maxSpeed = maxSpeed
        this.direction = new Vector2()
    }

    boolean isRooted() {
        isRooted
    }
}
