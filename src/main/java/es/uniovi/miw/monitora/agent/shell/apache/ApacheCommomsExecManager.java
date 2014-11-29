package es.uniovi.miw.monitora.agent.shell.apache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

import es.uniovi.miw.monitora.agent.shell.ShellManager;

public class ApacheCommomsExecManager implements ShellManager{
	
	@Override
	public String run(String command) {
		String line = "ls -la";
		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(stdout);
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(psh);
		try {
			executor.execute(cmdLine, resultHandler);
			resultHandler.waitFor();
			return stdout.toString();
			
		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
