package io.github.jlstrater.gamedemo.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper

class Facing implements Component {
    public static final ComponentMapper<Facing> MAPPER = ComponentMapper.getFor(Facing)

    FacingDirection direction

    Facing(FacingDirection facingDirection) {
        this.direction = facingDirection
    }

    enum FacingDirection {
        UP, DOWN, LEFT, RIGHT

        String atlasKey

        FacingDirection() {
            this.atlasKey = name().toLowerCase()
        }


    }
}
