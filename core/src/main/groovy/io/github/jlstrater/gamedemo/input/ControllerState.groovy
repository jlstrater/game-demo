package io.github.jlstrater.gamedemo.input

interface ControllerState {
    void keyDown(Command command)

    default void keyUp(Command command) {}
}
