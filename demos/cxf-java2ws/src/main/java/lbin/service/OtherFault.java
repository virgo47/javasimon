package lbin.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "OtherFault", namespace = "http://service.lbin/faults")
@XmlAccessorType(XmlAccessType.FIELD)
public class OtherFault extends Exception {
}
