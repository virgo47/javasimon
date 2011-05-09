package org.javasimon.testapp.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Tuple data object.
 *
 * @author Radovan Sninsky
 * @version $Revision: $ $Date: $
 * @since 2.0
 * @see Tuples
 */
public class Tuple implements Serializable {

	private int unique1;
	private int idx;
	private int one;
	private int ten;
	private int twenty;
	private int twentyFive;
	private int fifty;
	private int evenOnePercent;
	private int oddOnePercent;
	private String stringU1;
	private String stringU2;
	private String string4;

	private long created;

	public Tuple() {
		this.created = System.currentTimeMillis();
	}

	public int getUnique1() {
		return unique1;
	}

	public void setUnique1(int unique1) {
		this.unique1 = unique1;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getOne() {
		return one;
	}

	public void setOne(int one) {
		this.one = one;
	}

	public int getTen() {
		return ten;
	}

	public void setTen(int ten) {
		this.ten = ten;
	}

	public int getTwenty() {
		return twenty;
	}

	public void setTwenty(int twenty) {
		this.twenty = twenty;
	}

	public int getTwentyFive() {
		return twentyFive;
	}

	public void setTwentyFive(int twentyFive) {
		this.twentyFive = twentyFive;
	}

	public int getFifty() {
		return fifty;
	}

	public void setFifty(int value) {
		this.fifty = value;
	}

	public int getEvenOnePercent() {
		return evenOnePercent;
	}

	public void setEvenOnePercent(int evenOnePercent) {
		this.evenOnePercent = evenOnePercent;
	}

	public int getOddOnePercent() {
		return oddOnePercent;
	}

	public void setOddOnePercent(int oddOnePercent) {
		this.oddOnePercent = oddOnePercent;
	}

	public String getStringU1() {
		return stringU1;
	}

	public void setStringU1(String stringU1) {
		this.stringU1 = stringU1;
	}

	public String getStringU2() {
		return stringU2;
	}

	public void setStringU2(String stringU2) {
		this.stringU2 = stringU2;
	}

	public String getString4() {
		return string4;
	}

	public void setString4(String string4) {
		this.string4 = string4;
	}

	public long getCreated() {
		return created;
	}

	public Date getCreatedAsDate() {
		return new Date(created);
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Tuple");
		buf.append("[unique1=").append(unique1);
		buf.append(",idx=").append(idx);
		buf.append(",one=").append(one);
		buf.append(",ten=").append(ten);
		buf.append(",twenty=").append(twenty);
		buf.append(",twentyFive=").append(twentyFive);
		buf.append(",fifty=").append(fifty);
		buf.append(",even=").append(evenOnePercent);
		buf.append(",odd=").append(oddOnePercent);
		buf.append(",stringU1=").append(stringU1);
		buf.append(",stringU2=").append(stringU2);
		buf.append(",string4=").append(string4);
		buf.append(",created=").append(getCreatedAsDate());
		buf.append(']');
		return buf.toString();
	}
}
