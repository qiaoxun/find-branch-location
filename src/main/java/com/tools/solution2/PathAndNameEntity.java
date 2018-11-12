package com.tools.solution2;

public class PathAndNameEntity {
    private String path;
    private String branchName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return "PathAndNameEntity{" +
                "path='" + path + '\'' +
                ", branchName='" + branchName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + path.hashCode();
        result = 31 * result + branchName.hashCode();
        return result;
    }
}
