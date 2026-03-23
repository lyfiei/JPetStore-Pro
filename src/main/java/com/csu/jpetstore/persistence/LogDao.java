package com.csu.jpetstore.persistence;


import csu.web.mypetstore.domain.LogData;

public interface LogDao {
    boolean insertLog(LogData logData);
}
