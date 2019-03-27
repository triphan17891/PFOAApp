/*
 *  Created by Tri Phan on 16/02/19 4:31 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 16/02/19 4:31 PM
 */

package edu.flinders.crcapp.model;

public class Calibration {
    public static final double C1 = 258.7;
    public static final double C2 = 0.7171;
    public static final double C3 = 0.3532;
    public static final double C4 = 0.8868;

    private double _c1;
    private double _c2;
    private double _c3;
    private double _c4;

    public Calibration() {
        _c1 = C1;
        _c2 = C2;
        _c3 = C3;
        _c4 = C4;
    }

    public double get_c1() {
        return _c1;
    }

    public void set_c1(double _c1) {
        this._c1 = _c1;
    }

    public double get_c2() {
        return _c2;
    }

    public void set_c2(double _c2) {
        this._c2 = _c2;
    }

    public double get_c3() {
        return _c3;
    }

    public void set_c3(double _c3) {
        this._c3 = _c3;
    }

    public double get_c4() {
        return _c4;
    }

    public void set_c4(double _c4) {
        this._c4 = _c4;
    }
}
