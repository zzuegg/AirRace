package com.airwar;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;

/**
 * Created by Zuegg on 26.05.2014.
 */
public class Startup extends SimpleApplication implements PhysicsCollisionListener {
    public static void main(String args[]) {
        Startup startup = new Startup();
        startup.start();
    }


    Ambient ambient;

    Building building;

    Airplane airplane;

    BulletAppState bulletAppState;

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
    }


    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        airplane.update(tpf);
    }

    @Override
    public void collision(PhysicsCollisionEvent physicsCollisionEvent) {
        System.out.println("Collision between: " + physicsCollisionEvent.getNodeA().getName() + ":" + physicsCollisionEvent.getNodeB().getName());
        if(physicsCollisionEvent.getNodeA().getName().equals("Airplane")){
            airplane.collideWith(physicsCollisionEvent.getNodeB());
        }
        if(physicsCollisionEvent.getNodeB().getName().equals("Airplane")){
            airplane.collideWith(physicsCollisionEvent.getNodeA());
        }
    }
}
