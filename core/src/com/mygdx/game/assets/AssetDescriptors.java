package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors
{

    public static final AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<BitmapFont>(AssetPaths.FONT, BitmapFont.class);

    public static final AssetDescriptor<TextureAtlas> GAMEPLAY =
            new AssetDescriptor<TextureAtlas>(AssetPaths.GAMEPLAY, TextureAtlas.class);

    public static final AssetDescriptor<Sound> WATERDROP_VOICE =
            new AssetDescriptor<>(AssetPaths.WATERDROP_VOICE, Sound.class);

    public static final AssetDescriptor<Sound> ICECREAM_COLLECT =
            new AssetDescriptor<>(AssetPaths.ICECREAM_COLLECT, Sound.class);

    public static final AssetDescriptor<Sound> LASERGUN =
            new AssetDescriptor<>(AssetPaths.LASER_GUN, Sound.class);

    public static final AssetDescriptor<ParticleEffect> FIREWORK =
            new AssetDescriptor<>(AssetPaths.FIREWORK, ParticleEffect.class);

    public static final AssetDescriptor<ParticleEffect> STAR =
            new AssetDescriptor<>(AssetPaths.STAR, ParticleEffect.class);


    private AssetDescriptors() {}
}
