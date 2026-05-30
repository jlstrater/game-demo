package io.github.jlstrater.gamedemo.ai

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import io.github.jlstrater.gamedemo.component.Animation2D
import io.github.jlstrater.gamedemo.component.Fsm
import io.github.jlstrater.gamedemo.component.Move

enum AnimationState implements State<Entity>{
    IDLE {
        @Override
        void enter(Entity entity) {
            Animation2D.MAPPER.get(entity).type = Animation2D.AnimationType.IDLE
        }

        @Override
        void update(Entity entity) {
            Move move = Move.MAPPER.get(entity)
            if (move && !move.isRooted() && !move.direction.isZero()) {
                Fsm.MAPPER.get(entity).animationFsm.changeState(WALK)
            }
        }

        @Override
        void exit(Entity entity) {

        }

        @Override
        boolean onMessage(Entity entity, Telegram telegram) {
            return false
        }
    },
    WALK {
        @Override
        void enter(Entity entity) {
            Animation2D.MAPPER.get(entity).type = Animation2D.AnimationType.WALK
        }

        @Override
        void update(Entity entity) {
            Move move = Move.MAPPER.get(entity)
            if (!move || move.direction.isZero() || move.isRooted()) {
                Fsm.MAPPER.get(entity).animationFsm.changeState(IDLE)
            }
        }

        @Override
        void exit(Entity entity) {

        }

        @Override
        boolean onMessage(Entity entity, Telegram telegram) {
            return false
        }
    }

    abstract void enter(Entity entity)

    abstract void update(Entity entity)

    abstract void exit(Entity entity)

    abstract boolean onMessage(Entity entity, Telegram telegram)

}
