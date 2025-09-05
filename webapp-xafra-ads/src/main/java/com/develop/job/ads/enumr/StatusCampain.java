package com.develop.job.ads.enumr;

public enum StatusCampain {

	PROCESSED(1), PROCESSING(2), UNPROCESSING(3), CANCELED(4), ERROR(5), TRAKING_PROCESSING_INT(2),
	TRAKING_SUCCESS_INT(1);

	private StatusCampain(int status) {
		this.status = status;
	}

	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
