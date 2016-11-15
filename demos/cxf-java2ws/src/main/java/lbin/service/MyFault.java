package lbin.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "MyFault", namespace = "http://service.lbin/faults")
@XmlAccessorType(XmlAccessType.FIELD)
public class MyFault extends Exception {

	@XmlElement(namespace = "http://service.lbin/faults", name="msg")
	public String msg;
}
