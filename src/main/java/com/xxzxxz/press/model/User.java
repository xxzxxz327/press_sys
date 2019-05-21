package com.xxzxxz.press.model;

public class User {
	private String name;
	private int age;
	private String password;
	public User(){}
    public User(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	 @Override
    public String toString() {
        return ("name=" + this.name + ",age=" + this.age + ",password=" + this.password);
    }
	
}
