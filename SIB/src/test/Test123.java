package test;

import java.util.ArrayList;
import java.util.List;
 

public class Test123 {
	
	private int id;
	private String name;

		 
 
	
public Test123(int id, String name) {
	
		this.id = id;
		this.name = name;
	}




@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
}




@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Test123 other = (Test123) obj;
	if (id != other.id)
		return false;
	if (name == null) {
		if (other.name != null)
			return false;
	} else if (!name.equals(other.name))
		return false;
	return true;
}




@Override
	public String toString() {
		return "Test123 [id=" + id + ", name=" + name + "]";
	}




public static void main(String[] args) {
		
		Test123 emp=new Test123(1,"Test");
		Test123 emp1=new Test123(1,"Test");
		
		List<Test123> lst=new  ArrayList<>();
		lst.add(emp);
		lst.add(emp1);
		 System.out.println(emp.toString());
		 System.out.println(emp1.toString());
		System.out.println(emp.toString().equals(emp1.toString()));
		 System.out.println(lst.size());
		 System.out.println(lst.contains(( new Test123(1,"Test")) ));
	//	 System.out.println(lst.contains(emp));
		};
		
		 
	}

 
