package com.alo.digital.transportes.webapp.efact.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface V_VariosMapper {

    public Map<String,Object> getVarios();

}
