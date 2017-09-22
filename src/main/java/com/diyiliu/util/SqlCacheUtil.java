package com.diyiliu.util;

import com.diyiliu.cache.ICache;
import com.diyiliu.model.TableSchema;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Description: SqlCacheUtil
 * Author: DIYILIU
 * Update: 2017-09-22 14:26
 */


public class SqlCacheUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String SQL_FILE = "sql.xml";

    @Resource
    private ICache sqlCacheProvider;

    public void init(){

        InputStream is = null;
        try {
            is = new ClassPathResource(SQL_FILE).getInputStream();
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(is);

            List<Node> sqlList = document.selectNodes("root/sql");
            for (Node sqlNode : sqlList) {
                //String id = sqlNode.valueOf("@id");
                String name = sqlNode.valueOf("@name");
                String type = sqlNode.valueOf("@type");

                String content = sqlNode.getText().trim();
                if ("table".equalsIgnoreCase(type)){
                    TableSchema tableSchema = new TableSchema();
                    tableSchema.setTableName(name);
                    tableSchema.setTableContent(content);

                    sqlCacheProvider.put(name, tableSchema);
                }else if ("index".equalsIgnoreCase(type)){
                    String table = sqlNode.valueOf("@table");
                    if (sqlCacheProvider.containsKey(table)){
                        TableSchema tableSchema = (TableSchema) sqlCacheProvider.get(table);
                        tableSchema.setIndexName(name);
                        tableSchema.setIndexContent(content);
                    }else {
                        logger.warn("需要创建的索引，无法找到相对应的table[{}]!", table);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
