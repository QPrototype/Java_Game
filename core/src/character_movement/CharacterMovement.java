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
    private TextureAtlas miningAtlas;

    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 32;


    private int currentFrame = 1;
    private String currentAtlasKey;
    private int currentX;
    private int currentY;
    public float destinationX = -1;
    public float destinationY = -1;

    private TiledMap map;
    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;

    private String type;


    public static int cutStart = -1;
    public static int mineStart = -1;
    public TiledMapTile current;
    public TiledMapTileSet pine;
    public TiledMapTileLayer.Cell cut;
    public TiledMapTileLayer.Cell mine;

    private boolean selected = false;



    public CharacterMovement(String type, TextureAtlas walkingAtlas, TextureAtlas lookingAtlas,
                             TextureAtlas cuttingAtlas, TextureAtlas miningAtlas,  TextureAtlas.AtlasRegion region,
                             TiledMap map) {
        super(new Sprite(region));

        this.walkingAtlas = walkingAtlas;
        this.lookingAtlas = lookingAtlas;
        this.cuttingAtlas = cuttingAtlas;
        this.miningAtlas = miningAtlas;
        this.map = map;
        //this.setPosition(currentX, currentY);
        this.background = (TiledMapTileLayer) map.getLayers().get(0);
        this.foreground = (TiledMapTileLayer) map.getLayers().get(1);
        this.type = type;
        //MainScreen.addCharacter(this);
    }

    public int getCurrentX() {
        return this.currentX;
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public String checkCollision(String direction) {
        int currPosX = currentX / TILE_WIDTH - currentY / TILE_HEIGHT + 1;
        int currPosY = currentX / TILE_WIDTH + currentY / TILE_HEIGHT + 1;
        int x = (int) (destinationX / TILE_WIDTH - destinationY / TILE_HEIGHT + 1);
        int y = (int) (destinationX / TILE_WIDTH + destinationY / TILE_HEIGHT + 1);
        //if (currPosX == x && currPosY == y) {
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
            // Checking if unit is a worker and close enough to destination
            if (type.equals("worker") && Math.abs(currPosX - x) < 3 && Math.abs(currPosY - y) < 3) {
                // Looping through nearby tiles looking for tree
                for (int i = x - 1; i < x + 1; i++) {
                    for (int j = y - 1; j < y + 3; j++) {
                        cut = foreground.getCell(i - 1, j - 3);
                        mine = foreground.getCell(i, j - 2);

                        if (cut != null && cut.getTile() != null && cut.getTile().getProperties().containsKey("suur")) {
                            if (cutStart == -1) {
                                cutStart = GameHud.getTime();
                            }
                            if (cutStart - 3 > GameHud.getTime()) {
                                //current = cut.getTile();

                                cut.setTile(null);
                                cutStart = -1;

                                GameHud.addWood(20);
                            }

                            return "cut";
                        } else if (mine != null && mine.getTile() != null
                                && mine.getTile().getProperties().containsKey("iron")) {
                            if (mineStart == -1) {
                                mineStart = GameHud.getTime();
                            }
                            if (mineStart - 3 > GameHud.getTime()) {
                                //current = mine.getTile();

                                mine.setTile(null);
                                mineStart = -1;

                                //GameHud.addIron(20);
                            }
                            return "mine";
                        }
                    }
                }
                    // When worker assignment is cancelled
                    cutStart = -1;
                    mineStart = -1;
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


    public void walk() {
        if (destinationX == -1 || destinationY == -1) {
            return;
        }
        //if (selected) {
            currentAtlasKey = String.format("looking ne%04d", currentFrame);

            if (destinationX - currentX > 5) {
                if (destinationY > currentY) {
                    if (checkCollision("ne").equals("water")) {
                        return;
                    } else if (checkCollision("ne").equals("cut")) {
                        cutTree("ne");
                        return;
                    } else if (checkCollision("ne").equals("mine")) {
                        mine("ne");
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
                    }  else if (checkCollision("se").equals("mine")) {
                        mine("se");
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
                    } else if (checkCollision("e").equals("mine")) {
                        mine("e");
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
                    } else if (checkCollision("nw").equals("mine")) {
                        mine("nw");
                        return;
                    }
                    currentX -= 4;
                    currentY += 4;
                    changeFrame("nw");
                } else if (currentY - destinationY > 5) {
                    if (checkCollision("sw").equals("water")) {
                        return;
                    } else if (checkCollision("sw").equals("cut")) {
                        cutTree("sw");
                        return;
                    } else if (checkCollision("sw").equals("mine")) {
                        mine("sw");
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
                    } else if (checkCollision("w").equals("mine")) {
                        mine("w");
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
                    }  else if (checkCollision("n").equals("mine")) {
                        mine("n");
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
                    }  else if (checkCollision("s").equals("mine")) {
                        mine("s");
                        return;
                    }
                    currentY -= 4;
                    changeFrame("s");
                } else {
                    currentAtlasKey = currentAtlasKey.replaceAll("walking", "looking");

                    this.setRegion(lookingAtlas.findRegion(currentAtlasKey));
                }
            }
        //}
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

    public void mine(String direction) {
        this.setPosition(currentX, currentY);
        currentFrame++;
        if (currentFrame > 12) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("working %s%04d", direction, currentFrame);
        this.setRegion(miningAtlas.findRegion(currentAtlasKey));
    }

    public void carryTrunk(String direction) {
        this.setPosition(currentX, currentY);
        currentFrame++;
        if (currentFrame > 7) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("walking with trunk %s%04d", direction, currentFrame);
        this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
    }

    public void select() {
        selected = true;
    }

    public void unSelect() {
        selected = false;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setCurrentXY(int x, int y) {
        currentX = x;
        currentY = y;
    }

    public void setDestination(float x, float y) {
        destinationX = x;
        destinationY = y;
    }

    public void setLocation(int x, int y) {
        this.setPosition((float) x, (float) y);
        currentX = x;
        currentY = y;
    }
}

