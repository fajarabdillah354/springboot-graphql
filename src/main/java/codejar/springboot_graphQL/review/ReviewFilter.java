package codejar.springboot_graphQL.review;

public record ReviewFilter(
    Integer rating,
    Boolean verified,
    String reviewerName
) {}
