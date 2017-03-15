package character_movement;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;


public class CharacterMovement extends Sprite {

    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;

    private int TILE_WIDTH = 64;
    private int TILE_HEIGHT = 32;


    private int currentFrame = 1;
    private String currentAtlasKey;
    private int currentX = 800;
    private int currentY = 0;

    private TiledMapTileLayer collisionLayer;


    public CharacterMovement(TextureAtlas walkingAtlas, TextureAtlas lookingAtlas, TextureAtlas.AtlasRegion region,
                             TiledMapTileLayer collisionLayer) {
        super(new Sprite(region));

        this.walkingAtlas = walkingAtlas;
        this.lookingAtlas = lookingAtlas;
        this.collisionLayer = collisionLayer;
        this.setPosition(currentX, currentY);


        //this.sprite = sprite;
    }

    public int getCurrentX() {
        return this.currentX;
    }

    public int getCurrentY() {
        return this.currentY;
    }

    public boolean checkCollision(int currentX, int currentY, TiledMapTileLayer layer, String direction) {
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
        TiledMapTileLayer.Cell currentCell = layer.getCell(x, y);
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
                    && !checkCollision(currentX, currentY, collisionLayer, "ne")) {
                currentX += 3;
                currentY += 3;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking ne%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

            } else if (currentY - destinationY > 5
                    && !checkCollision(currentX, currentY, collisionLayer, "se")) {
                currentX += 4;
                currentY -= 4;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking se%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

            } else {
                if (!checkCollision(currentX, currentY, collisionLayer, "e")) {
                    currentX += 3;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking e%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
                }
            }
        } else if (currentX - destinationX > 5) {
            if (destinationY - currentY > 5
                    && !checkCollision(currentX, currentY, collisionLayer, "nw")) {
                currentX -= 4;
                currentY += 4;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking nw%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

            } else if (currentY - destinationY > 5
                    && !checkCollision(currentX, currentY, collisionLayer, "sw")) {
                currentX -= 3;
                currentY -= 3;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking sw%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
            } else {
                if (!checkCollision(currentX, currentY, collisionLayer, "w")) {
                    currentX -= 4;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking w%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

                }
            }
        } else if (Math.abs(destinationX - currentX) <= 10) {
            if (destinationY - currentY > 1
                    && !checkCollision(currentX, currentY, collisionLayer, "n")) {
                    currentY += 4;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking n%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
            } else if (currentY - destinationY > 5
                    && !checkCollision(currentX, currentY, collisionLayer, "s")) {
                    currentY -= 4;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking s%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
            } else {
                currentAtlasKey = currentAtlasKey.replaceAll("walking", "looking");

                this.setRegion(lookingAtlas.findRegion(currentAtlasKey));
            }
        }
    }
}

