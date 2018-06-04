package ru.snake.jsdb.cli.args;

import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgumentParser {

	private static final String NO_REPL = "n";
	private static final String SCRIPT_PATH = "s";
	private static final String JDBC_URL = "u";
	private static final String TAG_MAPPER = "t";
	private static final String FIELD_MAPPER = "f";
	private static final String JDBC_DRIVER = "d";
	private static final String LIBRARY_PATH = "l";
	private static final String CONFIG_PATH = "c";

	private final Options options;

	private final CommandLine commandLine;

	private ArgumentParser(CommandLine commandLine, Options options) {
		this.options = options;
		this.commandLine = commandLine;
	}

	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp("jsdb-cli", this.options);
	}

	public Arguments getArguments() {
		Arguments arguments = new Arguments();

		if (this.commandLine != null) {
			String config = this.commandLine.getOptionValue(CONFIG_PATH);
			String driver = this.commandLine.getOptionValue(JDBC_DRIVER);
			String url = this.commandLine.getOptionValue(JDBC_URL);
			String[] libraryPaths = this.commandLine.getOptionValues(LIBRARY_PATH);
			String[] scriptPaths = this.commandLine.getOptionValues(SCRIPT_PATH);
			Properties fields = this.commandLine.getOptionProperties(FIELD_MAPPER);
			Properties tags = this.commandLine.getOptionProperties(TAG_MAPPER);
			boolean noRelp = this.commandLine.hasOption(NO_REPL);

			arguments.setConfigPath(config);
			arguments.setJdbcDriver(driver);
			arguments.setJdbcUrl(url);
			arguments.setNoRepl(noRelp);

			if (libraryPaths != null) {
				Stream.of(libraryPaths).forEach(arguments::addLibraryPath);
			}

			if (scriptPaths != null) {
				Stream.of(scriptPaths).forEach(arguments::addScriptPath);
			}

			fields.forEach((key, value) -> arguments.addFieldMapper(String.valueOf(key), String.valueOf(value)));
			tags.forEach((key, value) -> arguments.addTagMapper(String.valueOf(key), String.valueOf(value)));
		}

		return arguments;
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

	/**
	 * Create and returns new {@link Options}.
	 *
	 * @return options instance
	 */
	private static Options createOptions() {
		Option config = Option.builder(CONFIG_PATH)
				.longOpt("config")
				.argName("PATH")
				.hasArg()
				.desc("Configuration file path.")
				.build();
		Option libraries = Option.builder(LIBRARY_PATH)
				.longOpt("library-path")
				.argName("URL")
				.hasArg()
				.desc("URL to library JAR file (if path is local - `file://...`).")
				.build();
		Option driver = Option.builder(JDBC_DRIVER)
				.longOpt("driver")
				.argName("CLASS")
				.hasArg()
				.desc("JDBC driver class name")
				.build();
		Option fields = Option.builder(FIELD_MAPPER)
				.longOpt("field")
				.argName("TABLE.FIELD=CLASS")
				.numberOfArgs(2)
				.valueSeparator()
				.desc("Mapper class name for single table field. Filed represents full field name in particular table.")
				.build();
		Option tags = Option.builder(TAG_MAPPER)
				.longOpt("tag")
				.argName("TAG=CLASS")
				.numberOfArgs(2)
				.valueSeparator()
				.desc("Mapper class name for single tag. Tag represents full column name in query results (db.execute)")
				.build();
		Option url = Option.builder(JDBC_URL)
				.longOpt("url")
				.argName("JDBC_URL")
				.hasArg()
				.desc("JDBC database URL to connect.")
				.build();
		Option script = Option.builder(SCRIPT_PATH)
				.longOpt("script")
				.argName("PATH")
				.hasArg()
				.desc("Execute file in database context.")
				.build();
		Option noRepl = Option.builder(NO_REPL).longOpt("no-repl").desc("Do not start REPL").build();

		Options options = new Options();
		options.addOption(config);
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
