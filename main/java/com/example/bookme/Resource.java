// File: Resource.java
package com.example.bookme;

public class Resource {
    private String resourcesID;
    private String resourcesName;
    private String resourcesType;
    private String availability;

    public Resource() {
        // Default constructor required for calls to DataSnapshot.getValue(Resource.class)
    }

    public Resource(String resourcesID, String resourcesName, String resourcesType, String availability) {
        this.resourcesID = resourcesID;
        this.resourcesName = resourcesName;
        this.resourcesType = resourcesType;
        this.availability = availability;
    }

    public String getResourcesID() {
        return resourcesID;
    }

    public void setResourcesID(String resourcesID) {
        this.resourcesID = resourcesID;
    }

    public String getResourcesName() {
        return resourcesName;
    }

    public void setResourcesName(String resourcesName) {
        this.resourcesName = resourcesName;
    }

    public String getResourcesType() {
        return resourcesType;
    }

    public void setResourcesType(String resourcesType) {
        this.resourcesType = resourcesType;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
