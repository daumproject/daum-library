package org.daum.library.android.sitac.manager;

import java.util.Hashtable;

import org.daum.library.android.sitac.command.ICommand;

import android.util.Log;

public abstract class AbstractCmdManager<T extends ICommand> {
	
	private static final String TAG = "AbstractCmdManager";
	
	protected Hashtable<Class<? extends T>, T> commands;

	public AbstractCmdManager() {
		commands = new Hashtable<Class<? extends T>, T>();
	}
	
	public <K> void execute(Class<? extends T> cmdClass, K... args) {
		T cmd = commands.get(cmdClass);
		if (cmd != null) {
			try {
				cmd.execute(args);
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Error while executing command "+cmd.getClass().getSimpleName(), e);
			}
		} else {
			Log.w(TAG, "Command "+cmdClass.getSimpleName()+" not found in the AbstractCmdManager, did you add it ?");
		}
	}
}
