package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
 import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import com.mygdx.engine.*;
public class GameplayScene implements GameScene {
    private SceneManager sceneManager;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture playerTexture, bulletTexture, asteroidTexture, heartTexture;
    private OrthographicCamera camera;
    private Player player;
    private EntityManagement entityManagement;
    private float asteroidScale = 0.03f;
    private int numAsteroids = 10;
    private float scale = 0.3f;
    private float elapsedTime = 0;
    private static final float FALL_INTERVAL = 0.5f; // Adjust this interval as needed
    private int score = 0;
    private static final int pointsPerAsteroid = 100;

    public GameplayScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new Texture("player.png");
        bulletTexture = new Texture("bullet.png");
        asteroidTexture = new Texture("asteroid.png");
        heartTexture = new Texture("heart.png");

        font = new BitmapFont();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        player = new Player(50, 50, playerTexture, bulletTexture);
        player.setSceneManager(sceneManager);
        entityManagement = new EntityManagement(player);
        new InputOutputManagement(player, entityManagement);

        Gdx.input.setInputProcessor(new InputOutputManagement(player, entityManagement));

        for (int i = 0; i < numAsteroids; i++) {
            float randomX = MathUtils.random(0, Gdx.graphics.getWidth() - asteroidTexture.getWidth() * asteroidScale);
            float randomY = MathUtils.random(Gdx.graphics.getHeight(), Gdx.graphics.getHeight() * 2); // Spawn off-screen
            Asteroid asteroid = new Asteroid(randomX, randomY, 50, 50, asteroidTexture);
            entityManagement.addAsteroid(asteroid);
        }
    }
    @Override
    public void render(SpriteBatch batch) {
        float dt = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Draw player
        batch.draw(playerTexture, player.getX(), player.getY(), playerTexture.getWidth() * scale, playerTexture.getHeight() * scale);

        // Draw bullets
        for (Bullet bullet : entityManagement.getBullets()) {
            batch.draw(bulletTexture, bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
        }

        // Draw asteroids
        for (Asteroid asteroid : entityManagement.getAsteroids()) {
            batch.draw(asteroid.getAsteroidTexture(), asteroid.getX(), asteroid.getY(),
                    asteroidTexture.getWidth() * asteroidScale, asteroidTexture.getHeight() * asteroidScale);
        }

        float heartIconWidth = 20;
        float heartIconHeight = 20;
        for (int i = 0; i < player.getHealth(); i++) {
            batch.draw(heartTexture, 600 + i * (heartIconWidth + 5), Gdx.graphics.getHeight() - heartIconHeight + 50,
                    heartIconWidth, heartIconHeight); // Draw scaled heart icon
        }

        font.draw(batch, "Score: " + score, 600, 500);

        batch.end();

        // Update game logic
        update(dt);
    }

    @Override
    public void update(float dt) {
        // Update player
        player.update();

        // Update bullets
        for (Bullet bullet : entityManagement.getBullets()) {
            bullet.update();
        }

        // Update asteroids
        for (Asteroid asteroid : entityManagement.getAsteroids()) {
            if (asteroid.isDestroyed()) {
                score += pointsPerAsteroid;
            }
            asteroid.update();
        }

        // Check for collisions
        CollisionManagement collisionManagement = new CollisionManagement();
        collisionManagement.checkCollisions(entityManagement);
        // entityManagement.printEntities();

        // Spawn asteroids continuously for 15 seconds
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (elapsedTime >= FALL_INTERVAL) {
            spawnAsteroid();
            elapsedTime -= FALL_INTERVAL; // Reset the timer
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            entityManagement.addBullet(player.shoot());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneManager.pushScene(new PauseMenuScene(sceneManager));
        }
    }

    private void spawnAsteroid() {
        float randomX = MathUtils.random(0, Gdx.graphics.getWidth() - asteroidTexture.getWidth() * asteroidScale);
        float randomY = Gdx.graphics.getHeight(); // Spawn off-screen
        Asteroid asteroid = new Asteroid(randomX, randomY, 50, 50, asteroidTexture);
        entityManagement.addAsteroid(asteroid);
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        bulletTexture.dispose();
        asteroidTexture.dispose();
        sceneManager.dispose();
        heartTexture.dispose();
    }
}