package com.nti.socialmediaappcore.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDTO<T> {
    private List<T> items;
    private int currentPage;
    private long totalItems;
    private int totalPages;

    public PageDTO(Page<T> page) {
        this.items = page.getContent();
        this.currentPage = page.getNumber();
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
