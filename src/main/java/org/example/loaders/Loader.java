package org.example.loaders;

import java.util.Map;

public interface Loader<T> {
    Map<Long,T> load();
    void clear();
}
