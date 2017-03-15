package character_movement;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;


public class CharacterMovement extends Sprite {

    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;

    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 32;


    private int currentFrame = 1;
    private String currentAtlasKey;
    private int currentX = 800;
    private int currentY = 0;

    private TiledMap map;
    private TiledMapTileLayer background;
    private TiledMapTileLayer foreground;


    public CharacterMovement(TextureAtlas walkingAtlas, TextureAtlas lookingAtlas, TextureAtlas.AtlasRegion region,
                             TiledMap map) {
        super(new Sprite(region));

        this.walkingAtlas = walkingAtlas;
        this.lookingAtlas = lookingAtlas;
        this.map = map;
        this.setPosition(currentX, currentY);
        this.background = (TiledMapTileLayer) map.getLayers().get(0);
        //this.foreground = (TiledMapTileLayer) map.getLayers().get(1);


        //this.sprite = sprite;
    }

    public int getCurrentX() {
        return this.currentX;
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public boolean checkCollision(int currentX, int currentY, String direction) {
        int x = currentX / TILE_WIDTH - currentY / TILE_HEIGHT;
        int y = currentX / TILE_WIDTH + currentY / TILE_HEIGHT;
        if (direction.equals("ne")) {
            y++;
        } else if (direction.equals("se")) {
            x++;
            //y++;
        } else if (direction.equals("e")) {
            x++;
            y += 2;
        } else if (direction.equals("nw")) {
            x--;
            y++;
        } /*else if (direction.equals("sw")) {
            //do nothing
        }*/ else if (direction.equals("w")) {
           x--;
        } else if (direction.equals("n")) {
            x--;
            y += 2;
        } else if (direction.equals("s")) {
            x++;
        }
        TiledMapTileLayer.Cell currentCell = background.getCell(x, y);
        return (currentCell != null && currentCell.getTile().
                getProperties().containsKey("Water"));
    }


    public void walk(float destinationX, float destinationY) {
        if (destinationX == -1 && destinationY == -1) {
            return;
        }
        currentAtlasKey = String.format("looking ne%04d", currentFrame);

        if (destinationX - currentX > 5) {
            if (destinationY > currentY
                    && !checkCollision(currentX, currentY,"ne")) {
                currentX += 3;
                currentY += 3;
                changeFrame("ne");
            } else if (currentY - destinationY > 5
                    && !checkCollision(currentX, currentY,"se")) {
                currentX += 4;
                currentY -= 4;
                changeFrame("se");
            } else {
                if (!checkCollision(currentX, currentY,"e")) {
                    currentX += 3;
                    this.setPosition(currentX, currentY);
                    changeFrame("e");
                }
            }
        } else if (currentX - destinationX > 5) {
            if (destinationY - currentY > 5
                    && !checkCollision(currentX, currentY,"nw")) {
                currentX -= 4;
                currentY += 4;
                changeFrame("nw");
            } else if (currentY - destinationY > 5
                    && !checkCollision(currentX, currentY, "sw")) {
                currentX -= 3;
                currentY -= 3;
                changeFrame("sw");
            } else {
                if (!checkCollision(currentX, currentY,"w")) {
                    currentX -= 4;
                    changeFrame("w");
                }
            }
        } else if (Math.abs(destinationX - currentX) <= 5) {
            if (destinationY - currentY > 1
                    && !checkCollision(currentX, currentY,"n")) {
                    currentY += 4;
                    changeFrame("n");
            } else if (currentY - destinationY > 5
                    && !checkCollision(currentX, currentY,"s")) {
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

    public void cutTree(String direction, TextureAtlas cuttingAtlas) {
        this.setPosition(currentX, currentY);
        if (currentFrame > 12) {
            currentFrame = 0;
        }
        currentAtlasKey = String.format("felling tree %s%04d", direction, currentFrame);
        this.setRegion(cuttingAtlas.findRegion(currentAtlasKey));

    }
}

