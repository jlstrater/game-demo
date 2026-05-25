package io.github.jlstrater.gamedemo.tiled

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.TextureData
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FileTextureData
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import io.github.jlstrater.gamedemo.CampingGame
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.assets.AtlasAsset
import io.github.jlstrater.gamedemo.component.Graphic
import io.github.jlstrater.gamedemo.component.Transform

class TiledAshleyConfigurator {
    Engine engine
    AssetService assetService

    TiledAshleyConfigurator(Engine engine, AssetService assetService) {
        this.engine = engine
        this.assetService = assetService
    }

    void onLoadOjbect(TiledMapTileMapObject tiledMapTileMapObject) {
        Entity entity = this.engine.createEntity()
        TiledMapTile tile = tiledMapTileMapObject.tile
        TextureRegion textureRegion = getTextureRegion(tile)
        int z = tile.properties.get("z", 1, Integer)

        entity.add(new Graphic(Color.WHITE.cpy(), textureRegion))
        addEntityTransform(
            tiledMapTileMapObject.x, tiledMapTileMapObject.y, z,
            textureRegion.regionWidth, textureRegion.regionHeight,
            tiledMapTileMapObject.scaleX, tiledMapTileMapObject.scaleY,
            entity
        )

        engine.addEntity(entity)
    }

    void addEntityTransform(
        float x, float y, int z,
        float w, float h,
        float scaleX, float scaleY,
        Entity entity
    ) {
        Vector2 position = new Vector2(x, y)
        Vector2 size = new Vector2(w,h)
        Vector2 scaling = new Vector2(scaleX, scaleY)

        position.scl(CampingGame.UNIT_SCALE)
        size.scl(CampingGame.UNIT_SCALE)

        entity.add(new Transform(position, z, size, scaling, 0f))
    }

    private TextureRegion getTextureRegion(TiledMapTile tile) {
        String atlasAssetStr = tile.properties.get("atlasAsset", AtlasAsset.OBJECTS.name(), String)
        AtlasAsset atlasAsset = AtlasAsset.valueOf(atlasAssetStr)
        TextureAtlas textureAtlas = assetService.get(atlasAsset)
        FileTextureData textureData = tile.textureRegion.texture.textureData as FileTextureData
        String atlasKey = textureData.fileHandle.nameWithoutExtension()
        TextureAtlas.AtlasRegion region = textureAtlas.findRegion(atlasKey + "/" + atlasKey)

        return region ?: tile.getTextureRegion()
    }
}
