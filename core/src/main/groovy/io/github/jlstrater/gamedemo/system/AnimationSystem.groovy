package io.github.jlstrater.gamedemo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.GdxRuntimeException
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.assets.AtlasAsset
import io.github.jlstrater.gamedemo.component.Animation2D
import io.github.jlstrater.gamedemo.component.Animation2D.AnimationType
import io.github.jlstrater.gamedemo.component.Facing
import io.github.jlstrater.gamedemo.component.Facing.FacingDirection
import io.github.jlstrater.gamedemo.component.Graphic

class AnimationSystem extends IteratingSystem {
    static final float FRAME_DURATION = (1/8).toFloat()
    AssetService assetService
    Map<CacheKey, Animation<TextureRegion>> animationCache = [:]

    AnimationSystem(AssetService assetService) {
        super(Family.all(Animation2D, Graphic, Facing).get())
        this.assetService = assetService
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Animation2D animation2D = Animation2D.MAPPER.get(entity)
        FacingDirection direction = Facing.MAPPER.get(entity).direction
        final float stateTime

        if (animation2D.isDirty() || direction != animation2D.facingDirection) {
            updateAnimation(animation2D, direction)
            stateTime = 0f
        } else {
            stateTime = animation2D.incAndGetStateTime(deltaTime)
        }

        Animation<TextureRegion> animation = animation2D.animation
        animation.setPlayMode(animation2D.playMode)

        TextureRegion frame = animation.getKeyFrame(stateTime)
        Graphic.MAPPER.get(entity).setRegion(frame)
    }

    void updateAnimation(Animation2D animation2D, FacingDirection facingDirection) {
        AtlasAsset atlasAsset = animation2D.atlasAsset
        String atlasKey = animation2D.atlasKey
        AnimationType type = animation2D.type
        CacheKey key = new CacheKey(atlasAsset, atlasKey, type, facingDirection)
        Animation<TextureRegion> animation = animationCache.computeIfAbsent(key, { cacheKey ->
            TextureAtlas textureAtlas = assetService.get(atlasAsset)
            String combinedKey = atlasKey + "/" + type.atlasKey + "_" + facingDirection.atlasKey
            Array<TextureAtlas.AtlasRegion> regions = textureAtlas.findRegions(combinedKey)
            if (regions.isEmpty()) {
                throw new GdxRuntimeException("No regions found for key: $combinedKey")
            }
            return new Animation<>(FRAME_DURATION, regions)
        })
        animation2D.setAnimation(animation, facingDirection)
    }

    record CacheKey(
        AtlasAsset atlasAsset,
        String atlasKey,
        AnimationType type,
        FacingDirection direction
    ){
        CacheKey(AtlasAsset atlasAsset, String atlasKey, AnimationType type, FacingDirection direction) {
            this.atlasAsset = atlasAsset
            this.atlasKey = atlasKey
            this.type = type
            this.direction = direction
        }
    }
}
