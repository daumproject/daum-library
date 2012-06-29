package org.daum.library.android.sitac.observer;

import java.util.Observer;

public interface IObservable {

    /**
     * {@link java.util.Observable#addObserver(java.util.Observer)}
     */
	public void addObserver(Observer obs);

    /**
     * {@link java.util.Observable#deleteObserver(java.util.Observer)}
     */
    public void deleteObserver(Observer obs);
}
