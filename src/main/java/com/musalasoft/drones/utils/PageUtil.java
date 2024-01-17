package com.musalasoft.drones.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;


public interface PageUtil {

    static <T> Map<String, Object> buildPaginatedResponse(Page<T> object, Pageable pageable, long total, String key) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("total", total);
        pagination.put("size", object.getNumberOfElements());
        pagination.put("page", pageable.getPageNumber() + 1);
        pagination.put("totalPages", (int) Math.ceil((double) total / pageable.getPageSize()));

        Map<String, Object> data = new HashMap<>();
        data.put(key, object.getContent());
        data.put("pagination", pagination);
        return data;
    }

    static PageRequest buildPageRequest(Long page, Long size) {
        long defaultPage = (page != null) ? page : 1L;
        long defaultPageSize = (size != null) ? size : 20L;

        int pageNo = (int) Math.max(defaultPage - 1, 0);
        int pageSize = (defaultPageSize <= 0) ? 20 : (int) defaultPageSize;

        return PageRequest.of(pageNo, pageSize);
    }
}
