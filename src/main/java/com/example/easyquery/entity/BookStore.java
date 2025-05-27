package com.example.easyquery.entity;

import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Navigate;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import com.example.easyquery.entity.proxy.BookStoreProxy;

import java.util.List;

@Data
@Table("t_book_store")
@EntityProxy
@FieldNameConstants
public class BookStore implements ProxyEntityAvailable<BookStore , BookStoreProxy> {

    @Column(primaryKey = true)
    private Long id;
    private String name;
    private String website;


    @Navigate(value = RelationTypeEnum.OneToMany,
            selfProperty = {Fields.id},
            targetProperty = {Book.Fields.storeId})
    private List<Book> books;

}
