package com.example.easyquery;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.proxy.core.draft.Draft2;
import com.easy.query.core.proxy.sql.GroupKeys;
import com.easy.query.core.proxy.sql.Select;
import com.example.easyquery.dto.BookDTO;
import com.example.easyquery.dto.BookInput;
import com.example.easyquery.entity.Book;
import com.example.easyquery.entity.BookStore;
import com.example.easyquery.entity.TreeNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class DemoController {


    @Autowired
    private EasyEntityQuery entityQuery;

    @RequestMapping("/tree")
    List<TreeNode> tree() {
        List<TreeNode> treeList = entityQuery.queryable(TreeNode.class)
                .where(m -> {
                    m.parentId().isNull();
                })
                .asTreeCTE()
                .toTreeList();
        return treeList;
    }

    @RequestMapping("/group")
    List<Draft2<Long, BigDecimal>> group() {
        List<Draft2<Long, BigDecimal>> result = entityQuery.queryable(Book.class)
                .groupBy(b -> GroupKeys.of(b.storeId()))
                .select(g -> Select.DRAFT.of(
                        g.groupTable().storeId(),
                        g.groupTable().price().avg()
                ))
                .toList();

        return result;
    }


    @RequestMapping("/outputDto")
    List<BookDTO> outputDto() {
        List<BookDTO> books = entityQuery.queryable(Book.class)
                .selectAutoInclude(BookDTO.class).toList();
        return books;
    }

    @RequestMapping("/orderBySubQuery")
    List<BookStore> orderBySubQuery() {
        List<BookStore> list = entityQuery.queryable(BookStore.class)
                .orderBy(
                        store -> store.books().first().price().desc()
                ).select(BookStore.class).toList();

        return list;
    }

    @RequestMapping("add")
    public void add(@RequestBody BookInput bookInput) {
        Book book = new Book();
        BeanUtils.copyProperties(bookInput, book);
        entityQuery.insertable(book).executeRows();
    }

}
