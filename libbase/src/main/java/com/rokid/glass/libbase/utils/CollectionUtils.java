package com.rokid.glass.libbase.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    private CollectionUtils() {}

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection) {
        return null != collection && !collection.isEmpty();
    }

    public static Collection retainAll(Collection collection, Collection retain) {
        List list = new ArrayList(Math.min(collection.size(), retain.size()));

        for (Object obj : collection) {
            if (retain.contains(obj)) {
                list.add(obj);
            }
        }

        return list;
    }
}
