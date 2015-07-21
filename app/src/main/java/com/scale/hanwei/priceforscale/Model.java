package com.scale.hanwei.priceforscale;

public class Model {

    private long id;
    private String model;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return model;
    }
}

