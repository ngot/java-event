package me.ngot;

import java.util.Observable;
import java.util.Observer;

public class ValObserver implements Observer {

	private CallbackFunction callback;

	public CallbackFunction getCallback() {
		return callback;
	}

	public void setCallback(CallbackFunction callback) {
		this.callback = callback;
	}

	public ValObserver() {
	}

	public ValObserver(CallbackFunction callback) {
		this.callback = callback;
	}

	@Override
	public void update(Observable observable, Object data) {
		this.callback.call(data);
	}

}