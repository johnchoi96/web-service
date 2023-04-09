package io.github.johnchoi96.webservice.models.petfinder;

import lombok.Data;

@Data
public class Pagination{
	private int countPerPage;
	private int totalCount;
	private int totalPages;
	private int currentPage;
}