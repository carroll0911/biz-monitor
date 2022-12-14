package com.carroll.biz.monitor.analyzer.utils;

import com.carroll.biz.monitor.common.request.PageRequest;
import com.carroll.biz.monitor.common.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created by tangbin on 17/6/6.
 */
public class PageUtil {

    private PageUtil() {
    }

    public static Pageable convertPageRequestToPageable(PageRequest pageRequest) {
        return new org.springframework.data.domain.PageRequest(pageRequest.getCurPage(), pageRequest.getPageSize());
    }

    public static Pageable convertPageRequestToPageable(PageRequest pageRequest, Sort sort) {
        return new org.springframework.data.domain.PageRequest(pageRequest.getCurPage(), pageRequest.getPageSize(), sort);
    }

    public static <T extends PageResponse> T convertPageToPageResponse(Page<?> page, T response) {
        if (null == page) {
            return response;
        }
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setNumber(page.getNumber());
        response.setNumberOfElements(page.getNumberOfElements());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        response.setSize(page.getSize());
        return response;
    }
}
