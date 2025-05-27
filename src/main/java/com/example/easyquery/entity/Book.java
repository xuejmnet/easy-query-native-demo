package com.example.easyquery.entity;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Navigate;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import com.example.easyquery.entity.proxy.BookProxy;

import java.math.BigDecimal;
import java.util.List;


@Data
@Table("t_book")
@EntityProxy
@FieldNameConstants
public class Book implements ProxyEntityAvailable<Book , BookProxy> {

    @Column(primaryKey = true)
    private Long id;

    private String name;

    private Integer edition;

    private BigDecimal price;

    private Long storeId;


    @Navigate(value = RelationTypeEnum.ManyToOne,
            selfProperty = {Fields.storeId},
            targetProperty = {BookStore.Fields.id})
    private BookStore store;

    @Navigate(value = RelationTypeEnum.ManyToMany,
            mappingClass = BookAuthorMapping.class,
            selfProperty = Fields.id,
            selfMappingProperty = BookAuthorMapping.Fields.bookId,
            targetMappingProperty = BookAuthorMapping.Fields.authorId,
            targetProperty = Author.Fields.id)
    private List<Author> authors;

}
