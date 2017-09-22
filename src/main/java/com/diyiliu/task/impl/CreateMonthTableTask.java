package com.diyiliu.task.impl;

import com.diyiliu.cache.ICache;
import com.diyiliu.model.TableSchema;
import com.diyiliu.task.ITask;
import com.diyiliu.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Description: CreateMonthTableTask
 * Author: DIYILIU
 * Update: 2017-09-22 14:01
 */
public class CreateMonthTableTask implements ITask{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private ICache sqlCacheProvider;

    // 需要提前创建几个月的表（默认提前创建1个月）
    private final static int NEXT_TO = 3;

    public void execute(){
        logger.info("检测是否需要创建月表...");

        for (int i = 0; i <= NEXT_TO; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, i);
            Date date = calendar.getTime();
            String monthTable = DateUtil.dateToString(date, "%1$tY%1$tm");

            Set keys = sqlCacheProvider.getKeys();
            for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
                String name = (String) iterator.next();
                TableSchema schema = (TableSchema) sqlCacheProvider.get(name);

                // 创建月表
                if (!existTable(schema, monthTable)) {
                    createTable(schema, monthTable);
                }

                // 创建索引
                if (!existIndex(schema, monthTable)) {
                    createIndex(schema, monthTable);
                }
            }
        }
    }


    private void createTable(TableSchema schema, String monthTable) {
        String sql = schema.getTableContent().replaceAll("\\?", monthTable);

        logger.info("创建月表：{}, SQL[{}]", schema.getTableName() + monthTable, sql);

        jdbcTemplate.execute(sql);
    }

    private void createIndex(TableSchema schema, String monthTable) {

        String sql = schema.getIndexContent().replaceAll("\\?", monthTable);

        logger.info("创建索引：{}, SQL[{}]", schema.getIndexName() + monthTable, sql);

        jdbcTemplate.execute(sql);
    }

    private boolean existTable(TableSchema schema, String monthTable) {
        String sql = "SELECT t.table_name FROM user_tables t WHERE t.table_name = '" + schema.getTableName() + monthTable +"'";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        if (rowSet.next()) {

            return true;
        }

        return false;
    }

    private boolean existIndex(TableSchema schema, String monthTable) {
        String sql = "SELECT t.index_name FROM user_indexes t WHERE t.index_name = '" + schema.getIndexName() + monthTable +"'";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        if (rowSet.next()) {

            return true;
        }

        return false;
    }
}
