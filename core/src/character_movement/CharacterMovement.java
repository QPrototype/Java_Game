package character_movement;

import Screens.MainScreen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import Scenes.GameHud;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;



public class CharacterMovement extends Sprite {

    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;
    private TextureAtlas cuttingAtlas;

    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 32;


    private int currentFrame = 1;
    private String currentAtlasKey;
    private int currentX = 600;
    private int currentY = 0;

    private TiledMap map;
    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;

    private String type;


    public static int cutStart = -1;
    public static int cutEnd = -1;
    public TiledMapTile current;
    public TiledMapTileSet pine;
    public TiledMapTileLayer.Cell cut;
    public TiledMapTileSet grass_water;
    public TiledMapTile grass;



    public CharacterMovement(String type, TextureAtlas walkingAtlas, TextureAtlas lookingAtlas, TextureAtlas cuttingAtlas, TextureAtlas.AtlasRegion region,
                             TiledMap map) {
        super(new Sprite(region));

        this.walkingAtlas = walkingAtlas;
        this.lookingAtlas = lookingAtlas;
        this.cuttingAtlas = cuttingAtlas;
        this.map = map;
        this.setPosition(currentX, currentY);
        this.background = (TiledMapTileLayer) map.getLayers().get(0);
        this.foreground = (TiledMapTileLayer) map.getLayers().get(1);
        this.type = type;
        this.grass_water = map.getTileSets().getTileSet("grass_water");
        //this.grass = grass_water.getTile(1);
        //this.pine = map.getTileSets().getTileSet("pine");
        //System.out.println(pine.getProperties().containsKey("suur"));


        //this.sprite = sprite;
    }

    public int getCurrentX() {
        return this.currentX;
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public String checkCollision(String direction) {
        int x = currentX / TILE_WIDTH - currentY / TILE_HEIGHT + 1;
        int y = currentX / TILE_WIDTH + currentY / TILE_HEIGHT + 1;
        if (direction.equals("ne")) {
            //y += 2;
            y++;
        } else if (direction.equals("se")) {
            //x++;
            //x += 2;
        } else if (direction.equals("e")) {
            x++;
            y++;
        } else if (direction.equals("nw")) {
            //x -= 2;
            //x -= 3;
            x--;
        } else if (direction.equals("sw")) {
            y--;
        } else if (direction.equals("w")) {
           x--;
           y--;
        } else if (direction.equals("n")) {
            x--;
            y++;
        } else if (direction.equals("s")) {
            x++;
            y--;
        }
        if (type.equals("worker")) {
            TiledMapTileLayer.Cell terrain = background.getCell(x, y);
            if (terrain != null && terrain.getTile().
                    getProperties().containsKey("trunk")) {


                cut = foreground.getCell(x - 1, y - 3);

                if (cut.getTile() != null && cut.getTile().getProperties().containsKey("suur")) {
                    if (cutStart == -1) {
                        cutStart = GameHud.getTime();
                    }
                }
                if (cut.getTile() != null && cut.getTile().getProperties().containsKey("suur")) {
                    if (cutStart - 3 > GameHud.getTime()) {
                        current = cut.getTile();

                        cut.setTile(null);
                        cutStart = -1;
                        terrain.setTile(map.getTileSets().getTileSet("isometric_grass_and_water").getTile(2));

                        //Add wood
                        GameHud.addWood(20);
                    }
                }
                return "cut";
            } else {
                cutStart = -1;
            }
        }
        //respawn tree
        //if (cut != null) {
        //    if (cut.getTile() == null && cutEnd - 5 > GameHud.getTime()) {
        //        cut.setTile(current);
        //    }
        //}

        TiledMapTileLayer.Cell groundCell = background.getCell(x, y);
        if (groundCell != null && groundCell.getTile().
                getProperties().containsKey("Water")) {
            return "water";
        }
        return "move on.";
    }


    public void walk(float destinationX, float destinationY) {
        if (destinationX == -1 && destinationY == -1) {
            return;
        }
        currentAtlasKey = String.format("looking ne%04d", currentFrame);

        if (destinationX - currentX > 5) {
            if (destinationY > currentY) {
                    if (checkCollision("ne").equals("water")) {
                        return;
                    } else if (checkCollision("ne").equals("cut")) {
                        cutTree("ne");
                        return;
                    }
                    currentX += 3;
                    currentY += 3;
                    changeFrame("ne");
            } else if (currentY - destinationY > 5) {
                    if (checkCollision("se").equals("water")) {
                        return;
                    } else if (checkCollision("se").equals("cut")) {
                        cutTree("se");
                        return;
                    }

                currentX += 4;
                currentY -= 4;
                changeFrame("se");
            } else {
                if (checkCollision("e").equals("water")) {
                    return;
                } else if (checkCollision("e").equals("cut")) {
                        cutTree("e");
                        return;
                    }
                    currentX += 3;
                    this.setPosition(currentX, currentY);
                    changeFrame("e");
                }
        } else if (currentX - destinationX > 5) {
            if (destinationY - currentY > 5) {
                if (checkCollision("nw").equals("water")) {
                    return;
                } else if (checkCollision("nw").equals("cut")) {
                    cutTree("nw");
                    return;
                }
                currentX -= 4;
                currentY += 4;
                changeFrame("nw");
            } else if (currentY - destinationY > 5) {
                if (checkCollision("sw").equals("water")) {
                    return;
                }
                else if (checkCollision("sw").equals("cut")) {
                    cutTree("sw");
                    return;
                }
                currentX -= 3;
                currentY -= 3;
                changeFrame("sw");
            } else {
                if (checkCollision("w").equals("water")) {
                    return;
                } else if (checkCollision("w").equals("cut")) {
                    cutTree("w");
                    return;
                }
                currentX -= 4;
                changeFrame("w");
                }
        } else if (Math.abs(destinationX - currentX) <= 5) {
            if (destinationY - currentY > 1) {
                if (checkCollision("n").equals("water")) {
                    return;
                } else if (checkCollision("n").equals("cut")) {
                    cutTree("n");
                    return;
                }
                    currentY += 4;
                    changeFrame("n");
            } else if (currentY - destinationY > 5) {
                if (checkCollision("s").equals("water")) {
                    return;
                } else if (checkCollision("s").equals("cut")) {
                    cutTree("s");
                    return;
                }
                    currentY -= 4;
                    changeFrame("s");
            } else {
                currentAtlasKey = currentAtlasKey.replaceAll("walking", "looking");

                this.setRegion(lookingAtlas.findRegion(currentAtlasKey));
            }
        }
    }

    public void changeFrame(String direction) {
        this.setPosition(currentX, currentY);
        currentFrame++;
        if (currentFrame > 7) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("walking %s%04d", direction, currentFrame);
        this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
    }

    public void cutTree(String direction) {
        this.setPosition(currentX, currentY);
        currentFrame++;
        if (currentFrame > 12) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("felling tree %s%04d", direction, currentFrame);
        this.setRegion(cuttingAtlas.findRegion(currentAtlasKey));

    }
}

