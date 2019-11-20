package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;

public class WhatToDo {

    String queuedSound = "";


    public class Results {
        void Reset() {
            strafe = 0;
            forward = 0;
            grab = false;
        }

        double strafe;
        double forward;
        double distance;
        boolean grab;

        void CopyFrom(Results other) {
            strafe = other.strafe;
            forward = other.forward;
            grab = other.grab;
        }

        boolean IsSame(Results other) {
            if (other.strafe != strafe)
                return false;
            if (other.forward != forward)
                return false;
            if (other.grab != grab)
                return false;

            return true;
        }

        void DebugWithSound() {
            if (grab)
                queuedSound = "loot";
            else if (forward != 0) {
                if (forward < .4) {
                    queuedSound = "forward_slowly";
                } else {
                    queuedSound = "move_forward";
                }
            } else if (strafe != 0) {
                if (strafe < 0) {
                    queuedSound = "strafe_left";
                }
                if (strafe > 0) {
                    queuedSound = "strafe_right";
                }
            } else {
                queuedSound = "searching";
            }
        }
    }

    public Results results = new Results();
    public Results lastResults = new Results();

    void ProcessVision(double left, double right, double top, double bottom, double confidence)
    {

        results.Reset();
        if (confidence > .5)
        {
            double middleAvg = (right + left) / 2.0;

            results.strafe = CalcStrafe(middleAvg);

            double height = top - bottom;

            results.distance = CalcDist(height);
            if (results.strafe == 0) {
                results.forward = CalcForward(height);

                if (results.forward == 0) {
                    results.grab = true;
                }
            }
        }

        if (!results.IsSame(lastResults))
        {
            lastResults.CopyFrom(results);
            results.DebugWithSound();
        }
    }


    double CalcDist(double height)
    {
        double distance = .27/height;
                return distance;
    }
    double ReturnCent(double distance)
    {
        return .5;
    }
    private double CalcForward(double height)
    {

        double speed = 0;
        if (height  > .8)
            speed = 0;
        else if (height > .6)
            speed = .2;
        else if (height <= .6)
            speed = .5;

        return speed;

    }
    private double CalcStrafe(double middleAvg)
    {
        double strafeSpeed = 0.0;
        if (middleAvg < .4)
        {
            strafeSpeed = -.5;
        }
        else if (middleAvg > .6)
        {
            strafeSpeed = .5;
        }
        else {
            strafeSpeed = 0.0;
        }
        return strafeSpeed;
    }

}
