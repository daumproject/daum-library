package org.daum.library.android.sitac.controller;

import java.util.Hashtable;

import org.daum.library.android.sitac.command.AddDangerCommand;
import org.daum.library.android.sitac.command.AddDemandCommand;
import org.daum.library.android.sitac.command.AddTargetCommand;
import org.daum.library.android.sitac.command.ICommand;
import org.daum.library.android.sitac.command.UpdateDemandCommand;
import org.daum.library.android.sitac.model.SITACEngine;

import android.util.Log;

public class CmdManager {

	private static final String TAG = "CmdHandler";
	
	private static CmdManager instance;

	private Hashtable<Class<? extends ICommand>, ICommand> commands;

	private CmdManager(SITACEngine engine) {
		commands = new Hashtable<Class<? extends ICommand>, ICommand>();
		

		commands.put(AddDemandCommand.class, new AddDemandCommand(engine));
		commands.put(AddDangerCommand.class, new AddDangerCommand(engine));
		commands.put(AddTargetCommand.class, new AddTargetCommand(engine));
		commands.put(UpdateDemandCommand.class, new UpdateDemandCommand(engine));
	}

	public static CmdManager getInstance(SITACEngine engine) {
		if (CmdManager.instance == null) {
			CmdManager.instance = new CmdManager(engine);
		}
		return CmdManager.instance;
	}

	public <T> void execute(Class<? extends ICommand> cmdClass, T arg) {
		ICommand cmd = commands.get(cmdClass);
		if (cmd != null) {
			cmd.execute(arg);
		} else {
			Log.w(TAG, "Command "+cmdClass.getSimpleName()+" not found in the CmdHandler, did you add it ?");
		}
	}
}
