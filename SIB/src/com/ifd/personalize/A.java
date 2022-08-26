package com.ifd.personalize;

class B
{
int x=-1;
int display(){
try{
  
  if(x/0==0)
  {
    return x;
  }

}catch(Exception e)
{
  System.out.println("Exception is"+e.getMessage());
}
return x;
}
}
class A
{
public static void main(String[] args){
B obj = new B();
System.out.println(obj.display());

}
}

