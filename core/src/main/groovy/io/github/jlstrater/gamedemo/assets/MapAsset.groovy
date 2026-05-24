package io.github.jlstrater.gamedemo.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.maps.tiled.BaseTiledMapLoader
import com.badlogic.gdx.maps.tiled.BaseTmxMapLoader
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

enum MapAsset implements Asset<TiledMap>{
    MAIN('campsite.tmx')

    private final AssetDescriptor<TiledMap> descriptor

    MapAsset(String mapName) {
        BaseTiledMapLoader.Parameters parameters = new BaseTiledMapLoader.Parameters()
        parameters.projectFilePath = 'maps/campsite.tiled-project'
        descriptor = new AssetDescriptor<>('maps/' + mapName, TiledMap, parameters)
    }

    @Override
    AssetDescriptor<TiledMap> getDescriptor() {
        return descriptor
    }
}
