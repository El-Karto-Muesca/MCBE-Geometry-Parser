package me.k128.mcGeoParser.utilities;

import java.util.ArrayList;
import java.util.List;

abstract class Vector<T extends Number> {
    private final List<T> vals;

    protected Vector(int dimensionsCount) {
        this.vals = new ArrayList<>(dimensionsCount);
    }

    protected T get(int i) {
        return this.vals.get(i);
    }

    protected void set(int i, T val) {
        this.vals.set(i, val);
    }
}