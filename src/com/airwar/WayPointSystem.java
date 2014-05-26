package com.airwar;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;

/**
 * Created by Zuegg on 26.05.2014.
 */
public class WayPointSystem {
    static ArrayList<Vector3f> waypoints = new ArrayList<>();

    static {
        waypoints.add(new Vector3f(-78.06709f, 6.615792f, -86.50637f));
        waypoints.add(new Vector3f(-102.46819f, 6.1111646f, -42.701942f));
        waypoints.add(new Vector3f(-128.69623f, 4.8251805f, 2.8716567f));
        waypoints.add(new Vector3f(-131.10683f, 7.270331f, 52.181503f));
        waypoints.add(new Vector3f(-130.8407f, 6.3499646f, 108.11413f));
        waypoints.add(new Vector3f(-87.36998f, 7.7767415f, 129.0589f));
        waypoints.add(new Vector3f(-47.915585f, 5.8330903f, 130.68163f));
        waypoints.add(new Vector3f(7.272965f, 7.5818667f, 130.95203f));
        waypoints.add(new Vector3f(55.652596f, 6.3350425f, 131.03952f));
        waypoints.add(new Vector3f(99.667915f, 6.532876f, 132.8951f));
        waypoints.add(new Vector3f(126.29663f, 32.322052f, 64.80164f));
        waypoints.add(new Vector3f(126.51359f, 60.741837f, -19.92523f));
        waypoints.add(new Vector3f(128.2222f, 95.85976f, -69.3834f));
        waypoints.add(new Vector3f(122.45856f, 136.73079f, -153.6605f));
        waypoints.add(new Vector3f(48.53328f, 118.51721f, -75.9064f));
        waypoints.add(new Vector3f(23.509525f, 119.05561f, -46.123158f));
        waypoints.add(new Vector3f(52.271988f, 118.8762f, -25.989313f));
        waypoints.add(new Vector3f(84.088f, 118.71786f, -53.83668f));
        waypoints.add(new Vector3f(101.370575f, 122.9942f, 2.2097597f));
        waypoints.add(new Vector3f(34.15147f, 88.20674f, 55.965466f));
        waypoints.add(new Vector3f(80.36121f, 90.31354f, 52.326977f));
        waypoints.add(new Vector3f(86.88131f, 68.604675f, 111.19397f));
        waypoints.add(new Vector3f(20.106283f, 69.15721f, 108.74169f));
        waypoints.add(new Vector3f(-49.43063f, 65.48496f, 100.51474f));
        waypoints.add(new Vector3f(-81.01222f, 62.86649f, 73.856606f));
        waypoints.add(new Vector3f(-28.456158f, 63.196976f, 51.51326f));
        waypoints.add(new Vector3f(-7.547157f, 91.74756f, -32.577633f));
        waypoints.add(new Vector3f(-7.456325f, 92.08772f, -81.77771f));
        waypoints.add(new Vector3f(21.419344f, 94.01394f, -106.389336f));
        waypoints.add(new Vector3f(73.59148f, 94.4512f, -108.88769f));
    }

    SimpleApplication simpleApplication;
    ArrayList<Spatial> waypointAvailable = new ArrayList<>();
    BitmapText bitmapText;

    public WayPointSystem(SimpleApplication simpleApplication, BitmapText bitmapText) {
        this.simpleApplication = simpleApplication;
        this.bitmapText = bitmapText;
    }

    public void updateText() {
        bitmapText.setText("Collected " + (waypoints.size() - waypointAvailable.size() + " of " + waypoints.size()));
    }

    public void load() {

        reset();

    }

    public void reset() {
        for (Spatial spatial : waypointAvailable) {
            spatial.removeFromParent();
            simpleApplication.getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(spatial.getControl(RigidBodyControl.class));
        }
        waypointAvailable.clear();
        Material waypointMat = new Material(simpleApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        waypointMat.setColor("Color", ColorRGBA.Blue);
        for (Vector3f waypoint : waypoints) {
            Geometry geometry = new Geometry("WayPoint", new Sphere(4, 4, 4f));
            geometry.setMaterial(waypointMat);
            RigidBodyControl rigidBodyControl = new RigidBodyControl(CollisionShapeFactory.createBoxShape(geometry));
            geometry.addControl(rigidBodyControl);
            rigidBodyControl.setMass(0);
            rigidBodyControl.setPhysicsLocation(waypoint);
            simpleApplication.getStateManager().getState(BulletAppState.class).getPhysicsSpace().add(rigidBodyControl);
            geometry.setName("Waypoint");
            simpleApplication.getRootNode().attachChild(geometry);
            waypointAvailable.add(geometry);
        }
        updateText();
    }


    public void removeWaypoint(Spatial waypoint) {
        simpleApplication.getStateManager().getState(BulletAppState.class).getPhysicsSpace().remove(waypoint.getControl(RigidBodyControl.class));
        waypoint.removeFromParent();
        waypointAvailable.remove(waypoint);
        updateText();
    }
}
