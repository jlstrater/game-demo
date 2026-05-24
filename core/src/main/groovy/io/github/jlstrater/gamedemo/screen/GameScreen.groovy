package io.github.jlstrater.gamedemo.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.jlstrater.gamedemo.CampingGame
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.assets.MapAsset
import io.github.jlstrater.gamedemo.system.RenderSystem

/** First screen of the application. Displayed after the application is created. */
class GameScreen extends ScreenAdapter {
    Batch batch
    CampingGame game
    AssetService assetService
    Viewport viewport
    OrthographicCamera camera
    Engine engine

    GameScreen(CampingGame game) {
        this.game = game
        assetService = game.assetService
        viewport = game.viewport
        camera = game.camera
        batch = game.batch
        engine = new Engine()

        engine.addSystem(new RenderSystem(batch, viewport, assetService))
    }

    void show() {
        this.assetService.load(MapAsset.MAIN)
        engine.getSystem(RenderSystem).setMap(assetService.get(MapAsset.MAIN))
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
