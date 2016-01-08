package org.eclipse.persistence.bugs.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Owner implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer uniqId;

	@ManyToMany
	@JoinTable(name = "Owner_Dog",
		joinColumns = @JoinColumn(name = "onwer_uniqid", referencedColumnName = "uniqId"),
		inverseJoinColumns = @JoinColumn(name = "dog_id", referencedColumnName = "id"))
	private Set<Dog> dogs;

	public Integer getUniqId() {
		return uniqId;
	}

	public void setUniqId(Integer uniqId) {
		this.uniqId = uniqId;
	}

	public Set<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(Set<Dog> dogs) {
		this.dogs = dogs;
	}

	@Override public String toString() {
		return "Owner{" +
			"id=" + id +
			", uniqId=" + uniqId +
			", dogs=" + dogs +
			'}';
	}
}
