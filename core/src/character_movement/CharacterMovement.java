package character_movement;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;


public class CharacterMovement extends Sprite {

    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;


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

    public boolean checkCollision(TiledMapTileLayer layer, String direction) {
        TiledMapTileLayer.Cell currentCell = collisionLayer
                .getCell(currentX / 64 - currentY / 32, currentX / 64 + currentY / 32 + 2);
        return (currentCell != null && currentCell.getTile().
                getProperties().containsKey("Water"));
    }


    public void walk(float destinationX, float destinationY) {
        if (destinationX == -1 && destinationY == -1) {
            return;
        }
        currentAtlasKey = String.format("looking ne%04d", currentFrame);

        if (destinationX - currentX > 5) {
            if (destinationY > currentY) {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32, currentX / 64 + currentY / 32 + 2);
                if (!((currentCell != null && currentCell.getTile().
                        getProperties().containsKey("Water")))) {
                    currentX += 3;
                    currentY += 3;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking ne%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
                }
            } else if (currentY - destinationY > 5) {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32 + 1, currentX / 64 + currentY / 32 + 1);
                if (!((currentCell != null && currentCell.getTile().
                        getProperties().containsKey("Water")))) {
                    currentX += 4;
                    currentY -= 4;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking se%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
                }
            } else {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32 + 1, currentX / 64 + currentY / 32 + 2);
                if (!((currentCell != null && currentCell.getTile().
                        getProperties().containsKey("Water")))) {
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

        }
        else if (currentX - destinationX > 5) {
            if (destinationY - currentY > 5) {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32 - 1, currentX / 64 + currentY / 32 + 1);
                if (!((currentCell != null && currentCell.getTile().
                        getProperties().containsKey("Water")))) {
                    currentX -= 4;
                    currentY += 4;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking nw%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
                }

            } else if (currentY - destinationY > 5) {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32, currentX / 64 + currentY / 32);
                if (!((currentCell != null && currentCell.getTile().
                        getProperties().containsKey("Water")))) {
                    currentX -= 3;
                    currentY -= 3;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking sw%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
                }

            } else {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32 - 1, currentX / 64 + currentY / 32);
                if (!((currentCell != null && currentCell.getTile().
                        getProperties().containsKey("Water")))) {
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
        }
        else if (Math.abs(destinationX - currentX) <= 10) {
            if (destinationY - currentY > 1) {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32 - 1, currentX / 64 + currentY / 32 + 2);
                if (!((currentCell != null && currentCell.getTile().
                        getProperties().containsKey("Water")))) {
                    currentY += 4;
                    this.setPosition(currentX, currentY);
                    currentFrame++;
                    if (currentFrame > 7) {
                        currentFrame = 0;
                    }
                    currentAtlasKey = String.format("walking n%04d", currentFrame);
                    this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
                }

            } else if (currentY - destinationY > 5) {
                TiledMapTileLayer.Cell currentCell = collisionLayer
                        .getCell(currentX / 64 - currentY / 32 + 1, currentX / 64 + currentY / 32);
                    if (!((currentCell != null && currentCell.getTile().
                            getProperties().containsKey("Water")))) {
                        currentY -= 4;
                        this.setPosition(currentX, currentY);
                        currentFrame++;
                        if (currentFrame > 7) {
                            currentFrame = 0;
                        }
                        currentAtlasKey = String.format("walking s%04d", currentFrame);
                        this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
                    }

            } else {
                currentAtlasKey = currentAtlasKey.replaceAll("walking", "looking");

                this.setRegion(lookingAtlas.findRegion(currentAtlasKey));
            }
        }
    }
}
