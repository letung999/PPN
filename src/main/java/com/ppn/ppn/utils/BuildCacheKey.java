package com.ppn.ppn.utils;

public class BuildCacheKey {
    public static String cacheKeyAllData(String prefix, int page, int size) {
        StringBuilder cacheKey = new StringBuilder(prefix);
        cacheKey.append("-")
                .append(page)
                .append("-")
                .append(size);
        return cacheKey.toString();
    }
}
