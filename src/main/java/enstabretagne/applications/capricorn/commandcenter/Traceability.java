package enstabretagne.applications.capricorn.commandcenter;

import enstabretagne.base.logger.Logger;
import enstabretagne.engine.InitData;

import java.util.ArrayList;
import java.util.List;

public class Traceability {

    private int mobileNumber;
    private double mobileSpeed;
    private int interceptedMobiles;
    private int launchedMissiles;
    private int factoryDamaged;
    private int missileFailure;
    private int mobileFailure;
    private List<Integer> interceptionDistance;
    private InitData ini;


    protected Traceability(InitData ini){
        this.ini = ini;
        this.mobileNumber = 0;
        this.mobileSpeed = 0;
        this.interceptedMobiles = 0;
        this.launchedMissiles = 0;
        this.factoryDamaged = 0;
        this.missileFailure = 0;
        this.mobileFailure = 0;
        this.interceptionDistance = new ArrayList<>();
    }

    public void setMobileNumber(int mobileNumber){
        this.mobileNumber = mobileNumber;
    }

    public void setMobileSpeed(double mobileSpeed){
        this.mobileSpeed = mobileSpeed;
    }

    public void incrementInterceptedMobiles(){
        this.interceptedMobiles++;
    }

    public void incrementLaunchedMissiles(){
        this.launchedMissiles++;
    }

    public void incrementFactoryDamages(){
        this.factoryDamaged++;
    }

    public void incrementMissileFailure(){
        this.missileFailure++;
    }

    public void incrementMobileFailure(){
        this.mobileFailure++;
    }

    public void addDistance(int distance){
        this.interceptionDistance.add(distance);
    }

    /**
     * Traces the details of
     */
    public void traceIt(){
        this.mobileFailure = this.mobileNumber - ( this.factoryDamaged + this.interceptedMobiles);

        StringBuilder sb = new StringBuilder();
        int l_list = this.interceptionDistance.size();
        for (int i = 0; i < l_list; i++){
            sb.append(this.interceptionDistance.get(i));
            if (i != (l_list - 1)){
                sb.append(",");
            }
        }
        Logger.Detail(this, this.ini.getName(), this.mobileNumber + "," + this.mobileSpeed + ","
            + this.launchedMissiles + "," + this.factoryDamaged + "," + this.missileFailure + ","
                + this.mobileFailure + "," + this.interceptedMobiles + "," + sb);
    }

}
