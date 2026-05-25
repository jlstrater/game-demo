package io.github.jlstrater.gamedemo.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas

enum AtlasAsset implements Asset<TextureAtlas>{
    OBJECTS("objects.atlas")

    private final AssetDescriptor<TextureAtlas> descriptor

    AtlasAsset(String atlasName) {
        descriptor = new AssetDescriptor<>("graphics/"+atlasName, TextureAtlas)
    }

    @Override
    AssetDescriptor<TextureAtlas> getDescriptor() {
        return this.descriptor
    }
}
