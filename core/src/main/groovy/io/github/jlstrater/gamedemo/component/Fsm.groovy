package io.github.jlstrater.gamedemo.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import io.github.jlstrater.gamedemo.ai.AnimationState

class Fsm implements Component {
    public static final ComponentMapper<Fsm> MAPPER = ComponentMapper.getFor(Fsm)

    DefaultStateMachine<Entity, AnimationState> animationFsm

    Fsm(Entity owner) {
        animationFsm = new DefaultStateMachine<Entity, AnimationState>(owner, AnimationState.IDLE)
    }
}
