package io.github.jlstrater.gamedemo.input

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.utils.GdxRuntimeException

class KeyboardController extends InputAdapter {
    Map<Integer, Command> KEY_MAPPING = [
        (Input.Keys.W): Command.UP,
        (Input.Keys.S): Command.DOWN,
        (Input.Keys.A): Command.LEFT,
        (Input.Keys.D): Command.RIGHT,
        (Input.Keys.SPACE): Command.SELECT,
        (Input.Keys.ESCAPE): Command.CANCEL
    ]
    final boolean[] commandState
    Map<Class<? extends ControllerState>, ControllerState> stateCache
    ControllerState activeState

    KeyboardController(Class<? extends ControllerState> initialState, Engine engine) {
        stateCache = [:]
        activeState = null
        commandState = new boolean[Command.values().length]

        stateCache.put(IdleControllerState, new IdleControllerState())
        stateCache.put(GameControllerState, new GameControllerState(engine))
        setActiveState(initialState)
    }

    void setActiveState(Class<? extends ControllerState> stateClass) {
        ControllerState controllerState = stateCache.get(stateClass)
        if (!controllerState) {
            throw new GdxRuntimeException("No state with class $stateClass found")
        }

        Command.values().each { Command command ->
            if(activeState && commandState[command.ordinal()]) {
                activeState.keyUp(command)
            }
            commandState[command.ordinal()] = false
        }

        activeState = controllerState
    }

    @Override
    boolean keyDown(int keycode) {
        Command command = KEY_MAPPING.get(keycode)
        if(!command) {
            return false
        }

        commandState[command.ordinal()] = true
        activeState.keyDown(command)
        return true
    }

    @Override
    boolean keyUp(int keycode) {
        Command command = KEY_MAPPING.get(keycode)
        if(!command) {
            return false
        }
        if (!commandState[command.ordinal()]) return false

        commandState[command.ordinal()] = false
        activeState.keyUp(command)
        return true
    }
}
