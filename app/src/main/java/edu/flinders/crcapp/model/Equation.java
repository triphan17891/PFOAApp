/*
 *  Created by Tri Phan on 16/02/19 4:33 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 16/02/19 4:33 PM
 */

package edu.flinders.crcapp.model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import edu.flinders.crcapp.CRCApplication;
import edu.flinders.crcapp.injection.AppComponent;

public class Equation {
    public static final String SHARED_FILE_KEY = "mPreference";
    public static final String SHARED_OBJ_KEY = "mCalibration";
    private Calibration _calibObj;
    private Context _context;

    public Equation() {
        _context = CRCApplication.getContext();
        Calibration tmp = getSavedObjectFromPreference(_context, SHARED_FILE_KEY, SHARED_OBJ_KEY, Calibration.class);
        if(tmp == null) {
            // do nothing
            _calibObj = new Calibration();
        } else {
            _calibObj = tmp;
        }
    }

    public Calibration get_calibObj() {
        return _calibObj;
    }

    public void set_calibObj(Calibration _calibObj) {
        this._calibObj = _calibObj;
    }

    private float getX(int r, int g, int b) {
        float x = ((float) b / (2 * b - r - g));
        return x;
    }

    public void calibOneSample(int[] v0) {
        _calibObj.set_c2(getCalibC2(v0));
        saveObjectToSharedPreference(_context, SHARED_FILE_KEY, SHARED_OBJ_KEY, _calibObj);
    }

    public void calibTwoSamples(int[] v1, int[] v2) {
        _calibObj.set_c2(Calibration.C2);
        _calibObj.set_c3(getCalibC3(v1, v2));
        _calibObj.set_c1(getCalibC1(v2));
        saveObjectToSharedPreference(_context, SHARED_FILE_KEY, SHARED_OBJ_KEY, _calibObj);
    }

    public void calibThreeSamples(int[] v0, int[] v1, int[] v2) {
        _calibObj.set_c2(getCalibC2(v0));
        _calibObj.set_c3(getCalibC3(v1, v2));
        _calibObj.set_c1(getCalibC1(v2));
        saveObjectToSharedPreference(_context, SHARED_FILE_KEY, SHARED_OBJ_KEY, _calibObj);
    }

    public double getCalibC2(int[] v) {
        float x = getX(v[0], v[1], v[2]);
        return (float) (Math.ceil(x * 100) / 100);
    }

    public double getCalibC3(int[] v1, int[] v2) {
        float x1 = getX(v1[0], v1[1], v1[2]);
        float x2 = getX(v2[0], v2[1], v2[2]);
        double c2 = _calibObj.get_c2();
        float frac0 = (float) (30.15 * ((c2 - x1) / (c2 - x2)));
        double c3 = x1 - (x2 - x1) * frac0 / (1 - frac0);
        return (float) (Math.ceil(c3 * 100) / 100);
    }

    public double getCalibC1(int[] v) {
        float x = getX(v[0], v[1], v[2]);
        double c2 = _calibObj.get_c2();
        double c3 = _calibObj.get_c3();
        double c4 = _calibObj.get_c4();
        float frac0 = (float) Math.pow(((c2 - c3) / (x - c3) - 1), c4);
        double c1 = 500 / frac0;
        return (float) (Math.ceil(c1 * 100) / 100);
    }

    /**
     * @param r red
     * @param b blue
     * @param g green
     * @return
     * @throws Exception for eliminating the dividing zero
     */
    public float getFinalResult(int r, int g, int b) throws Exception {
        float x = getX(r, g, b);
        double c1 = _calibObj.get_c1();
        double c2 = _calibObj.get_c2();
        double c3 = _calibObj.get_c3();
        double c4 = _calibObj.get_c4();
        float c = (float) (c1 * Math.pow(((c2 - c3) / (x - c3) - 1), c4));
        return (float) (Math.ceil(c * 100) / 100);
    }

    /**
     * The function for saving object to SharedPreference by using Gson.
     * @param context current context (Application context is approved)
     * @param preferenceFileName unique name for the SharedPreference file
     * @param serializedObjectKey the object name (unique)
     * @param object the object
     */
    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    /**
     * Retrieve a object from SharedPreference file. The get for set function saveObjectToSharedPreference
     * @param context current context (Application context is approved)
     * @param preferenceFileName unique name for the SharedPreference file
     * @param preferenceKey the object name (unique)
     * @param classType the target object's file type
     * @return the object with data type is as classType
     */
    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }


}
