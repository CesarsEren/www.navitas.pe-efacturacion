package com.alo.digital.transportes.webapp.efact.util;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class HeaderHandlerResolver implements HandlerResolver {

	@SuppressWarnings("rawtypes")
	@Override
	public List<Handler> getHandlerChain(PortInfo arg0) {
		 List<Handler> handlerChain = new ArrayList<Handler>();
	        HeaderHandler hh = new HeaderHandler();
	        handlerChain.add(hh);
	        return handlerChain;
	}

}
