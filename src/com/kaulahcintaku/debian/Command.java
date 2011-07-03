package com.kaulahcintaku.debian;

public class Command {
	
	public static String debianUserCommand(String command){
		return "/system/bin/debian -u "+command;
	}
	
	public static String debianRootCommand(String command){
		return "/system/bin/debian "+command;
	}
	
	public static String initdCommand(String initd, String command){
		return debianRootCommand("/etc/init.d/"+initd+" "+command);
	}
	
	private String command;
	private String caption;
	private boolean needRoot;
	
	public Command(String config) {
		String[] splitted = config.split("#");
		caption = splitted[0];
		command = splitted[1];
		needRoot = Boolean.parseBoolean(splitted[2]);
	}
	
	public String getCaption() {
		return caption;
	}
	
	public String getCommand() {
		return command;
	}
	
	public boolean isNeedRoot() {
		return needRoot;
	}
	
	@Override
	public String toString() {
		return caption;
	}
	
}
