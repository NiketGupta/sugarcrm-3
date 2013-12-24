package com.knowprocess.sugarcrm.api;

public class SugarException extends RuntimeException {

	private static final long serialVersionUID = -6622157312720254155L;

	public SugarException() {
		super();
	}

	public SugarException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SugarException(String arg0) {
		super(arg0);
	}

	public SugarException(Throwable arg0) {
		super(arg0);
	}

}
