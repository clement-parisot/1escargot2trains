package com.escargot.game.desktop;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
public class MyPacker {
    public static void main (String[] args) throws Exception {
    	System.out.println(".."+File.separator+ "android" + File.separator +"assets"+ File.separator);
        TexturePacker.process(".."+File.separator+"images", ".."+File.separator+ "android" + File.separator +"assets"+ File.separator , "pack");
    }
}