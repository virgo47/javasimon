package org.hibernate.bugs.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Dog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@ManyToOne
	@JoinColumn(name = "breed_id")
	private Breed breed;

	@Override
	public String toString() {
		return "Dog{" +
			"id=" + id +
			", name='" + name + '\'' +
			", breed.id=" + (breed != null ? breed.getId() : null) +
			'}';
	}
}
