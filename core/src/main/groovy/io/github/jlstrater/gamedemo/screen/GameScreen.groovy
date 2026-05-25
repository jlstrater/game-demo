package io.github.jlstrater.gamedemo.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.jlstrater.gamedemo.CampingGame
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.assets.MapAsset
import io.github.jlstrater.gamedemo.system.RenderSystem
import io.github.jlstrater.gamedemo.tiled.TiledAshleyConfigurator
import io.github.jlstrater.gamedemo.tiled.TiledService

import java.util.function.Consumer

/** First screen of the application. Displayed after the application is created. */
class GameScreen extends ScreenAdapter {
    Batch batch
    CampingGame game
    AssetService assetService
    Viewport viewport
    OrthographicCamera camera
    Engine engine
    TiledService tiledService
    TiledAshleyConfigurator tiledAshleyConfigurator

    GameScreen(CampingGame game) {
        this.game = game
        assetService = game.assetService
        viewport = game.viewport
        camera = game.camera
        batch = game.batch
        tiledService = new TiledService(assetService)
        engine = new Engine()
        tiledAshleyConfigurator = new TiledAshleyConfigurator(engine, assetService)
        engine.addSystem(new RenderSystem(batch, viewport, camera))
    }

    void show() {
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
