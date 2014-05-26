package com.airwar;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 * Created by Zuegg on 27.05.2014.
 */
public class CoinControl extends AbstractControl {
    @Override
    protected void controlUpdate(float v) {
        this.getSpatial().rotate(new Quaternion().fromAngleAxis(1 * v, Vector3f.UNIT_Y));
    }

    @Override
    protected void controlRender(RenderManager renderManager, ViewPort viewPort) {

    }
}
