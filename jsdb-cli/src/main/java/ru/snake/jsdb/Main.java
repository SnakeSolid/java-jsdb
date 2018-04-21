package ru.snake.jsdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.snake.jsdb.error.JsDbException;
import ru.snake.jsdb.error.ScriptExecutionException;
import ru.snake.jsdb.settings.FieldMapping;
import ru.snake.jsdb.settings.JsDbSettings;
import ru.snake.jsdb.settings.TableMapping;
import ru.snake.jsdb.settings.TagMapping;

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
		CommandLine commandLine = parseOptions(args);

		if (commandLine == null) {
			return SUCCESS;
		}

		JsDbSettings settings = buildSettings(commandLine);
		String connectionString = getConnectionString(commandLine);
		List<File> scripts = getScripts(commandLine);
		boolean startRepl = getStartRepl(commandLine);

		try (JsDbContext context = JsDbContextFactory.create(settings, connectionString)) {
			for (File script : scripts) {
				runScript(context, script);
			}

			if (startRepl) {
				runRepl(context);
			}
		}

		return SUCCESS;
	}

	private void runScript(JsDbContext context, File script) throws IOException, FileNotFoundException {
		try (FileReader reader = new FileReader(script)) {
			try {
				showResult(context.execute(reader));
			} catch (ScriptExecutionException e) {
				showError(e);
			}
		}
	}

	private void runRepl(JsDbContext context) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			String line = reader.readLine();

			while (line != null) {
				line = reader.readLine();

				try {
					showResult(context.execute(line));
				} catch (ScriptExecutionException e) {
					showError(e);
				}
			}
		}
	}

	private boolean getStartRepl(CommandLine commandLine) {
		boolean nonInteractive = commandLine.hasOption('n');

		return !nonInteractive;
	}

	private void showError(Throwable error) {
		Throwable cause = error;

		while (cause != null) {
			LOG.warn(cause.getLocalizedMessage());

			cause = cause.getCause();
		}

		for (Throwable suppressed : error.getSuppressed()) {
			LOG.warn(suppressed.getLocalizedMessage());
		}
	}

	private void showResult(Object result) {
		if (result == null) {
		} else if (result instanceof Iterable<?>) {
			Iterable<?> iterable = (Iterable<?>) result;

			iterable.forEach(System.out::println);
		} else if (result instanceof Iterator<?>) {
			Iterator<?> iterator = (Iterator<?>) result;

			iterator.forEachRemaining(System.out::println);
		} else if (result instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) result;

			map.forEach((k, v) -> System.out.println(k + " -> " + v));
		} else {
			System.out.println(result);
		}
	}

	private List<File> getScripts(CommandLine commandLine) {
		List<File> scripts = Stream.of(commandLine.getOptionValues('s')).map(path -> new File(path)).collect(
				Collectors.toList());

		return scripts;
	}

	private String getConnectionString(CommandLine commandLine) {
		String connectionString = commandLine.getOptionValue('u');

		return connectionString;
	}

	private JsDbSettings buildSettings(CommandLine commandLine) {
		String driver = commandLine.getOptionValue('d');
		String[] libraryPaths = commandLine.getOptionValues('l');
		Properties fields = commandLine.getOptionProperties("f");

		JsDbSettings settings = new JsDbSettings();
		settings.setLibraryPaths(Stream.of(libraryPaths).collect(Collectors.toSet()));
		settings.setDrivers(Collections.singleton(driver));

		TableMapping tableMapping = settings.getTableFieldMapping();

		fields.forEach((key, value) -> {
			String tableAndFiled = key.toString();
			int dotPosition = tableAndFiled.lastIndexOf('.');

			if (dotPosition != -1 && dotPosition > 0 && dotPosition < tableAndFiled.length()) {
				String tableName = tableAndFiled.substring(0, dotPosition);
				String fieldName = tableAndFiled.substring(dotPosition + 1);

				if (tableMapping.contains(tableName)) {
					FieldMapping fieldMapping = tableMapping.get(tableName);
					fieldMapping.insert(fieldName, value.toString());
				} else {
					FieldMapping fieldMapping = new FieldMapping();
					fieldMapping.insert(fieldName, value.toString());
					tableMapping.insert(tableName, fieldMapping);
				}
			} else {
				LOG.warn("Incorrect table filed definition in field mapping: `{}`", key);
			}
		});

		TagMapping tagMapping = settings.getTagMapping();

		fields.forEach((key, value) -> {
			tagMapping.insert(key.toString(), value.toString());
		});

		return settings;
	}

	private CommandLine parseOptions(String[] args) {
		Option libraries = Option.builder("l")
				.longOpt("library-path")
				.hasArg()
				.desc("Path to library JAR file")
				.build();
		Option driver = Option.builder("d")
				.longOpt("driver")
				.required()
				.hasArg()
				.desc("JDBC driver class name")
				.build();
		Option fields = Option.builder("f")
				.longOpt("field")
				.argName("table.field")
				.numberOfArgs(2)
				.valueSeparator()
				.desc("Field mapper class name (table.field=class)")
				.build();
		Option tags = Option.builder("t")
				.longOpt("tag")
				.argName("tag")
				.numberOfArgs(2)
				.valueSeparator()
				.desc("Tag mapper class name (tag=class)")
				.build();
		Option url = Option.builder("u")
				.longOpt("url")
				.required()
				.hasArg()
				.desc("JDBC database URL to connect")
				.build();
		Option script = Option.builder("s").longOpt("script").hasArg().desc("Execute file in database context").build();
		Option nonInteractive = Option.builder("n").longOpt("no-repl").desc("Do not start REPL").build();

		Options options = new Options();
		options.addOption(libraries);
		options.addOption(driver);
		options.addOption(fields);
		options.addOption(tags);
		options.addOption(url);
		options.addOption(script);
		options.addOption(nonInteractive);

		try {
			return new DefaultParser().parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());

			HelpFormatter formatter = new HelpFormatter();

			formatter.printHelp("jsdb-cli", options);
		}

		return null;
	}

}
