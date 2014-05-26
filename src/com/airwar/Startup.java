package com.airwar;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;

/**
 * Created by Zuegg on 26.05.2014.
 */
public class Startup extends SimpleApplication implements PhysicsCollisionListener, ActionListener {
    public static void main(String args[]) {
        Startup startup = new Startup();
        startup.start();
    }


    Ambient ambient;

    Building building;

    Airplane airplane;

    BulletAppState bulletAppState;

    WayPointSystem wayPointSystem;

    @Override
    public void simpleInitApp() {


        bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        ambient = new Ambient(this);
        ambient.load();

        building = new Building(this);
        building.load();


        airplane = new Airplane(this);
        airplane.load();
        airplane.reset();
        flyCam.setMoveSpeed(100f);
        cam.setFrustumFar(2000);

        BitmapText hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.Blue);                             // font color            // the text
        hudText.setLocalTranslation(10, viewPort.getCamera().getHeight() - (hudText.getLineHeight() * 2), 0); // position
        guiNode.attachChild(hudText);
        wayPointSystem = new WayPointSystem(this, hudText);
        wayPointSystem.load();

        getInputManager().addMapping("Reset", new KeyTrigger(KeyInput.KEY_F2));
        getInputManager().addMapping("SwitchMode", new KeyTrigger(KeyInput.KEY_F3));
        getInputManager().addListener(this, "Reset", "SwitchMode");
      /*  System.out.println("init");
        Spatial world = assetManager.loadModel("Scenes/SceneWithBuilding.j3o");
        world.setLocalScale(10, 4, 10);
        rootNode.attachChild(world);

        flyCam.setMoveSpeed(100f);

        System.out.println("done");
        Node planeNode=new Node();
        airplane = assetManager.loadModel("Models/Airplane.j3o");
        airplane.setLocalScale(0.1f);
        planeNode.attachChild(airplane);
        rootNode.attachChild(planeNode);

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
        rootNode.addLight(ambientLight);

        DirectionalLight directionalLight=new DirectionalLight();
        directionalLight.setDirection(new Vector3f(0.73994213f, -0.6427877f, 0.19826694f ));
        rootNode.addLight(directionalLight);

        AudioNode audioNode=new AudioNode();
        audioNode.setAudioData(assetManager.loadAudio("Sounds/converted/propeller.ogg"),new AudioKey());
        audioNode.setPositional(false);
        audioNode.setLooping(true);

        planeNode.attachChild(audioNode);
        audioNode.play();*/

        BitmapText hudText2 = new BitmapText(guiFont, false);
        hudText2.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText2.setColor(ColorRGBA.Blue);                             // font color
        hudText2.setText("Press F2 to restart the Game");             // the text
        hudText2.setLocalTranslation(10, viewPort.getCamera().getHeight() - hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText2);
    }


    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        airplane.update(tpf);
    }

    @Override
    public void collision(PhysicsCollisionEvent physicsCollisionEvent) {
        // System.out.println("Collision between: " + physicsCollisionEvent.getNodeA().getName() + ":" + physicsCollisionEvent.getNodeB().getName());
        if (physicsCollisionEvent.getNodeA().getName().equals("Airplane")) {
            if (physicsCollisionEvent.getNodeB().getName().equals("Building")) {
                airplane.collideWith(physicsCollisionEvent.getNodeB());
            } else if (physicsCollisionEvent.getNodeB().getName().equals("Waypoint")) {
                wayPointSystem.removeWaypoint(physicsCollisionEvent.getNodeB());
            }
        }
        if (physicsCollisionEvent.getNodeB().getName().equals("Airplane")) {
            if (physicsCollisionEvent.getNodeA().getName().equals("Building")) {
                airplane.collideWith(physicsCollisionEvent.getNodeA());
            } else if (physicsCollisionEvent.getNodeA().getName().equals("Waypoint")) {
                wayPointSystem.removeWaypoint(physicsCollisionEvent.getNodeA());
            }
        }
    }

    @Override
    public void onAction(String s, boolean b, float v) {
        if (s.equals("Reset")) {
            if (b) {
                airplane.reset();
                wayPointSystem.reset();
            }
        }
        if (s.equals("SwitchMode")) {
            if (b) {
                wayPointSystem.switchMode();
                airplane.reset();
                wayPointSystem.reset();
            }
        }
    }
}
