package org.daum.library.android.sitac.command;

public interface ICommand {

	public <T> void execute(T arg);
}
