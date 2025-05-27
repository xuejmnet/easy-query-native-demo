package com.example.easyquery.entity;


import com.easy.query.core.annotation.Column;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Navigate;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import com.example.easyquery.entity.proxy.TreeNodeProxy;

import java.util.List;

@Data
@EntityProxy
@FieldNameConstants
@Table("t_tree_node")
public class TreeNode implements ProxyEntityAvailable<TreeNode , TreeNodeProxy> {

    @Column(primaryKey = true)
    private Long id;


    private String name;

    private Long parentId;

    @Navigate(value = RelationTypeEnum.ManyToOne,targetProperty = "id")
    private TreeNode parent;


    @Navigate(value = RelationTypeEnum.OneToMany,targetProperty = "parentId")
    private List<TreeNode> childNodes;
}