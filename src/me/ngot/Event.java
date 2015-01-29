package me.ngot;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

class Function implements CallbackFunction {

	private CallbackFunction callback;
	private HashMap<String, Val> events;
	private String k;

	public Function() {
	}

	public Function(CallbackFunction callback, HashMap<String, Val> events,
			String k) {
		this.callback = callback;
		this.events = events;
		this.k = k;
	}

	@Override
	public void call(Object o) {
		this.callback.call(o);
		this.events.remove(this.k);
	}

}

public class Event {

	private static HashMap<String, Val> events = new HashMap<String, Val>();

	public static void on(String k, CallbackFunction callback) {
		Val v = events.get(k);
		if (v == null) {
			synchronized (Event.class) {
				if (events.get(k) == null) {
					events.put(k, new Val(new ValObserver(callback)));
				}
			}

		} else {
			v.addObserver(new ValObserver(callback));
		}

	}

	public static void on(HashMap<String, CallbackFunction> map) {
		Iterator<Entry<String, CallbackFunction>> it = map.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, CallbackFunction> entry = (Entry<String, CallbackFunction>) it
					.next();
			String key = (String) entry.getKey();
			CallbackFunction callback = (CallbackFunction) entry.getValue();
			Event.on(key, callback);
		}
	}

	public static void once(String k, CallbackFunction callback) {
		Val v = events.get(k);
		if (v != null) {
			events.remove(k);
		}
		events.put(k, new Val(
				new ValObserver(new Function(callback, events, k))));
	}

	public static void once(HashMap<String, CallbackFunction> map) {
		Iterator<Entry<String, CallbackFunction>> it = map.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, CallbackFunction> entry = (Entry<String, CallbackFunction>) it
					.next();
			String key = (String) entry.getKey();
			CallbackFunction callback = (CallbackFunction) entry.getValue();
			Event.once(key, callback);
		}
	}

	public static void off(String k, CallbackFunction callback) {
		Val v = events.get(k);
		if (v != null) {
			List<ValObserver> observers = v.getObservers();
			Iterator<ValObserver> it = observers.iterator();
			while (it.hasNext()) {
				ValObserver observer = it.next();
				if (observer.getCallback() == callback) {
					v.deleteObserver(observer);
					it.remove();
				}
			}
		}

	}

	public static void off(HashMap<String, CallbackFunction> map) {

		Iterator<Entry<String, CallbackFunction>> it = map.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, CallbackFunction> entry = (Entry<String, CallbackFunction>) it
					.next();
			String key = (String) entry.getKey();
			CallbackFunction callback = (CallbackFunction) entry.getValue();
			Event.off(key, callback);
		}
	}

	public static void off(String k) {
		events.remove(k);
	}

	public static void off() {
		events.clear();
	}

	public static void trigger(String k) {
		Val v = events.get(k);
		if (v != null) {
			v.update();
		}
	}

	public static void trigger(String k, Object o) {
		Val v = events.get(k);
		if (v != null) {
			v.update(o);
		}
	}

}
