package com.shapira.collectorscanner.model;

public class PackageScan {
    int package_id;
    int pallet_id;
    int user_id;

    public PackageScan(int package_id, int pallet_id, int user_id) {
        this.package_id = package_id;
        this.pallet_id = pallet_id;
        this.user_id = user_id;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }

    public int getPallet_id() {
        return pallet_id;
    }

    public void setPallet_id(int pallet_id) {
        this.pallet_id = pallet_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
