import static org.junit.Assert.*;

import java.util.HashMap;

import me.ngot.CallbackFunction;
import me.ngot.Event;

import org.junit.Test;

public class Event_Test {

	int v1 = 0, v2 = 0, v3 = 0;

	private void t1(int a1, int a2) {
		v1 = v1 + a1 - a2 + 1234;
	}

	private void t2(int a1, int a2) {
		v2 = v2 + a1 - a2 + 4321;
	}

	CallbackFunction callback1 = new CallbackFunction() {

		@Override
		public void call(Object o) {
			int[] arr = (int[]) o;
			t1(arr[0], arr[1]);
		}
	};

	CallbackFunction callback2 = new CallbackFunction() {

		@Override
		public void call(Object o) {
			int[] arr = (int[]) o;
			t2(arr[0], arr[1]);
		}
	};

	CallbackFunction callback3 = new CallbackFunction() {

		@Override
		public void call(Object o) {
			v3 = 1;
		}
	};

	@Test
	public void on() {

		Event.on("on_test", callback1);

		assertEquals(v1, 0);
		assertEquals(v2, 0);
		int[] a = { 200, 100 };
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 0);

		Event.on("on_test", callback2);

		int[] b = { 2000, 1000 };
		Event.trigger("on_test", b);

		assertEquals(v1, 3568);
		assertEquals(v2, 5321);
	}

	@Test
	public void on_twice() {
		Event.on("on_test", callback1);
		assertEquals(v1, 0);
		int[] a = { 200, 100 };
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		v1 = 0;

		Event.on("on_test", callback1);
		int[] b = { 2000, 1000 };
		Event.trigger("on_test", b);
		assertEquals(v1, 4468);
	}

	@Test
	public void off() {
		Event.on("on_test", callback1);
		Event.on("on_test", callback2);

		int[] a = { 200, 100 };
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 4421);
		v1 = 0;
		v2 = 0;

		Event.off("on_test", callback1);
		int[] b = { 20, 10 };
		Event.trigger("on_test", b);
		assertEquals(v1, 0);
		assertEquals(v2, 4331);
	}

	@Test
	public void once() {
		Event.once("on_test", callback1);

		assertEquals(v1, 0);
		assertEquals(v2, 0);
		int[] a = { 200, 100 };
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 0);
		v1 = 0;

		Event.trigger("on_test", a);
		assertEquals(v1, 0);
		assertEquals(v2, 0);
	}

	@Test
	public void once_map() {
		HashMap<String, CallbackFunction> map = new HashMap<String, CallbackFunction>();
		map.put("on_test1", callback1);
		map.put("on_test2", callback2);
		Event.once(map);

		assertEquals(v1, 0);
		assertEquals(v2, 0);
		int[] a = { 200, 100 };
		Event.trigger("on_test1", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 0);
		Event.trigger("on_test1", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 0);

		int[] b = { 2000, 1000 };
		Event.trigger("on_test2", b);
		assertEquals(v1, 1334);
		assertEquals(v2, 5321);
		Event.trigger("on_test2", b);
		assertEquals(v1, 1334);
		assertEquals(v2, 5321);
	}

	@Test
	public void off_all() {

		Event.on("on_test", callback1);
		Event.on("on_test1", callback2);

		int[] a = { 200, 100 };
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 0);
		v1 = 0;

		Event.trigger("on_test1", a);
		assertEquals(v1, 0);
		assertEquals(v2, 4421);
		v2 = 0;

		Event.off();
		Event.trigger("on_test", a);
		Event.trigger("on_test1", a);
		assertEquals(v1, 0);
		assertEquals(v2, 0);
	}

	@Test
	public void on_map() {
		HashMap<String, CallbackFunction> map = new HashMap<String, CallbackFunction>();
		map.put("on_test1", callback1);
		map.put("on_test2", callback2);
		Event.on(map);

		assertEquals(v1, 0);
		assertEquals(v2, 0);
		int[] a = { 200, 100 };
		Event.trigger("on_test1", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 0);

		int[] b = { 2000, 1000 };
		Event.trigger("on_test2", b);
		assertEquals(v1, 1334);
		assertEquals(v2, 5321);
	}

	@Test
	public void off_map() {
		HashMap<String, CallbackFunction> map = new HashMap<String, CallbackFunction>();
		map.put("on_test1", callback1);
		map.put("on_test2", callback2);
		Event.on(map);

		assertEquals(v1, 0);
		assertEquals(v2, 0);
		int[] a = { 200, 100 };
		Event.trigger("on_test1", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 0);

		int[] b = { 2000, 1000 };
		Event.trigger("on_test2", b);
		assertEquals(v1, 1334);
		assertEquals(v2, 5321);

		v1 = 0;
		v2 = 0;

		Event.off(map);
		Event.trigger("on_test1", a);
		Event.trigger("on_test2", b);

		assertEquals(v1, 0);
		assertEquals(v2, 0);
	}

	@Test
	public void off_key() {
		Event.on("on_test", callback1);
		Event.on("on_test", callback2);

		int[] a = { 200, 100 };
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		assertEquals(v2, 4421);
		v1 = 0;
		v2 = 0;

		Event.off("on_test");
		int[] b = { 20, 10 };
		Event.trigger("on_test", b);
		assertEquals(v1, 0);
		assertEquals(v2, 0);
	}

	@Test
	public void overwrite() {

		Event.on("on_test", callback1);
		Event.once("on_test", callback1);

		assertEquals(v1, 0);

		int[] a = { 200, 100 };
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		Event.trigger("on_test", a);
		assertEquals(v1, 1334);
		assertEquals(v1, 1334);
	}

	@Test
	public void trigger_null() {
		Event.on("on_test", callback3);
		assertEquals(v3, 0);
		Event.trigger("on_test");
		assertEquals(v3, 1);
	}

}
