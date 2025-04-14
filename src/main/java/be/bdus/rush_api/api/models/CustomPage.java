package be.bdus.rush_api.api.models;

import java.util.List;

public record CustomPage<T>(
        List<T> results,
        int totalPages,
        int currentPage
) {

}