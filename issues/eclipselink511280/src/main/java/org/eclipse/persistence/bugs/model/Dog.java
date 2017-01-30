package org.eclipse.persistence.bugs.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Dog implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	public String name;

	@Column
	public LocalDate birthdate;

	@Column
	public LocalDate died;

	@Override public String toString() {
		return "Dog{" +
			"id=" + id +
			", name='" + name + '\'' +
			", birthdate=" + birthdate +
			", died=" + died +
			'}';
	}
}
