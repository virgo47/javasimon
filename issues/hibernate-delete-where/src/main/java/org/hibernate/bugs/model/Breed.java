package org.hibernate.bugs.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Breed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Breed{" +
			"id=" + id +
			", name='" + name + '\'' +
			'}';
	}
}
