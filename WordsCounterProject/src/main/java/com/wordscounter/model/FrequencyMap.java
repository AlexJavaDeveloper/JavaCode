package com.wordscounter.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class FrequencyMap {

	// Attributes
	private ConcurrentMap<String, Frequency> map;
	private List<Entry<String, Frequency>> list;
	private OrderEnum listOrder;


	// Constructors
	public FrequencyMap() {
		this.map = new ConcurrentHashMap<String, Frequency>();
	}


	// Getters / Setters
	public ConcurrentMap<String, Frequency> getMap() {
		return map;
	}
	public void setMap(ConcurrentMap<String, Frequency> map) {
		this.map = map;
	}


	// Methods
	public List<Entry<String, Frequency>> getSortedList(final OrderEnum order) {

		if (list == null || !order.equals(listOrder)) {

			list = new LinkedList<Entry<String, Frequency>>(map.entrySet());

			Collections.sort(list, new Comparator<Entry<String, Frequency>>() {
				public int compare(Entry<String, Frequency> o1, Entry<String, Frequency> o2) {
					if (order == OrderEnum.ASC) {
						return o1.getValue().compareTo(o2.getValue());
					} else {
						return o2.getValue().compareTo(o1.getValue());
					}
				}
			});

			listOrder = order;

		}

		return list;

	}

}

