package com.kaulahcintaku.debian;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DebianManager extends Activity {
	
	private static String KIT_LOCATION = "/mnt/sdcard/external_sd/debian";
	
	private static String MOUNT_COMMAND = "/system/bin/debian -m"; 
	private static String UMOUNT_COMMAND = "/system/bin/debian -um"; 

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button)findViewById(R.id.mount)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				runCommand(MOUNT_COMMAND, true);
			}
		});
        ((Button)findViewById(R.id.umount)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				runCommand(UMOUNT_COMMAND, true);
			}
		});
        
        final List<String> initdScripts = readAllLines(KIT_LOCATION+"/initd.txt");
        ArrayAdapter<String> initdAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, initdScripts);
        ListView initdView = (ListView)findViewById(R.id.initd_entry);
        initdView.setAdapter(initdAdapter);
        initdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int item,
        			long arg3) {
        		createInitdCommandsDialog(initdScripts.get(item)).show();
        	}
		});
        configureHeight(initdView, initdAdapter);
        
        List<String> scriptConfigs = readAllLines(KIT_LOCATION+"/scripts.txt");
        final List<Command> commands = new ArrayList<Command>();
        for(String config: scriptConfigs)
        	commands.add(new Command(config));
        ArrayAdapter<Command> scriptsAdapter = new ArrayAdapter<Command>(this, android.R.layout.simple_list_item_1, commands);
        ListView scriptsView = ((ListView)findViewById(R.id.scripts_entry));
        scriptsView.setAdapter(scriptsAdapter);
        scriptsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int item,
        			long arg3) {
        		Command command = commands.get(item);
        		String commandText = command.isNeedRoot() 
	        		? Command.debianRootCommand(command.getCommand()) 
	        		: Command.debianUserCommand(command.getCommand()) ;
	        	runCommand(commandText, true);
        	}
		});
        configureHeight(scriptsView, scriptsAdapter);
    }
    
    private List<String> readAllLines(String file){
		List<String> results = new ArrayList<String>();
    	try{
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    		String line = null;
    		while((line = reader.readLine()) != null){
    			String trimmed = line.trim();
    			if(trimmed.length() != 0)
    				results.add(trimmed);
    		}
    	}
    	catch(IOException ioe){
    		HostedCommandActivity.showException(ioe, this);
    	}
    	return results;
    }
    
    private AlertDialog createInitdCommandsDialog(final String initd) {
    	final CharSequence[] commands = new CharSequence[]{"start", "stop", "restart", "status"};
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Pick Command");
    	builder.setItems(commands, new DialogInterface.OnClickListener() {
    	    public void onClick(DialogInterface dialog, int item) {
    	    	runCommand(Command.initdCommand(initd, (String)commands[item]), true);
    	    }
    	});
    	AlertDialog alert = builder.create();
        return alert;
    }
    
    private void runCommand(String command, boolean isHosted){
		if(isHosted){
			Intent intent = new Intent(this, HostedCommandActivity.class);
			Bundle data = new Bundle();
			data.putString("command", command);
			intent.putExtras(data);
			startActivity(intent);
		}
		else{
			Intent intent = new Intent(
					Intent.ACTION_VIEW,
					Uri.parse("exe:#"+command+"#"));
			startActivity(intent);
		}
    }
    
	private void configureHeight(ListView listView, ArrayAdapter<?> adapter){
		int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
	}
}