package lbin.metheck;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class MethodCheckerMain {

	public static void main(String[] args) {
		SpringApplication.run(MethodCheckerMain.class, args);
	}

	@RequestMapping(value = "/all")
	public String all() {
		return "OK-all";
	}

	@RequestMapping(value = "/get", method = GET)
	public String get(@RequestParam(name = "zipcode") String zipcode) {
		return "OK-get" + zipcode;
	}
}
