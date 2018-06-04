package ru.snake.jsdb.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.snake.jsdb.cli.args.ArgumentParser;
import ru.snake.jsdb.cli.args.Arguments;
import ru.snake.jsdb.cli.config.ConfigReader;
import ru.snake.jsdb.cli.config.Configuration;
import ru.snake.jsdb.cli.executor.ReplExecutor;
import ru.snake.jsdb.cli.executor.ScriptExecutor;
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
		Arguments arguments = parser.getArguments();
		SettingsBuilder builder = new SettingsBuilder();
		String configPath = arguments.getConfigPath();

		if (configPath != null) {
			try (Reader reader = new FileReader(configPath)) {
				Configuration config = ConfigReader.read(reader);

				builder.withConfiguration(config);
			}
		}

		builder.withArguments(arguments);

		if (!builder.validate()) {
			parser.printHelp();

			return ERROR;
		}

		JsDbSettings jsdbSettings = builder.build();
		String connectionString = builder.getJdbcUrl();
		List<String> scriptPaths = arguments.getScriptPaths();
		boolean noRepl = arguments.isNoRepl();

		try (JsDbContext context = JsDbContextFactory.create(jsdbSettings, connectionString)) {
			for (String scriptPath : scriptPaths) {
				File script = new File(scriptPath);

				new ScriptExecutor(context, script, System.out, System.err).run();
			}

			if (!noRepl) {
				new ReplExecutor(context, System.in, System.out, System.err).run();
			}
		}

		return SUCCESS;
	}

}
