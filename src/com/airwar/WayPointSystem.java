package com.airwar;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 * Created by Zuegg on 26.05.2014.
 */
public class WayPointSystem {
    static ArrayList<Vector3f> waypoints=new ArrayList<>();
    SimpleApplication simpleApplication;

    public WayPointSystem(SimpleApplication simpleApplication) {
        this.simpleApplication = simpleApplication;
    }

    public void load(){

    }

    public void reset(){

    }
}
