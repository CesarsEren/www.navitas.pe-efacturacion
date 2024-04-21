package com.alo.digital.transportes.webapp.efact.dao;

import com.alo.digital.transportes.webapp.efact.beans.V_ClientesBean;
import com.alo.digital.transportes.webapp.efact.mapper.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteIDAO implements ClienteDAO {

	@Autowired
	ClienteMapper clienteMapper;
	
	@Override
	public V_ClientesBean listClientesRUC(String rUC) {
		// TODO Auto-generated method stub
		return clienteMapper.listClientesRUC(rUC);
	}

}
