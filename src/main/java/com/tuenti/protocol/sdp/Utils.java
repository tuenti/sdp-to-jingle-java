package com.tuenti.protocol.sdp;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Tuenti Technologies. All rights reserved.
 *
 * @author Manuel Peinado Gallego <mpeinado@tuenti.com>
 */
public class Utils {
	public static <T> ArrayList<T> filterByClass(List<T> items, Class<T> class_) {
		ArrayList<T> result = new ArrayList<T>();
		for (T item : items) {
			if (item.getClass().equals(class_)) {
				result.add(item);
			}
		}
		return result;
	}
}
