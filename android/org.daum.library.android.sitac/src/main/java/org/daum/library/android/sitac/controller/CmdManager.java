package org.daum.library.android.sitac.controller;

import java.util.Hashtable;

import org.daum.library.android.sitac.command.AddModelCommand;
import org.daum.library.android.sitac.command.DeleteModelCommand;
import org.daum.library.android.sitac.command.ICommand;
import org.daum.library.android.sitac.command.UpdateModelCommand;
import org.daum.library.android.sitac.model.SITACEngine;

import android.util.Log;

public class CmdManager {

	private static final String TAG = "CmdHandler";
	
	private static CmdManager instance;

	private Hashtable<Class<? extends ICommand>, ICommand> commands;

	private CmdManager(SITACEngine engine) {
		commands = new Hashtable<Class<? extends ICommand>, ICommand>();
		

		commands.put(AddModelCommand.class, new AddModelCommand(engine));
		commands.put(UpdateModelCommand.class, new UpdateModelCommand(engine));
		commands.put(DeleteModelCommand.class, new DeleteModelCommand(engine));
	}

	public static CmdManager getInstance(SITACEngine engine) {
		if (CmdManager.instance == null) {
			CmdManager.instance = new CmdManager(engine);
		}
		return CmdManager.instance;
	}

	public <T> void execute(Class<? extends ICommand> cmdClass, T... args) {
		ICommand cmd = commands.get(cmdClass);
		if (cmd != null) {
			cmd.execute(args);
		} else {
			Log.w(TAG, "Command "+cmdClass.getSimpleName()+" not found in the CmdHandler, did you add it ?");
		}
	}
}
