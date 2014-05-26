package com.airwar;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.scene.Spatial;

/**
 * Created by Zuegg on 26.05.2014.
 */
public class Building {
    SimpleApplication simpleApplication;

    Spatial world;

    public Building(SimpleApplication simpleApplication) {
        this.simpleApplication = simpleApplication;
    }

    public void load() {
        world = simpleApplication.getAssetManager().loadModel("Scenes/SceneWithBuilding.j3o");
        world.setName("Building");


        world.setLocalScale(10, 4, 10);
        RigidBodyControl worldPhysics = new RigidBodyControl(CollisionShapeFactory.createMeshShape(world));
        worldPhysics.setMass(0);
        world.addControl(worldPhysics);
        simpleApplication.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(worldPhysics);

        simpleApplication.getRootNode().attachChild(world);
    }
}
