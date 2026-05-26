package io.github.jlstrater.gamedemo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import io.github.jlstrater.gamedemo.CampingGame
import io.github.jlstrater.gamedemo.component.Controller
import io.github.jlstrater.gamedemo.component.Move
import io.github.jlstrater.gamedemo.input.Command

class ControllerSystem extends IteratingSystem {
    CampingGame game

    ControllerSystem(CampingGame game) {
        super(Family.all(Controller).get())
        this.game = game
    }

    protected void processEntity(Entity entity, float deltaTime) {
        Controller controller = Controller.MAPPER.get(entity)

        if (controller.pressedCommands.isEmpty() && controller.releasedCommands.isEmpty()) {
            return
        }

        controller.pressedCommands.each { Command pressedCommand ->
            switch (pressedCommand) {
                case Command.UP -> moveEntity(entity, 0f, 1f)
                case Command.DOWN -> moveEntity(entity, 0f, -1f)
                case Command.LEFT -> moveEntity(entity, -1f, 0f)
                case Command.RIGHT -> moveEntity(entity, 1f, 0f)
            }
        }

        controller.pressedCommands.clear()

        controller.releasedCommands.each { Command releasedCommand ->
            switch (releasedCommand) {
                case Command.UP -> moveEntity(entity, 0f, -1f)
                case Command.DOWN -> moveEntity(entity, 0f, 1f)
                case Command.LEFT -> moveEntity(entity, 1f, 0f)
                case Command.RIGHT -> moveEntity(entity, -1f, 0f)
            }
        }

        controller.releasedCommands.clear()
    }

    private void moveEntity(Entity entity, float directionX, float directionY) {
        Move move = Move.MAPPER.get(entity)
        if (move) {
            move.direction.x += directionX
            move.direction.y += directionY
        }
    }
}
