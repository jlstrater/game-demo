package io.github.jlstrater.gamedemo.input

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import io.github.jlstrater.gamedemo.component.Controller

class GameControllerState implements ControllerState {

    ImmutableArray<Entity> controllerEntities

    GameControllerState(Engine engine) {
        controllerEntities = engine.getEntitiesFor(Family.all(Controller).get())
    }

    @Override
    void keyDown(Command command) {
        controllerEntities.each { entity ->
            Controller.MAPPER.get(entity).getPressedCommands().add(command)
        }
    }

    @Override
    void keyUp(Command command) {
        controllerEntities.each { entity ->
            Controller.MAPPER.get(entity).getReleasedCommands().add(command)
        }

    }
}
