package com.ppn.ppn.utils;

import com.ppn.ppn.payload.SearchUserRequest;

public class BuildCacheKey {
    public static String cacheKeyAllData(String prefix, int page, int size) {
        StringBuilder cacheKey = new StringBuilder(prefix);
        cacheKey.append("-")
                .append(page)
                .append("-")
                .append(size);
        return cacheKey.toString();
    }

    public static String buildCacheKeySearchUsers(String prefix, SearchUserRequest request) {
        StringBuilder stringBuilder = new StringBuilder(prefix);
        stringBuilder.append("-");
        if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
            stringBuilder.append("-").append(request.getFirstName());
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            stringBuilder.append("-").append(request.getEmail());
        }

        if (request.getGender() != null && !request.getGender().isEmpty()) {
            stringBuilder.append("-").append(request.getGender());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            stringBuilder.append("-").append(request.getPhoneNumber());
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            stringBuilder.append("-").append(request.getStatus());
        }

        stringBuilder.append("-").append(request.getPageIndex());
        stringBuilder.append("-").append(request.getPageSize());
        stringBuilder.append("-").append(request.isAscending());

        if (request.getSortBy() != null && !request.getSortBy().isEmpty()) {
            stringBuilder.append("-").append(request.getSortBy());
        }
        return stringBuilder.toString();
    }
}
