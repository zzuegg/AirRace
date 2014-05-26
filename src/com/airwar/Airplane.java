package com.airwar;


import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioKey;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Created by Zuegg on 26.05.2014.
 */
public class Airplane implements ActionListener {
    Node model;
    Node vNode;
    Node hNode;
    Spatial airplane;
    SimpleApplication simpleApplication;

    Vector3f flightDirection = new Vector3f(0, 0.5f, 1);
    float flightSpeed = 50;

    float vAngle;
    float hAngle;
    boolean up, down, left, right;


    AudioNode engineAudio;
    AudioNode explosionAudio;
    float enginePitch = 1;

    Vector3f startPos = new Vector3f(0, 40, -1500);

    public Airplane(SimpleApplication simpleApplication) {
        this.simpleApplication = simpleApplication;

    }

    public void reset() {
        model.setLocalTranslation(startPos);
        model.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
        flightDirection = new Vector3f(0, 0.5f, 1);
        airplane.setLocalRotation(new Quaternion());
        enabled = true;
        engineAudio.play();
    }


    boolean enabled = true;

    public void load() {
        model = new Node();

        airplane = simpleApplication.getAssetManager().loadModel("Models/Airplane.j3o");
        airplane.setLocalScale(0.1f);


        model.attachChild(airplane);

        simpleApplication.getRootNode().attachChild(model);

        simpleApplication.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        simpleApplication.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        simpleApplication.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        simpleApplication.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        simpleApplication.getInputManager().addMapping("DBG", new KeyTrigger(KeyInput.KEY_SPACE));
        simpleApplication.getInputManager().addListener(this, "Left", "Right", "Up", "Down", "DBG");

        engineAudio = new AudioNode(simpleApplication.getAssetManager().loadAudio("Sounds/converted/propeller.ogg"), new AudioKey());
        engineAudio.setPositional(false);
        engineAudio.setLooping(true);
        model.attachChild(engineAudio);


        explosionAudio = new AudioNode(simpleApplication.getAssetManager().loadAudio("Sounds/converted/explosion1.ogg"), new AudioKey());
        explosionAudio.setPositional(false);
        model.attachChild(explosionAudio);

        CollisionShape collisionShape = CollisionShapeFactory.createBoxShape(airplane);
        GhostControl ghostControl = new GhostControl(collisionShape);
        airplane.addControl(ghostControl);
        simpleApplication.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(ghostControl);
        airplane.setName("Airplane");
    }


    public void update(float tpf) {
        if (enabled) {
            if (down) {
                airplane.rotate(new Quaternion().fromAngleAxis(-1 * tpf, Vector3f.UNIT_X));
                hAngle = hAngle - 0.7f * tpf;
            }
            if (up) {
                airplane.rotate(new Quaternion().fromAngleAxis(1 * tpf, Vector3f.UNIT_X));
                hAngle = hAngle + 1 * tpf;
            }
            if (left) {
                airplane.rotate(new Quaternion().fromAngleAxis(-3 * tpf, Vector3f.UNIT_Z));
                vAngle = vAngle - 1 * tpf;
            }
            if (right) {
                airplane.rotate(new Quaternion().fromAngleAxis(3 * tpf, Vector3f.UNIT_Z));
                vAngle = vAngle + 1 * tpf;
            }


            //airplane.lookAt(model.getLocalTranslation().add(flightDirection), Vector3f.UNIT_Y);


            Quaternion h = new Quaternion().fromAngleAxis(hAngle, Vector3f.UNIT_X);
            Quaternion v = new Quaternion().fromAngleAxis(vAngle, Vector3f.UNIT_Z);

            Quaternion rotation = airplane.getWorldRotation();


            flightDirection = rotation.mult(new Vector3f(0, 0, 1));


            model.setLocalTranslation(model.getWorldTranslation().add(flightDirection.mult(flightSpeed * tpf)));
            //airplane.setLocalRotation(rotation);
            Vector3f camLocation = model.getLocalTranslation().subtract(flightDirection.mult(10f));
            camLocation.y = model.getWorldTranslation().y;
            simpleApplication.getCamera().setLocation(camLocation);
            simpleApplication.getCamera().lookAt(model.getWorldTranslation(), Vector3f.UNIT_Y);
            if (flightDirection.y < 0) {
                enginePitch = Math.min(enginePitch + 0.5f * tpf, 2);
            }
            if (flightDirection.y > 0) {
                enginePitch = Math.max(enginePitch - 0.5f * tpf, 0.9f);
            }
            engineAudio.setPitch(enginePitch);
            if (model.getWorldTranslation().y <= 0) {
                onCrash();
            }
        }
    }

    @Override
    public void onAction(String s, boolean keyPressed, float tpf) {
        if (s.equals("Up")) {
            up = keyPressed;
        }
        if (s.equals("Down")) {
            down = keyPressed;
        }
        if (s.equals("Left")) {
            left = keyPressed;
        }
        if (s.equals("Right")) {
            right = keyPressed;
        }

        if (s.equals("DBG")) {
            if (keyPressed) {
                System.out.println("waypoints.add(new Vector3f(" + simpleApplication.getCamera().getLocation().x + "f," + simpleApplication.getCamera().getLocation().y + "f," + simpleApplication.getCamera().getLocation().z + "f));");
            }
        }

    }

    private void onCrash() {
        if (enabled) {
            enabled = false;
            explosionAudio.playInstance();
            engineAudio.stop();
        }
    }


    public void collideWith(Spatial nodeB) {
        onCrash();
    }
}
