package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pallet {



        @SerializedName("id")
        private int id;

        @SerializedName("branch_id")
        private int branchId;

        @SerializedName("branch")
        private Branch branch;

        @SerializedName("packages")
        private List<Package> packages;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int setBranchId() {
        return branchId;
    }

    public void getBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Branch getBranch() {
        return branch;
    }
    public List<Package> getPackages() {
        return packages;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
