package com.shapira.collectorscanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pallet {



        @SerializedName("id")
        private int id;

        @SerializedName("branch_id")
        private int branchId;

        @SerializedName("organization_id")
        private int organziationId;
        @SerializedName("branch")
        private Branch branch;
    @SerializedName("organizaion")
    private Organization organizaion;
        @SerializedName("packages")
        private List<Package> packages;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrganziationId() {
        return branchId;
    }

    public void setOrganziationId(int organziationId) {
        this.organziationId = organziationId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Branch getBranch() {
        return branch;
    }
    public Organization getOrganziation() {
        return organizaion;
    }
    public List<Package> getPackages() {
        return packages;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
}
