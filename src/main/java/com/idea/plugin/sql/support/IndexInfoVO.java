package com.idea.plugin.sql.support;


public class IndexInfoVO {
    public String indexName;
    public String indexColumnName;

    public static IndexInfoVO builder() {
        return new IndexInfoVO();
    }

    public IndexInfoVO indexName(String indexName) {
        this.indexName = indexName;
        return this;
    }

    public IndexInfoVO indexColumnName(String indexColumnName) {
        this.indexColumnName = indexColumnName;
        return this;
    }
}
