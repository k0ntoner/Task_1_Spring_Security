package org.example.interfaces;

import java.util.Map;

public interface Loader {
    Map<Integer,Model> load();
    void clear();
}
