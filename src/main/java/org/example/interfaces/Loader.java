package org.example.interfaces;

import java.util.Map;

public interface Loader<T> {
    Map<Integer,T> load(String filePath);
}
