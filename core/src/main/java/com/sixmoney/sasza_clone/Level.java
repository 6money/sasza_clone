package com.sixmoney.sasza_clone;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.World;
import com.sixmoney.sasza_clone.entities.Crate;
import com.sixmoney.sasza_clone.entities.Entity;
import com.sixmoney.sasza_clone.entities.FloorTile;
import com.sixmoney.sasza_clone.entities.Player;
import com.sixmoney.sasza_clone.utils.ChaseCam;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Level {
    private World<Entity> world;
    private Viewport viewport;
    private ChaseCam camera;
    private Player player;
    private Crate crate;

    private Array<FloorTile> grassTiles;
    private Array<FloorTile> dirtTiles;
    private Array<FloorTile> sandTiles;
    private Array<FloorTile> waterTiles;

    public Level(Viewport viewport) {
        this.viewport = viewport;

        world = new World<>();
        world.setTileMode(false);
        player = new Player();
        world.add(player.item, player.bbox.x, player.bbox.y, player.bbox.width, player.bbox.height);
        crate = new Crate();
        world.add(crate.item, crate.bbox.x, crate.bbox.y, crate.bbox.width, crate.bbox.height);

        camera = new ChaseCam(player);
        viewport.setCamera(camera);

        grassTiles = new Array<>();
        dirtTiles = new Array<>();
        sandTiles = new Array<>();
        waterTiles = new Array<>();
    }

    public Player getPlayer() {
        return player;
    }

    public void setGrassTiles(Array<FloorTile> grassTiles) {
        this.grassTiles = grassTiles;
    }

    public void setDirtTiles(Array<FloorTile> dirtTiles) {
        this.dirtTiles = dirtTiles;
    }

    public void setSandTiles(Array<FloorTile> sandTiles) {
        this.sandTiles = sandTiles;
    }

    public void setWaterTiles(Array<FloorTile> waterTiles) {
        this.waterTiles = waterTiles;
        for (FloorTile waterTile: waterTiles) {
            waterTile.bbox.x += 55;
            waterTile.bbox.y += 55;
            waterTile.bbox.width -= 110;
            waterTile.bbox.height -= 110;
            world.add(waterTile.item, waterTile.bbox.x, waterTile.bbox.y, waterTile.bbox.width, waterTile.bbox.height);
        }
    }

    public void update(float delta) {
        player.update(delta, world);
    }

    public void render(Batch batch) {
        for (FloorTile tile: grassTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: dirtTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: sandTiles) {
            tile.render(batch);
        }
        for (FloorTile tile: waterTiles) {
            tile.render(batch);
        }
        crate.render(batch);
        player.render(batch);
    }

    public void renderDebug(ShapeDrawer drawer) {
        crate.renderDebug(drawer);
        player.renderDebug(drawer);
        for (FloorTile tile: waterTiles) {
            tile.renderDebug(drawer);
        }
    }
}
