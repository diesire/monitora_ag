package es.uniovi.miw.monitora.agent;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import es.uniovi.miw.monitora.agent.core.MonitoraAgent;
import es.uniovi.miw.monitora.server.model.exceptions.BusinessException;

public class App {
	private static Logger logger = LoggerFactory.getLogger(App.class);
	private static InteractiveThread interactiveThread;

	@Parameter(names = "--help", help = true, description = "Help")
	private boolean help;

	@Parameter(names = { "-i", "--interactive" }, description = "Interactive mode")
	private boolean interactive;

	@Parameter(names = { "--debug" }, description = "Debug mode")
	private boolean debug;
	private MonitoraAgent monitoraAg;
	private JCommander jc;

	public App(String[] args) throws BusinessException {
		jc = new JCommander(this);
		jc.setProgramName("monitora_ag");

		try {
			jc.parse(args);
		} catch (ParameterException e) {
			System.out.println(e.getLocalizedMessage());
			help();
		}

		if (help) {
			help();
		}

		monitoraAg = new MonitoraAgent();

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
		try {
			new App(args);
		} catch (BusinessException e) {
			logger.error(e.toString());
			System.out.println("Fatal error" + e.getLocalizedMessage());
		}
	}

	public void start() throws BusinessException {
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

	private void addShutdownHook(App app) throws BusinessException {
		try {
			Runtime.getRuntime().addShutdownHook(new ShutdownThread(app));

			System.out.println("To close normally, use [Ctrl]+[C]");
		} catch (Throwable t) {
			logger.error("Could not add Shutdown hook", t);
			throw new BusinessException(t);
		}
	}

	public void exit() throws BusinessException {
		try {
			monitoraAg.exit();

			if (interactive) {
				interactiveThread.terminate();
				interactiveThread.interrupt(); // Force stop, I/O thread
			}
			logger.debug("Bye, bye");

		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public void help() {
		jc.usage();
		System.exit(0);
	}

	public void debug() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();

		System.out.println("JVM Name = " + runtime.getName());
		System.out.println("JVM PID  = "
				+ Long.valueOf(runtime.getName().split("@")[0]));

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		System.out.println("Peak Thread Count = " + bean.getPeakThreadCount());
	}

	public void update() throws BusinessException {
		monitoraAg.updateTasks();
	}

	public void dump() throws BusinessException {
		monitoraAg.sendResults();
	}

	public void ping() throws BusinessException {
		monitoraAg.pingServer();
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
		System.out
				.println("MonitORA agent interactive mode. To see comands type 'help'");
		running = true;

		while (running) {
			try {
				parse();
			} catch (BusinessException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}

	private void parse() throws BusinessException {
		String readLine = readLine(">");
		logger.debug("command " + readLine);
		switch (readLine) {
		case "exit":
			app.exit();
			break;
		case "help":
			System.out
					.println("Interactive commands:\n"
							+ "  debug        Show app info\n"
							+ "  start        Start client\n"
							+ "  update       Update client by retrieving server info\n"
							+ "  ping         Check if server is online\n"
							+ "  dump         Send client information to server\n"
							+ "  exit         Exit client\n"
							+ "  help         Show client interactive commands\n"
							+ "  start        Start client\n");
			break;
		case "debug":
			app.debug();
			break;
		case "start":
			app.start();
			break;
		case "update":
			app.update();
			break;
		case "ping":
			app.ping();
			break;
		case "dump":
			app.dump();
			break;
		default:
			throw new BusinessException("Command not recognised");
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
		} catch (BusinessException e) {
			logger.error("Error closing app", e);
			logger.warn("Calling System.exit()...");
			System.exit(-1);
		}
	}
}