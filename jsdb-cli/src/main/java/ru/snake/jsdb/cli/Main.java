package ru.snake.jsdb.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.snake.jsdb.lib.JsDbContext;
import ru.snake.jsdb.lib.JsDbContextFactory;
import ru.snake.jsdb.lib.error.JsDbException;
import ru.snake.jsdb.lib.settings.JsDbSettings;

public class Main {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	private static final int SUCCESS = 0;

	private static final int ERROR = 1;

	public static void main(String[] args) {
		int exitCode;

		try {
			exitCode = new Main().run(args);
		} catch (JsDbException e) {
			exitCode = ERROR;

			LOG.error("JSDB error", e);
		} catch (FileNotFoundException e) {
			exitCode = ERROR;

			LOG.error("File not found", e);
		} catch (IOException e) {
			exitCode = ERROR;

			LOG.error("IO error", e);
		}

		System.exit(exitCode);
	}

	private int run(String[] args) throws JsDbException, FileNotFoundException, IOException {
		ArgumentParser parser = ArgumentParser.parse(args);

		if (!parser.isSuccess()) {
			parser.printHelp();

			return ERROR;
		}

		JsDbSettings settings = parser.buildSettings();
		String connectionString = parser.getConnectionString();
		List<File> scripts = parser.getScripts();
		boolean noRepl = parser.noRepl();

		try (JsDbContext context = JsDbContextFactory.create(settings, connectionString)) {
			for (File script : scripts) {
				new ScriptExecutor(context, script, System.out, System.err).run();
			}

			if (!noRepl) {
				new ReplExecutor(context, System.in, System.out, System.err).run();
			}
		}

		return SUCCESS;
	}

}
