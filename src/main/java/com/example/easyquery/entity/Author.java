package com.example.easyquery.entity;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Navigate;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import com.example.easyquery.entity.proxy.AuthorProxy;

import java.util.List;


@Table("t_author")
@Data
@EntityProxy
@FieldNameConstants
public class Author implements ProxyEntityAvailable<Author , AuthorProxy> {

    @Column(primaryKey = true)
    private Long id;

    private String firstName;

    private String lastName;

    @Navigate(value = RelationTypeEnum.ManyToMany,
            selfProperty = {Book.Fields.storeId},
            targetProperty = {BookStore.Fields.id})
    private BookStore store;


    @Navigate(value = RelationTypeEnum.ManyToMany,
            mappingClass = BookAuthorMapping.class,
            selfProperty = Fields.id,
            selfMappingProperty = BookAuthorMapping.Fields.authorId,
            targetMappingProperty = BookAuthorMapping.Fields.bookId,
            targetProperty = Book.Fields.id)
    private List<Book> books;


}

