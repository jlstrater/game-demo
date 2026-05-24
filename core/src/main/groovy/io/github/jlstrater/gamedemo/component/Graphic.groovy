package io.github.jlstrater.gamedemo.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Graphic implements Component {
    public static final ComponentMapper<Graphic> MAPPER = ComponentMapper.getFor(Graphic)

    TextureRegion region
    Color color

    Graphic(Color color, TextureRegion region) {
        this.color = color
        this.region = region
    }

    void setRegion(TextureRegion region) {
        this.region = region
    }
}
