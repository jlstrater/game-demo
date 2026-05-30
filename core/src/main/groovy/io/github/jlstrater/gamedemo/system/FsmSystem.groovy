package io.github.jlstrater.gamedemo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import io.github.jlstrater.gamedemo.component.Fsm

class FsmSystem extends IteratingSystem {
    FsmSystem() {
        super(Family.all(Fsm).get())
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Fsm.MAPPER.get(entity).animationFsm.update()
    }
}
