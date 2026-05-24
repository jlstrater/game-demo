package io.github.jlstrater.gamedemo.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector2

class Transform implements Component, Comparable<Transform> {
    public static final ComponentMapper<Transform> MAPPER = ComponentMapper.getFor(Transform)

    Vector2 position
    int z
    Vector2 size
    Vector2 scaling
    float rotationDeg

    Transform(Vector2 position, int z, Vector2 size, Vector2 scaling, float rotationDeg) {
        this.position = position
        this.z = z
        this.size = size
        this.scaling = scaling
        this.rotationDeg = rotationDeg
    }

    int compareTo(Transform other) {
        if (this.z !== other.z ) {
            return Float.compare(this.z, other.z)
        }
        if (this.position.y !== other.position.y) {
            return Float.compare(other.position.y, this.position.y)
        }
        return Float.compare(this.position.x, other.position.x)
    }
}
