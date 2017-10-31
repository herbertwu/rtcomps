package com.rtcomps.core.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XMLUnmarshaller<T> {

	public T unmarshallXMLPage(String xmlToUnmarshall, Class<T> clazz) {
		try {
			Unmarshaller unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();
			Object obj = unmarshaller.unmarshal(new StringReader(xmlToUnmarshall));
			return clazz.cast(obj);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}
