package com.mygdx.game.Naloga2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Assets {
    public static  BitmapFont font;
    public static  Sound waterDropVoice;
    public static  Sound IceCreamCollect;
    public static  Sound LaserGun;
    public static Texture background;
    public static Texture sugarImg;
    public static Texture waterImg;
    public static Texture iceCreamImg;
    public static Texture bulletImg;

    public static Texture bonusImg;

    public static void load() {
        sugarImg = new Texture("assets/SugarGame/images/sugar-cube.png");
        waterImg = new Texture("assets/SugarGame/images/waterDrop1.png");
        background = new Texture("assets/SugarGame/images/backgroundClouds.png");
        iceCreamImg = new Texture("assets/SugarGame/images/iceCream.png");
        bulletImg = new Texture("assets/SugarGame/images/bullet.png");
        bonusImg = new Texture("assets/SugarGame/images/bonus1.png");

        waterDropVoice = Gdx.audio.newSound(Gdx.files.internal("assets/SugarGame/sounds/soothing-waterdrop.wav"));
        IceCreamCollect = Gdx.audio.newSound(Gdx.files.internal("assets/SugarGame/sounds/plop-effect.wav"));
        LaserGun = Gdx.audio.newSound(Gdx.files.internal("assets/SugarGame/sounds/laser-gun-81720.mp3"));

        font = new BitmapFont(Gdx.files.internal("assets/SugarGame/fonts/arial-32.fnt"));

    }

    public static void dispose() {
        sugarImg.dispose();
        waterImg.dispose();
        iceCreamImg.dispose();
        font.dispose();
        bulletImg.dispose();
        IceCreamCollect.dispose();
        LaserGun.dispose();
        background.dispose();
        bonusImg.dispose();

    }
}
