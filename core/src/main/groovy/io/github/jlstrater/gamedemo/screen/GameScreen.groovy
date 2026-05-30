package io.github.jlstrater.gamedemo.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.Disposable
import io.github.jlstrater.gamedemo.CampingGame
import io.github.jlstrater.gamedemo.assets.MapAsset
import io.github.jlstrater.gamedemo.input.GameControllerState
import io.github.jlstrater.gamedemo.input.KeyboardController
import io.github.jlstrater.gamedemo.system.AnimationSystem
import io.github.jlstrater.gamedemo.system.ControllerSystem
import io.github.jlstrater.gamedemo.system.FacingSystem
import io.github.jlstrater.gamedemo.system.FsmSystem
import io.github.jlstrater.gamedemo.system.MoveSystem
import io.github.jlstrater.gamedemo.system.RenderSystem
import io.github.jlstrater.gamedemo.tiled.TiledAshleyConfigurator
import io.github.jlstrater.gamedemo.tiled.TiledService

import java.util.function.Consumer

/** First screen of the application. Displayed after the application is created. */
class GameScreen extends ScreenAdapter {
    Engine engine
    TiledService tiledService
    TiledAshleyConfigurator tiledAshleyConfigurator
    KeyboardController keyboardController
    CampingGame game

    GameScreen(CampingGame game) {
        this.game = game
        tiledService = new TiledService(game.assetService)
        engine = new Engine()
        tiledAshleyConfigurator = new TiledAshleyConfigurator(engine, game.assetService)
        keyboardController = new KeyboardController(GameControllerState, engine)

        engine.addSystem(new ControllerSystem(game))
        engine.addSystem(new MoveSystem())
        engine.addSystem(new FsmSystem())
        engine.addSystem(new FacingSystem())
        engine.addSystem(new AnimationSystem(game.assetService))
        engine.addSystem(new RenderSystem(game.batch, game.viewport, game.camera))
    }

    void show() {
        game.setInputProcessors(keyboardController)
        keyboardController.activeState = GameControllerState
        Consumer<TiledMap> renderConsumer = engine.getSystem(RenderSystem)::setMap
        tiledService.setMapChangeConsumer(renderConsumer)
        tiledService.setLoadObjectConsumer(tiledAshleyConfigurator::onLoadOjbect)

        tiledService.map = tiledService.loadMap(MapAsset.MAIN)
    }

    void hide() {
        engine.removeAllEntities()
    }

    void render(float delta) {
        delta = Math.min(delta, (1/30f).toFloat())
        engine.update(delta)
    }

    void dispose() {
        engine.systems.forEach {
            if (it instanceof Disposable) {
                it.dispose()
            }
        }
    }
}
