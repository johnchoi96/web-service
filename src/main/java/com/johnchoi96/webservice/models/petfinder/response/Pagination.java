package com.johnchoi96.webservice.models.petfinder.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination {
    private int countPerPage;

    private int totalCount;

    private int totalPages;

    private int currentPage;
}