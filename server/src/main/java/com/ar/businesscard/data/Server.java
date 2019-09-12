package com.ar.businesscard.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Server {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Brand")
    @Expose
    private String brand;
    @SerializedName("Model")
    @Expose
    private String model;
    @SerializedName("Serial")
    @Expose
    private String serial;
    @SerializedName("Processor")
    @Expose
    private String processor;
    @SerializedName("RAM")
    @Expose
    private String rAM;
    @SerializedName("Disks")
    @Expose
    private String disks;
    @SerializedName("Public IP")
    @Expose
    private String publicIP;
    @SerializedName("Private Network")
    @Expose
    private String privateNetwork;
    @SerializedName("Remote Management")
    @Expose
    private String remoteManagement;
    @SerializedName("Hardware RAID")
    @Expose
    private String hardwareRAID;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getRAM() {
        return rAM;
    }

    public void setRAM(String rAM) {
        this.rAM = rAM;
    }

    public String getDisks() {
        return disks;
    }

    public void setDisks(String disks) {
        this.disks = disks;
    }

    public String getPublicIP() {
        return publicIP;
    }

    public void setPublicIP(String publicIP) {
        this.publicIP = publicIP;
    }

    public String getPrivateNetwork() {
        return privateNetwork;
    }

    public void setPrivateNetwork(String privateNetwork) {
        this.privateNetwork = privateNetwork;
    }

    public String getRemoteManagement() {
        return remoteManagement;
    }

    public void setRemoteManagement(String remoteManagement) {
        this.remoteManagement = remoteManagement;
    }

    public String getHardwareRAID() {
        return hardwareRAID;
    }

    public void setHardwareRAID(String hardwareRAID) {
        this.hardwareRAID = hardwareRAID;
    }
}
