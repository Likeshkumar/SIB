package com.ifp.exceptions;

public class Exceptionifp {
public static Exception exception ;
public static String Message ;

public Throwable getException() {
	return exception;
}

public void setException(Exception exception1) {
	exception = exception1;
}

public String getMessage() {
	return Message;
}

public void setMessage(String message) {
	Message = message;
}


}
