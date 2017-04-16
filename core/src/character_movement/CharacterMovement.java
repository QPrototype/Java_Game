package character_movement;

import Screens.MainScreen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import Scenes.GameHud;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class CharacterMovement extends Sprite {

    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;
    private TextureAtlas cuttingAtlas;
    private TextureAtlas miningAtlas;
    private TextureAtlas carryingTrunkAtlas;
    private TextureAtlas carryingIronOreAtlas;
    private TextureAtlas pickingUpAtlas;
    private TextureAtlas carryingBerriesAtlas;

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
    public static int pickStart = -1;
    private boolean carryingLogs = false;
    private boolean carryingIronOres = false;
    private boolean carryingBerries = false;
    private int lastTreeX;
    private int lastTreeY;
    private int lastOreX;
    private int lastOreY;
    private int lastBushX;
    private int lastBushY;
    private int counter = 0;


    public TiledMapTileLayer.Cell cut;
    public TiledMapTileLayer.Cell mine;
    public TiledMapTileLayer.Cell pick;

    private boolean selected = false;


    public CharacterMovement(String type, ArrayList<TextureAtlas> atlases, TextureAtlas.AtlasRegion region,
                             TiledMap map) {
        super(new Sprite(region));

        this.walkingAtlas = atlases.get(0);
        this.lookingAtlas = atlases.get(1);
        this.cuttingAtlas = atlases.get(2);
        this.miningAtlas = atlases.get(3);
        this.carryingTrunkAtlas = atlases.get(4);
        this.carryingIronOreAtlas = atlases.get(5);
        this.pickingUpAtlas = atlases.get(6);
        this.carryingBerriesAtlas = atlases.get(7);
        this.map = map;
        this.background = (TiledMapTileLayer) map.getLayers().get(0);
        this.foreground = (TiledMapTileLayer) map.getLayers().get(1);
        this.type = type;
    }

    public int getCurrentX() {
        return this.currentX;
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public String checkCollision(String direction) {
        // 19 9 house
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
                        pick = foreground.getCell(i - 1, j - 6);

                        if (cut != null && cut.getTile() != null && cut.getTile().getProperties().containsKey("suur")) {
                            if (cutStart == -1) {
                                cutStart = GameHud.getTime();
                            }
                            if (cutStart - 3 > GameHud.getTime()) {
                                //current = cut.getTile();

                                cut.setTile(null);
                                MainScreen.removeTree(new Point2D.Double(i - 1, j - 3));
                                cutStart = -1;
                                // For automatic carrying of logs
                                carryingLogs = true;
                                // Where to carry logs --> Main building location
                                lastTreeX = x;
                                lastTreeY = y;
                                destinationX = 870;
                                destinationY = -185;


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
                                MainScreen.removeIronOre(new Point2D.Double(i, j - 2));
                                mineStart = -1;

                                // For automatic carrying of logs
                                carryingIronOres = true;
                                // Where to carry logs --> Main building location
                                lastOreX = x;
                                lastOreY = y;
                                destinationX = 870;
                                destinationY = -185;

                                //GameHud.addIron(20);
                            }
                            return "mine";
                        } else if (pick != null && pick.getTile() != null
                                && pick.getTile().getProperties().containsKey("berries") && !carryingBerries) {
                            if (pickStart == -1) {
                                pickStart = GameHud.getTime();
                            }
                            if (pickStart - 10 > GameHud.getTime()) {

                                //pick.setTile(null);

                                pickStart = -1;

                                // For automatic carrying of berries
                                carryingBerries = true;
                                // Where to carry berries --> Main building location
                                lastBushX = x - 1;
                                lastBushY = y - 6;
                                System.out.println("x: " + x + "y: " + y);
                                destinationX = 870;
                                destinationY = -185;
                            }
                            return "berries";
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
                    } else if (checkCollision("ne").equals("berries")) {
                        pickUp("ne");
                        return;
                    }
                    currentX += 3;
                    currentY += 3;
                    if (carryingLogs) {
                        carryTrunk("ne");
                    } else if (carryingIronOres) {
                        carryIron("ne");
                    } else if (carryingBerries) {
                        carryBerries("ne");
                    } else {
                        changeFrame("ne");
                    }
                } else if (currentY - destinationY > 5) {
                    if (checkCollision("se").equals("water")) {
                        return;
                    } else if (checkCollision("se").equals("cut")) {
                        cutTree("se");
                        return;
                    }  else if (checkCollision("se").equals("mine")) {
                        mine("se");
                        return;
                    } else if (checkCollision("se").equals("berries")) {
                        pickUp("se");
                        return;
                    }
                    currentX += 4;
                    currentY -= 4;
                    if (carryingLogs) {
                        carryTrunk("se");
                    } else if (carryingIronOres) {
                        carryIron("se");
                    } else if (carryingBerries) {
                        carryBerries("se");
                    } else {
                        changeFrame("se");
                    }
                } else {
                    if (checkCollision("e").equals("water")) {
                        return;
                    } else if (checkCollision("e").equals("cut")) {
                        cutTree("e");
                        return;
                    } else if (checkCollision("e").equals("mine")) {
                        mine("e");
                        return;
                    } else if (checkCollision("e").equals("berries")) {
                        pickUp("e");
                        return;
                    }
                    currentX += 3;
                    //this.setPosition(currentX, currentY);
                    if (carryingLogs) {
                        carryTrunk("e");
                    } else if (carryingIronOres) {
                        carryIron("e");
                    } else if (carryingBerries) {
                        carryBerries("e");
                    } else {
                        changeFrame("e");
                    }
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
                    } else if (checkCollision("nw").equals("berries")) {
                        pickUp("nw");
                        return;
                    }
                    currentX -= 4;
                    currentY += 4;
                    if (carryingLogs) {
                        carryTrunk("nw");
                    } else if (carryingIronOres) {
                        carryIron("nw");
                    } else if (carryingBerries) {
                        carryBerries("nw");
                    } else {
                        changeFrame("nw");
                    }
                } else if (currentY - destinationY > 5) {
                    if (checkCollision("sw").equals("water")) {
                        return;
                    } else if (checkCollision("sw").equals("cut")) {
                        cutTree("sw");
                        return;
                    } else if (checkCollision("sw").equals("mine")) {
                        mine("sw");
                        return;
                    } else if (checkCollision("sw").equals("berries")) {
                        pickUp("sw");
                        return;
                    }
                    currentX -= 3;
                    currentY -= 3;
                    if (carryingLogs) {
                        carryTrunk("sw");
                    } else if (carryingIronOres) {
                        carryIron("sw");
                    } else if (carryingBerries) {
                        carryBerries("sw");
                    } else {
                        changeFrame("sw");
                    }
                } else {
                    if (checkCollision("w").equals("water")) {
                        return;
                    } else if (checkCollision("w").equals("cut")) {
                        cutTree("w");
                        return;
                    } else if (checkCollision("w").equals("mine")) {
                        mine("w");
                        return;
                    } else if (checkCollision("w").equals("berries")) {
                        pickUp("w");
                        return;
                    }
                    currentX -= 4;
                    if (carryingLogs) {
                        carryTrunk("w");
                    } else if (carryingIronOres) {
                        carryIron("w");
                    } else if (carryingBerries) {
                        carryBerries("w");
                    } else {
                        changeFrame("w");
                    }
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
                    } else if (checkCollision("n").equals("berries")) {
                        pickUp("n");
                        return;
                    }
                    currentY += 4;
                    if (carryingLogs) {
                        carryTrunk("n");
                    } else if (carryingIronOres) {
                        carryIron("n");
                    } else if (carryingBerries) {
                        carryBerries("n");
                    } else {
                        changeFrame("n");
                    }
                } else if (currentY - destinationY > 5) {
                    if (checkCollision("s").equals("water")) {
                        return;
                    } else if (checkCollision("s").equals("cut")) {
                        cutTree("s");
                        return;
                    }  else if (checkCollision("s").equals("mine")) {
                        mine("s");
                        return;
                    } else if (checkCollision("s").equals("berries")) {
                        pickUp("s");
                        return;
                    }
                    currentY -= 4;
                    if (carryingLogs) {
                        carryTrunk("s");
                    } else if (carryingIronOres) {
                        carryIron("s");
                    } else if (carryingBerries) {
                        carryBerries("s");
                    } else {
                        changeFrame("s");
                    }
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

    public void pickUp(String direction) {
        this.setPosition(currentX, currentY);
        counter++;
        if (counter % 4 == 0) {
            currentFrame++;
        }
        if (currentFrame > 8) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("pick up %s%04d", direction, currentFrame);
        this.setRegion(pickingUpAtlas.findRegion(currentAtlasKey));
    }

    public void carryTrunk(String direction) {
            this.setPosition(currentX, currentY);
        currentFrame++;
        if (currentFrame > 7) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("walking with trunk %s%04d", direction, currentFrame);
        this.setRegion(carryingTrunkAtlas.findRegion(currentAtlasKey));
        if (Math.abs(currentX - destinationX) < 10 && Math.abs(currentY - destinationY) < 10) {
            GameHud.addWood(20);

            carryingLogs = false;
            int difference = 5000;
            int nextX = -1;
            int nextY = -1;
            for (Point2D coordinates : MainScreen.allTrees) {
                int tempDiff = 0;
                tempDiff += Math.abs(coordinates.getX() - lastTreeX);
                tempDiff += Math.abs(coordinates.getY() - lastTreeY);
                if (tempDiff < difference) {
                    difference = tempDiff;
                    nextX = (int) coordinates.getX();
                    nextY = (int) coordinates.getY();
                }
            }
            //System.out.println(nextX);
            //System.out.println(nextY);
            if (nextX != -1 && nextY != -1) {
                destinationX = (nextX + nextY) * TILE_HEIGHT + 110;
                destinationY = (nextY - nextX) / 2 * TILE_HEIGHT + 20;
                //System.out.println(destinationX);
                //System.out.println(destinationY);
            } else {
                //int currPosX = currentX / TILE_WIDTH - currentY / TILE_HEIGHT + 1;
                //int currPosY = currentX / TILE_WIDTH + currentY / TILE_HEIGHT + 1;
                destinationX = (lastTreeX + lastTreeY) * TILE_HEIGHT + 110;
                destinationY = (lastTreeY - lastTreeX) / 2 * TILE_HEIGHT + 20;

            }
        }
    }

    public void carryIron(String direction) {
        this.setPosition(currentX, currentY);
        currentFrame++;
        if (currentFrame > 7) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("walking %s%04d", direction, currentFrame);
        this.setRegion(carryingIronOreAtlas.findRegion(currentAtlasKey));
        if (Math.abs(currentX - destinationX) < 10 && Math.abs(currentY - destinationY) < 10) {
            GameHud.addIron(20);

            carryingIronOres = false;
            int difference = 5000;
            int nextX = -1;
            int nextY = -1;
            for (Point2D coordinates : MainScreen.allIronOres) {
                int tempDiff = 0;
                tempDiff += Math.abs(coordinates.getX() - lastOreX);
                tempDiff += Math.abs(coordinates.getY() - lastOreY);
                if (tempDiff < difference) {
                    difference = tempDiff;
                    nextX = (int) coordinates.getX();
                    nextY = (int) coordinates.getY();
                }
            }
            if (nextX != -1 && nextY != -1) {
                destinationX = (nextX + nextY) * TILE_HEIGHT + 110;
                destinationY = (nextY - nextX) / 2 * TILE_HEIGHT + 20;
            } else {

                destinationX = (lastOreX + lastOreY) * TILE_HEIGHT + 110;
                destinationY = (lastOreY - lastOreX) / 2 * TILE_HEIGHT + 20;

            }
        }
    }

    public void carryBerries(String direction) {
        this.setPosition(currentX, currentY);
        currentFrame++;
        if (currentFrame > 7) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("walking %s%04d", direction, currentFrame);
        this.setRegion(carryingBerriesAtlas.findRegion(currentAtlasKey));
        if (Math.abs(currentX - destinationX) < 10 && Math.abs(currentY - destinationY) < 10) {
            //GameHud.addFood(20);

            carryingBerries = false;
            //int difference = 5000;
            //int nextX = -1;
            //int nextY = -1;
            //for (Point2D coordinates : MainScreen.allIronOres) {
            //    int tempDiff = 0;
            //    tempDiff += Math.abs(coordinates.getX() - lastOreX);
            //    tempDiff += Math.abs(coordinates.getY() - lastOreY);
            //    if (tempDiff < difference) {
            //        difference = tempDiff;
            //        nextX = (int) coordinates.getX();
            //        nextY = (int) coordinates.getY();
            //    }
            //}
            //if (nextX != -1 && nextY != -1) {
            //    destinationX = (nextX + nextY) * TILE_HEIGHT + 110;
            //    destinationY = (nextY - nextX) / 2 * TILE_HEIGHT + 20;
            //} else {

            destinationX = (lastBushX + lastBushY) * TILE_HEIGHT + 110;
            destinationY = (lastBushY - lastBushX) / 2 * TILE_HEIGHT + 20;
            //}
        }

    }

    public void select() {
        selected = true;
    }

    public void unSelect() {
        selected = false;
    }

    public boolean isSelected() {
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

    public boolean isCarryingLogs() {
        return carryingLogs;
    }

    public boolean isCarryingIronOres() {
        return carryingIronOres;
    }

    public boolean isCarryingBerries() {
        return carryingBerries;
    }
}

