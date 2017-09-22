package com.diyiliu.model;

/**
 * Description: TableSchema
 * Author: DIYILIU
 * Update: 2017-09-22 14:06
 */
public class TableSchema {
    private String tableName;
    private String tableContent;
    private String indexName;
    private String indexContent;

    public TableSchema() {
    }

    public TableSchema(String tableName, String tableContent, String indexName, String indexContent) {
        this.tableName = tableName;
        this.tableContent = tableContent;
        this.indexName = indexName;
        this.indexContent = indexContent;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableContent() {
        return tableContent;
    }

    public void setTableContent(String tableContent) {
        this.tableContent = tableContent;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexContent() {
        return indexContent;
    }

    public void setIndexContent(String indexContent) {
        this.indexContent = indexContent;
    }
}
