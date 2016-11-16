package lbin.service;

import javax.jws.WebService;

@WebService(targetNamespace = "http://service.lbin/service", name = "MyService")
public interface MyService {

	MyResponse myOperation(MyRequest request) throws OtherFault, MyFault;
}
