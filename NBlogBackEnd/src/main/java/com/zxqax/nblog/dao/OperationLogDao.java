package com.zxqax.nblog.dao;

import com.zxqax.nblog.entity.OperationLog;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogDao {

    /**
     * 插入日志
     * @param operationLog
     */
    void insert(OperationLog operationLog);
}
