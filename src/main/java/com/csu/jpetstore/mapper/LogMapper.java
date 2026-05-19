package com.csu.jpetstore.mapper;

import com.csu.jpetstore.domain.LogData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    boolean insertLog(LogData logData);
}
