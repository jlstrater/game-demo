package io.github.jlstrater.gamedemo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import io.github.jlstrater.gamedemo.CampingGame
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.assets.AtlasAsset

class LoadingScreen extends ScreenAdapter {
    CampingGame game
    AssetService assetService

    LoadingScreen(CampingGame game, AssetService assetService) {
        this.game = game
        this.assetService = assetService
    }

    @Override
    void show() {
        AtlasAsset.values().each {atlas ->
            assetService.queue(atlas)
        }
    }

    @Override
    void render(float delta) {
        if (assetService.update()) {
            Gdx.app.debug("Loading Screen", "Finished Asset Loading")
            createScreens()
            game.removeScreen(this)
            dispose()
            game.screen = GameScreen
        }
    }

    private void createScreens() {
        game.addScreen(new GameScreen(this.game))
    }

}
