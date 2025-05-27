package com.example.easyquery.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
public class BookAuthorMapping {
    private Long bookId;
    private Long authorId;
}
