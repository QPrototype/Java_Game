package character_movement;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class CharacterMovement extends Sprite {

    private TextureAtlas walkingAtlas;
    private TextureAtlas lookingAtlas;


    private int currentFrame = 1;
    private String currentAtlasKey;
    private int currentX = 50;
    private int currentY = 100;


    public CharacterMovement(TextureAtlas walkingAtlas, TextureAtlas lookingAtlas, TextureAtlas.AtlasRegion region) {
        super(new Sprite(region));

        this.walkingAtlas = walkingAtlas;
        this.lookingAtlas = lookingAtlas;

        //this.sprite = sprite;
    }

    public void walk(float destinationX, float destinationY) {
        if (destinationX - currentX > 10) {
            if (destinationY > currentY) {
                currentX += 5;
                currentY += 5;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking ne%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
            } else if (currentY - destinationY > 10) {
                currentX += 5;
                currentY -= 5;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking se%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
            } else {
                currentX += 5;

                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking e%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));
            }

        }
        if (currentX - destinationX > 10) {
            if (destinationY - currentY > 10) {
                currentX -= 5;
                currentY += 5;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking nw%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

            } else if (currentY - destinationY > 10) {
                currentX -= 5;
                currentY -= 5;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking sw%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

            } else {
                currentX -= 5;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking w%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

            }
        }
        if (Math.abs(destinationX - currentX) <= 10) {
            if (destinationY - currentY > 10) {
                currentY += 5;
                this.setPosition(currentX, currentY);
                currentFrame++;
                if (currentFrame > 7) {
                    currentFrame = 0;
                }
                currentAtlasKey = String.format("walking n%04d", currentFrame);
                this.setRegion(walkingAtlas.findRegion(currentAtlasKey));

            } else if (currentY - destinationY > 10) {
                currentY -= 5;
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
