package io.github.jlstrater.gamedemo.tiled

import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.utils.GdxRuntimeException
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.assets.MapAsset

import java.util.function.Consumer

class TiledService {

    AssetService assetService

    TiledMap currentMap

    Consumer<TiledMap> mapChangeConsumer
    Consumer<TiledMapTileMapObject> loadObjectConsumer

    TiledService(AssetService assetService) {
        this.assetService = assetService
        mapChangeConsumer = null
        loadObjectConsumer = null
        currentMap = null
    }

    TiledMap loadMap(MapAsset mapAsset) {
        TiledMap tiledMap = assetService.load(mapAsset)
        tiledMap.properties.put("mapAsset", mapAsset)

        return tiledMap
    }

    void setMap(TiledMap map) {
        if (this.currentMap) {
            this.assetService.unload(this.currentMap.properties.get("mapAsset", MapAsset))
        }

        this.currentMap = map
        loadMapObjects(map)
        if (mapChangeConsumer) {
            mapChangeConsumer.accept(map)
        }
    }

    private void loadMapObjects(TiledMap tiledMap) {
        tiledMap.layers.forEach { layer ->
            if ("objects" == layer.name) {
                loadObjectLayer(layer)
            }
        }
    }

    private void loadObjectLayer(MapLayer objectLayer) {
        if (!loadObjectConsumer) {
            return
        }

        objectLayer.objects.forEach { mapObject ->
            if (mapObject instanceof TiledMapTileMapObject) {
                loadObjectConsumer.accept(mapObject)
            } else {
                throw new GdxRuntimeException("Unsupported object: " + mapObject.class)
            }
        }
    }
}
