package com.csu.jpetstore.persistence;


import com.csu.jpetstore.domain.LogData;

public interface LogDao {
    boolean insertLog(LogData logData);
}
