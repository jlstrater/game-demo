package io.github.jlstrater.gamedemo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.jlstrater.gamedemo.CampingGame
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.component.Graphic
import io.github.jlstrater.gamedemo.component.Transform

import java.awt.Graphics

class RenderSystem extends SortedIteratingSystem implements Disposable {

    OrthogonalTiledMapRenderer mapRenderer
    Batch batch
    Viewport viewport
    OrthographicCamera camera

    RenderSystem(Batch batch, Viewport viewport, AssetService assetService) {
        super(
            Family.all(Transform, Graphic).get(),
            Comparator.comparing(Transform.MAPPER::get)
        )
        this.batch = batch
        this.viewport = viewport
        this.camera = viewport.camera as OrthographicCamera
        mapRenderer = new OrthogonalTiledMapRenderer(null, CampingGame.UNIT_SCALE, batch)
    }

    @Override
    void update(float deltaTime) {
        viewport.apply()
        batch.color = Color.WHITE
        mapRenderer.setView(camera)
        mapRenderer.render()

        forceSort()
        super.update(deltaTime)
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Transform transform = Transform.MAPPER.get(entity)
        Graphic graphic = Graphic.MAPPER.get(entity)
        if (!graphic.region) {
            return
        }

        Vector2 position = transform.position
        Vector2 scaling = transform.scaling
        Vector2 size = transform.size
        batch.color = graphic.color
        batch.draw(
            graphic.region,
            (position.x - (1f - scaling.x) * size.x * 0.5f).toFloat(),
            (position.y - (1f - scaling.y) * size.y * 0.5f).toFloat(),
            (size.x * 0.5f).toFloat(), (size.y * 0.5f).toFloat(),
            size.x, size.y,
            scaling.x, scaling.y,
            transform.rotationDeg
        )
    }

    void setMap(TiledMap map) {
        this.mapRenderer.map = map
    }

    @Override
    void dispose() {
        mapRenderer.dispose()
    }
}
