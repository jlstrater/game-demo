package io.github.jlstrater.gamedemo

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.FPSLogger
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.utils.GdxRuntimeException
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.github.jlstrater.gamedemo.assets.AssetService
import io.github.jlstrater.gamedemo.screen.GameScreen

class CampingGame extends Game {
    public static final float WORLD_WIDTH = 16f
    public static final float WORLD_HEIGHT = 9f
    public static final UNIT_SCALE = (1f/128f).toFloat()

    Batch batch
    OrthographicCamera camera
    Viewport viewport
    AssetService assetService
    GLProfiler glProfiler
    FPSLogger fpsLogger

    private final Map<Class<? extends Screen>, Screen> screenCache = new HashMap<>()

    @Override
    void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG)

        batch = new SpriteBatch()
        assetService = new AssetService(new InternalFileHandleResolver())
        camera = new OrthographicCamera()
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)
        glProfiler = new GLProfiler(Gdx.graphics)
        glProfiler.enable()
        fpsLogger = new FPSLogger()

        addScreen(new GameScreen(this))
        setScreen(GameScreen.class)
    }

    @Override
    void resize(int width, int height) {
        viewport.update(width, height, true)
        super.resize(width, height)
    }

    void addScreen(Screen screen) {
        screenCache.put(screen.class, screen)
    }

    void setScreen(Class<? extends Screen> screenClass) {
        Screen screen = screenCache.get(screenClass)
        screen ? super.setScreen(screen) :  { throw new GdxRuntimeException("No screen with class "  + screenClass + " found in the screenCache")}
    }

    @Override
    void render() {
        glProfiler.reset()

        Gdx.gl.glClearColor(0f,0f,0f,1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        fpsLogger.log()

        super.render()
    }

    @Override
    void dispose() {
        screenCache.values().forEach(Screen::dispose)
        assetService.debugDiagnostics()
        assetService.dispose()
        batch.dispose()
    }
}
