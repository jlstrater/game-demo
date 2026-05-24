package io.github.jlstrater.gamedemo.assets

import com.badlogic.gdx.assets.AssetDescriptor

interface Asset<T> {
    AssetDescriptor<T> getDescriptor()

}
