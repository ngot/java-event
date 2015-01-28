package me.ngot;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Val extends Observable {

	List<ValObserver> observers = new ArrayList<ValObserver>();

	public Val() {
	}

	public Val(ValObserver ob) {
		this.addObserver(ob);
	}

	public void addObserver(ValObserver o) {
		super.addObserver(o);
		observers.add(o);
	}

	public List<ValObserver> getObservers() {
		return observers;
	}

	public void update() {
		setChanged();
		notifyObservers();
	}

	public void update(Object v) {
		setChanged();
		notifyObservers(v);
	}

}