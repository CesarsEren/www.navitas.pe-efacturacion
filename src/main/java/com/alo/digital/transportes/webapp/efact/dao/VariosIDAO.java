package com.alo.digital.transportes.webapp.efact.dao;

import com.alo.digital.transportes.webapp.efact.mapper.V_VariosMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VariosIDAO implements VariosDao {

    @Autowired
    V_VariosMapper v_variosMapper;

    @Override
    public Map<String, Object> getVarios() {
        return v_variosMapper.getVarios();
    }
}
