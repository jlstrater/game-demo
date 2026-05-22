package io.github.jlstrater.gamedemo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
class Main extends ApplicationAdapter {
    SpriteBatch batch
    Texture splash
    Texture tent
    Texture character
    Texture background
    Texture tentBg
    BitmapFont font
    GlyphLayout layout

    final float closeBtnSize = 48f
    final float closeBtnPadding = 10f

    // overlay pixel used for fade
    Texture overlayPixel

    // States
    boolean showingSplash = true
    boolean fading = false
    boolean gameStarted = false
    boolean insideTent = false

    float fadeTime = 0f

    // Character position
    float charX, charY

    @Override
    void create() {
        batch = new SpriteBatch()
        splash = new Texture("strater-games.png")

        // game assets (created programmatically)
        tent = new Texture("tent.png")
        character = new Texture("camper.png")
        background = new Texture("background.png")
        tentBg = new Texture("tentBg.png")

        font = new BitmapFont()
        layout = new GlyphLayout()

        // 1x1 white pixel texture for overlay (we'll tint to black when drawing)
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(1f, 1f, 1f, 1f)
        pixmap.fill()
        overlayPixel = new Texture(pixmap)
        pixmap.dispose()

        // Continue when any key, mouse click, or touch is received
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            boolean keyDown(int keycode) {
                if (showingSplash && !fading && !gameStarted) {
                    // start fade immediately from splash
                    fading = true
                    fadeTime = 0f
                }
                return true
            }

            @Override
            boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float w = Gdx.graphics.getWidth()
                float h = Gdx.graphics.getHeight()
                // convert screenY to world Y (libGDX touch Y is top-left)
                float worldY = (float) (h - screenY)

                // Check close button (always present)
                float closeX = (float) (w - closeBtnPadding - closeBtnSize)
                float closeY = (float) (h - closeBtnPadding - closeBtnSize)
                if (screenX >= closeX && screenX <= closeX + closeBtnSize &&
                    worldY >= closeY && worldY <= closeY + closeBtnSize) {
                    Gdx.app.exit()
                    return true
                }

                if (showingSplash && !fading && !gameStarted) {
                    fading = true
                    fadeTime = 0f
                    return true
                }

                // If game is running, check for tent click to enter/exit
                if (gameStarted && !fading) {
                    float tentX = (float) ((w - tent.getWidth()) / 2f)
                    float tentY = (float) ((h - tent.getHeight()) / 4f)

                    if (screenX >= tentX && screenX <= tentX + tent.getWidth() &&
                        worldY >= tentY && worldY <= tentY + tent.getHeight()) {
                        insideTent = !insideTent
                        if (!insideTent) {
                            // exit tent: place character at tent entrance
                            charX = (float) (tentX + (tent.getWidth() - character.getWidth()) / 2f)
                            charY = (float) (tentY + (tent.getHeight() - character.getHeight()) / 2f)
                        }
                        return true
                    }
                }

                return true
            }

            @Override
            boolean touchUp(int screenX, int screenY, int pointer, int button) {
                // consume touchUp as well
                return true
            }
        })
    }

    @Override
    void render() {
        float delta = Gdx.graphics.getDeltaTime()

        // Update
        if (gameStarted && !insideTent) {
            // simple movement using arrow keys
            // pixels per second
            float moveSpeed = 120f
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) charX -= moveSpeed * delta
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) charX += moveSpeed * delta
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) charY += moveSpeed * delta
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) charY -= moveSpeed * delta
            // clamp to screen
            charX = Math.max(0, Math.min(Gdx.graphics.getWidth() - character.getWidth(), charX))
            charY = Math.max(0, Math.min(Gdx.graphics.getHeight() - character.getHeight(), charY))
        }

        // clear background
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)

        batch.begin()

        float w = Gdx.graphics.getWidth()
        float h = Gdx.graphics.getHeight()

        if (showingSplash && !fading && !gameStarted) {
            // draw splash centered in window
            batch.draw(splash, ((w - splash.getWidth()) / 2f).toFloat(), ((h - splash.getHeight()) / 2f).toFloat())

            // draw prompt centered near the bottom of the splash
            String prompt = "press any key to continue"
            layout.setText(font, prompt)
            float x = (w - layout.width) / 2f
            float y = 30 + layout.height // 30px from bottom
            font.draw(batch, layout, x, y)
        } else if (fading) {
            // while fading, keep showing splash under overlay
            batch.draw(splash, ((w - splash.getWidth()) / 2f).toFloat(), ((h - splash.getHeight()) / 2f).toFloat())

            fadeTime += delta
            // seconds
            float fadeDuration = 1.0f
            float alpha = (float) Math.min(1f, fadeTime / fadeDuration)
            batch.setColor(0f, 0f, 0f, alpha)
            batch.draw(overlayPixel, 0, 0, (int) w, (int) h)
            batch.setColor(1f, 1f, 1f, 1f)

            if (alpha >= 1f) {
                fading = false
                showingSplash = false
                // start game
                gameStarted = true
                // place tent centered
                float tentX = (float) ((w - tent.getWidth()) / 2f)
                float tentY = (h - tent.getHeight()) / 4f
                // place character inside tent center
                charX = (float) (tentX + (tent.getWidth() - character.getWidth()) / 2f)
                charY = (float) (tentY + (tent.getHeight() - character.getHeight()) / 4f)
            }
        }

        if (gameStarted) {
            if (!insideTent) {
                batch.draw(background, 0, 0)

                // draw tent centered
                float tentX = (float) ((w - tent.getWidth()) / 2f)
                float tentY = (float) ((h - tent.getHeight()) / 4f)
                batch.draw(tent, tentX, tentY)
            } else {
                batch.draw(tentBg, 0, 0)
            }


            // draw character (unless inside tent)
            if (!insideTent) {
                batch.draw(character, charX, charY)
            }

            // draw a small HUD/title
            String title = insideTent ? "Inside the tent. Click tent to exit." : "You live in this tent. Use arrows to move. Click tent to enter."
            layout.setText(font, title)
            font.draw(batch, layout, 10f, (h - 10f).toFloat())
        }

        // draw close button (always present)
        float closeX = (float) (w - closeBtnPadding - closeBtnSize)
        float closeY = (float) (h - closeBtnPadding - closeBtnSize)
        // draw button background
        batch.setColor(0.2f, 0.2f, 0.2f, 0.9f)
        batch.draw(overlayPixel, closeX, closeY, closeBtnSize, closeBtnSize)
        batch.setColor(1f, 1f, 1f, 1f)
        // draw X centered
        layout.setText(font, "X")
        float xCenter = (float) (closeX + (closeBtnSize - layout.width) / 2f)
        float yCenter = (float) (closeY + (closeBtnSize + layout.height) / 2f)
        font.draw(batch, layout, xCenter, yCenter)

        batch.end()
    }

    @Override
    void dispose() {
        batch?.dispose()
        if (splash != null) splash.dispose()
        if (tent != null) tent.dispose()
        if (character != null) character.dispose()
        if (background != null) background.dispose()
        if (font != null) font.dispose()
        if (overlayPixel != null) overlayPixel.dispose()
    }
}
