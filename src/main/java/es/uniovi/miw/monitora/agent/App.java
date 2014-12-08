package es.uniovi.miw.monitora.agent;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

import javax.persistence.criteria.CriteriaBuilder.Case;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import es.uniovi.miw.monitora.agent.task.TaskManager;
import es.uniovi.miw.monitora.agent.task.quartz.QuartzTaskManager;

public class App {
	private static Logger logger = LoggerFactory.getLogger(App.class);
	private static InteractiveThread interactiveThread;

	@Parameter(names = "--help", help = true)
	private boolean help;

	@Parameter(names = { "-i", "--interactive" }, description = "Interactive mode")
	private boolean interactive;

	@Parameter(names = { "--debug" }, description = "Debug mode")
	private boolean debug;
	private MonitoraAgent monitoraAg;
	private JCommander jc;

	public App(String[] args) {
		monitoraAg = new MonitoraAgent();
		jc = new JCommander(this);
		jc.setProgramName("monitora_ag");

		if (args.length > 1) {
			jc.parse(args);
		}

		if (help) {
			help();
			return;
		}

		if (debug) {
			debug();
		}

		if (interactive) {
			interactiveThread = new InteractiveThread(this);
			interactiveThread.start();
		}

		addShutdownHook(this);
		if (!interactive) {
			start();
		}
	}

	public static void main(String[] args) {
		new App(args);
	}

	public void start() {
		monitoraAg.start();
	}

	// private static void addFailsafeExit() {
	// try {
	// Thread.currentThread();
	// Thread.sleep(1000000);
	// } catch (InterruptedException ie) {
	// logger.error("Error in failsafe exit", ie);
	// }
	//
	// System.exit(0);
	// }

	private void addShutdownHook(App app) {
		try {
			Runtime.getRuntime().addShutdownHook(new ShutdownThread(app));

			System.out.println("To close normally, use [Ctrl]+[C]");
		} catch (Throwable t) {
			logger.debug("Could not add Shutdown hook");
		}
	}

	public void exit() throws InterruptedException {
		monitoraAg.exit();

		if (interactive) {
			interactiveThread.terminate();
			interactiveThread.interrupt(); // Force stop, I/O thread
		}
		logger.debug("Bye, bye");
	}

	public void help() {
		jc.usage();
	}

	public void debug() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

		System.out.println("JVM Name = " + runtime.getName());
		System.out.println("JVM PID  = "
				+ Long.valueOf(runtime.getName().split("@")[0]));

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		System.out.println("Peak Thread Count = " + bean.getPeakThreadCount());
	}

	public void test() {
		monitoraAg.test();
	}
}

class InteractiveThread extends Thread {
	private App app;
	public Logger logger = LoggerFactory.getLogger(InteractiveThread.class);
	private boolean running;

	public InteractiveThread(App app) {
		super("Interactive thread");
		this.app = app;
	}

	public void terminate() {
		running = false;
	}

	@Override
	public void run() {
		logger.debug("interactive mode");
		running = true;

		while (running) {
			parse();
		}
	}

	private void parse() {
		switch (readLine(">")) {
		case "exit":
			logger.debug("command exit");
			try {
				app.exit();
			} catch (InterruptedException e) {
				logger.error("Error closing app");
				logger.debug(e.toString());
			}
			break;
		case "help":
			logger.debug("command help");
			app.help();
			break;
		case "debug":
			logger.debug("command debug");
			app.debug();
			break;
		case "start":
			logger.debug("command start");
			app.start();
			break;
		case "test":
			logger.debug("command test");
			app.test();
			break;
		default:
			break;
		}
	}

	private static String readLine(String prompt) {
		String line = null;
		Console console = System.console();
		if (console != null) {
			line = console.readLine(prompt);
		} else {
			System.out.print(prompt);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(System.in));
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				// Ignore
			}
		}
		return line;
	}
}

class ShutdownThread extends Thread {
	private App app = null;
	public Logger logger = LoggerFactory.getLogger(ShutdownThread.class);

	public ShutdownThread(App app) {
		super("Shutdown Thread");
		this.app = app;
	}

	@Override
	public void run() {
		logger.debug("shutdown event");
		try {
			app.exit();
		} catch (InterruptedException e) {
			logger.error("Error closing app");
			logger.debug(e.toString());
		}
	}
}
