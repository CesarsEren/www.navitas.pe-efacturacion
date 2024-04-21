package com.alo.digital.transportes.webapp.efact.util;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {
	
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty.booleanValue()) {
            SOAPMessage message = smc.getMessage();
            try {

                SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();

                SOAPHeader header = envelope.getHeader();
                
                
                if(header == null) {
                	
                	header = envelope.addHeader();
                }
                
                
                SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
                SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
                //usernameToken.addAttribute(new QName("xmlns:wsu"), "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
                SOAPElement usernameElement = usernameToken.addChildElement("Username", "wsse");
                
                usernameElement.addTextNode(Utils.USERNAME);

                SOAPElement passwordElement = usernameToken.addChildElement("Password", "wsse");
                passwordElement.addTextNode(Utils.PASSWORD);
                
                message.saveChanges();
                
                //message.writeTo(System.out);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return outboundProperty;
	}

	@Override
	public void close(MessageContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleFault(SOAPMessageContext arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
