package service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthBean {

	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	private String salt;
	private int count;
	private String hash;

}
