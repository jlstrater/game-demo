package io.github.jlstrater.gamedemo.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import io.github.jlstrater.gamedemo.input.Command

class Controller implements Component {
    public static final ComponentMapper<Controller> MAPPER = ComponentMapper.getFor(Controller)

    List<Command> pressedCommands
    List<Command> releasedCommands

    Controller() {
        pressedCommands = []
        releasedCommands = []
    }
}
