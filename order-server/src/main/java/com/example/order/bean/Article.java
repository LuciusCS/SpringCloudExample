package com.example.order.bean;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 全文搜索的实现
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "article")  // 指定索引名，索引的是哪一个字段
public class Article {

    @Id
    private String id;
    @Field(type = FieldType.Keyword) // 需明确字段类型:ml-citation{ref="3" data="citationList"}
    private String title;
    @Field(type = FieldType.Text,index = false)  // 需明确字段类型:ml-citation{ref="3" data="citationList"}
    private String content;
    @Field(type = FieldType.Text)  // 需明确字段类型:ml-citation{ref="3" data="citationList"}
    private String category;
}
