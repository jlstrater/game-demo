package io.github.jlstrater.gamedemo.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.utils.Disposable

class AssetService implements Disposable {
    private final AssetManager assetManager

    AssetService(FileHandleResolver fileHandleResolver) {
        assetManager = new AssetManager(fileHandleResolver)
        assetManager.setLoader(TiledMap.class, new TmxMapLoader())
    }

    <T> T load(Asset<T> asset) {
        assetManager.load(asset.descriptor)
        assetManager.finishLoading()

        return assetManager.get(asset.descriptor)
    }

    <T> void queue(Asset<T> asset) {
        assetManager.load(asset.descriptor)
    }

    <T> T get(Asset<T> asset) {
        assetManager.get(asset.descriptor)
    }

    boolean update() {
        assetManager.update()
    }

    void debugDiagnostics() {
        Gdx.app.debug("AssetService", assetManager.diagnostics)
    }

    @Override
    void dispose() {
        this.assetManager.dispose()
    }
}
