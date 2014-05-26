package com.airwar;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 * Created by Zuegg on 26.05.2014.
 */
public class Ambient {
    SimpleApplication simpleApplication;

    String skybox = "Textures/BrightSky.dds";
    public DirectionalLight directionalLight;
    public AmbientLight ambientLight;
    FilterPostProcessor fpp;
    WaterFilter water;

    Vector3f lightDir = new Vector3f(0.73994213f, -0.6427877f, 0.19826694f);

    public Ambient(SimpleApplication simpleApplication) {
        this.simpleApplication = simpleApplication;
    }

    public void load() {
        directionalLight = new DirectionalLight();
        directionalLight.setDirection(lightDir);

        ambientLight = new AmbientLight();
        ambientLight.setColor(new ColorRGBA(1f, 1f, 1f, 1f));

        simpleApplication.getRootNode().attachChild(SkyFactory.createSky(simpleApplication.getAssetManager(), skybox, false));


        simpleApplication.getRootNode().addLight(ambientLight);
        simpleApplication.getRootNode().addLight(directionalLight);

        fpp = new FilterPostProcessor(simpleApplication.getAssetManager());
        water = new WaterFilter(simpleApplication.getRootNode(), lightDir);
        water.setWaterHeight(0);
        fpp.addFilter(water);
        simpleApplication.getViewPort().addProcessor(fpp);

    }

}
