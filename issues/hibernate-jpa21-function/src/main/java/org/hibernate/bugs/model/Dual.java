package org.hibernate.bugs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DUAL")
public class Dual {
  @Id
  @Column(name = "DUMMY")
  private String dummy;
}
