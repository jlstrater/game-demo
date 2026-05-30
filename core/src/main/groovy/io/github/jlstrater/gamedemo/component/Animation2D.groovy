package io.github.jlstrater.gamedemo.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import io.github.jlstrater.gamedemo.assets.AtlasAsset
import io.github.jlstrater.gamedemo.component.Facing.FacingDirection

class Animation2D implements Component {
    public static final ComponentMapper<Animation2D> MAPPER = ComponentMapper.getFor(Animation2D)

    final AtlasAsset atlasAsset
    final String atlasKey
    AnimationType type
    Facing.FacingDirection facingDirection
    Animation.PlayMode playMode
    float speed = 1
    float stateTime
    Animation<TextureRegion> animation
    boolean dirty

    Animation2D(
        AtlasAsset atlasAsset,
        String atlasKey,
        AnimationType type,
        Animation.PlayMode playMode,
        float speed
    ) {
        this.atlasAsset = atlasAsset
        this.atlasKey = atlasKey
        this.type = type
        this.facingDirection = null
        this.playMode = playMode
        this.speed = speed
        stateTime = 0f
        animation = null
        dirty = true
    }

    void setAnimation(Animation<TextureRegion> animation, FacingDirection facingDirection) {
        this.animation = animation
        this.facingDirection = facingDirection
        stateTime = 0f
        dirty = false
    }

    void setType(AnimationType type) {
        this.type = type
        dirty = true
    }

    boolean isDirty() {
        return dirty
    }

    boolean isFinished() {
        animation.isAnimationFinished(stateTime)
    }

    float incAndGetStateTime(float deltaTime) {
        stateTime += deltaTime * speed
        return stateTime
    }

    enum AnimationType {
        IDLE, WALK, ATTACK, DAMAGED

        String atlasKey

        AnimationType(){
            this.atlasKey = name().toLowerCase()
        }
    }
}
