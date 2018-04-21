package ru.snake.jsdb;

import java.io.File;
import java.util.Collections;
import java.util.List;
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

import ru.snake.jsdb.settings.FieldMapping;
import ru.snake.jsdb.settings.JsDbSettings;
import ru.snake.jsdb.settings.TableMapping;
import ru.snake.jsdb.settings.TagMapping;

public class ArgumentParser {

	private static final Logger LOG = LoggerFactory.getLogger(ArgumentParser.class);

	private static final String NO_REPL = "n";
	private static final String SCRIPT_PATH = "s";
	private static final String JDBC_URL = "u";
	private static final String TAG_MAPPER = "t";
	private static final String FIELD_MAPPER = "f";
	private static final String JDBC_DRIVER = "d";
	private static final String LIBRARY_PATH = "l";

	private final Options options;

	private final CommandLine commandLine;

	private ArgumentParser(CommandLine commandLine, Options options) {
		this.options = options;
		this.commandLine = commandLine;
	}

	public boolean isSuccess() {
		return this.commandLine != null;
	}

	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("jsdb-cli", this.options);
	}

	public boolean noRepl() {
		return this.commandLine.hasOption(NO_REPL);
	}

	public List<File> getScripts() {
		String[] scriptPaths = this.commandLine.getOptionValues(SCRIPT_PATH);
		List<File> scripts;

		if (scriptPaths != null) {
			scripts = Stream.of(scriptPaths).map(path -> new File(path)).collect(Collectors.toList());
		} else {
			scripts = Collections.emptyList();
		}

		return scripts;
	}

	public String getConnectionString() {
		return this.commandLine.getOptionValue(JDBC_URL);
	}

	public JsDbSettings buildSettings() {
		String driver = this.commandLine.getOptionValue(JDBC_DRIVER);
		String[] libraryPaths = this.commandLine.getOptionValues(LIBRARY_PATH);
		Properties fields = this.commandLine.getOptionProperties(FIELD_MAPPER);
		Properties tags = this.commandLine.getOptionProperties(TAG_MAPPER);

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

		tags.forEach((key, value) -> {
			tagMapping.insert(key.toString(), value.toString());
		});

		return settings;
	}

	public static ArgumentParser parse(String[] args) {
		Options options = createOptions();

		try {
			CommandLine commandLine = new DefaultParser().parse(options, args);

			return new ArgumentParser(commandLine, options);
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
		}

		return new ArgumentParser(null, options);
	}

	private static Options createOptions() {
		Option libraries = Option.builder(LIBRARY_PATH)
				.longOpt("library-path")
				.hasArg()
				.desc("Path to library JAR file")
				.build();
		Option driver = Option.builder(JDBC_DRIVER)
				.longOpt("driver")
				.required()
				.hasArg()
				.desc("JDBC driver class name")
				.build();
		Option fields = Option.builder(FIELD_MAPPER)
				.longOpt("field")
				.argName("table.field")
				.numberOfArgs(2)
				.valueSeparator()
				.desc("Field mapper class name (table.field=class)")
				.build();
		Option tags = Option.builder(TAG_MAPPER)
				.longOpt("tag")
				.argName("tag")
				.numberOfArgs(2)
				.valueSeparator()
				.desc("Tag mapper class name (tag=class)")
				.build();
		Option url = Option.builder(JDBC_URL)
				.longOpt("url")
				.required()
				.hasArg()
				.desc("JDBC database URL to connect")
				.build();
		Option script = Option.builder(SCRIPT_PATH)
				.longOpt("script")
				.hasArg()
				.desc("Execute file in database context")
				.build();
		Option noRepl = Option.builder(NO_REPL).longOpt("no-repl").desc("Do not start REPL").build();

		Options options = new Options();
		options.addOption(libraries);
		options.addOption(driver);
		options.addOption(fields);
		options.addOption(tags);
		options.addOption(url);
		options.addOption(script);
		options.addOption(noRepl);

		return options;
	}

	@Override
	public String toString() {
		return "ArgumentParser [options=" + options + ", commandLine=" + commandLine + "]";
	}

}
