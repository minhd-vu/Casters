package Cast.Configs;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.Charset;

public class ConfigManager
{
	private JavaPlugin plugin;

	public ConfigManager(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}

	public Config getConfig(String fileName)
	{
		File file = this.getConfigFile(fileName);

		Config config = new Config(this.getConfigContent(fileName), file, this.getCommentsNum(file), plugin);

		return config;
	}

	private File getConfigFile(String file)
	{
		if (file.isEmpty() || file == null)
		{
			return null;
		}

		File configFile;

		if (file.contains("/"))
		{
			if (file.startsWith("/"))
			{
				configFile = new File(plugin.getDataFolder() + file.replace("/", File.separator));
			}
			else
			{
				configFile = new File(plugin.getDataFolder() + File.separator + file.replace("/", File.separator));
			}
		}
		else
		{
			configFile = new File(plugin.getDataFolder(), file);
		}

		return configFile;
	}

	public InputStream getConfigContent(String filePath)
	{
		return this.getConfigContent(this.getConfigFile(filePath));
	}

	private int getCommentsNum(File file)
	{
		if (!file.exists())
		{
			return 0;
		}
		try
		{
			int comments = 0;
			String currentLine;

			BufferedReader reader = new BufferedReader(new FileReader(file));

			while ((currentLine = reader.readLine()) != null)
			{
				if (currentLine.startsWith("#"))
				{
					comments++;
				}
			}

			reader.close();
			return comments;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	public InputStream getConfigContent(File file)
	{
		if (!file.exists())
		{
			return null;
		}
		try
		{
			int commentNum = 0;

			String addLine;
			String currentLine;
			String pluginName = this.getPluginName();

			StringBuilder whole = new StringBuilder("");
			BufferedReader reader = new BufferedReader(new FileReader(file));

			while ((currentLine = reader.readLine()) != null)
			{
				if (currentLine.startsWith("#"))
				{
					addLine = currentLine.replaceFirst("#", pluginName + "_COMMENT_" + commentNum + ":");
					whole.append(addLine + "\n");
					commentNum++;
				}
				else
				{
					whole.append(currentLine + "\n");
				}
			}

			String config = whole.toString();
			InputStream configStream = new ByteArrayInputStream(config.getBytes(Charset.forName("UTF-8")));

			reader.close();
			return configStream;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getPluginName()
	{
		return plugin.getDescription().getName();
	}

	public boolean getFileExists(String fileName)
	{
		return this.getConfigFile(fileName).exists();
	}

	public Config getNewConfig(String fileName)
	{
		return this.getNewConfig(fileName, null);
	}

	public Config getNewConfig(String fileName, String[] header)
	{
		File file = this.getConfigFile(fileName);

		if (!file.exists())
		{
			this.prepareFile(fileName);
		}

		Config config = new Config(this.getConfigContent(fileName), file, this.getCommentsNum(file), plugin);

		return config;
	}

	public void prepareFile(String filePath)
	{
		this.prepareFile(filePath, null);
	}

	public void prepareFile(String filePath, String resource)
	{
		File file = this.getConfigFile(filePath);

		if (file.exists())
		{
			return;
		}

		try
		{
			file.getParentFile().mkdirs();
			file.createNewFile();

			if (resource != null)
			{
				if (!resource.isEmpty())
				{
					this.copyResource(plugin.getResource(resource), file);
				}
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void copyResource(InputStream resource, File file)
	{
		try
		{
			OutputStream out = new FileOutputStream(file);

			int length;
			byte[] buf = new byte[1024];

			while ((length = resource.read(buf)) > 0)
			{
				out.write(buf, 0, length);
			}

			out.close();
			resource.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void save(String configString, File file)
	{
		String configuration = this.prepareConfigString(configString);

		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(configuration);
			writer.flush();
			writer.close();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private String prepareConfigString(String configString)
	{
		int lastLine = 0;
		int headerLine = 0;

		String[] lines = configString.split("\n");
		StringBuilder config = new StringBuilder("");

		for (String line : lines)
		{
			if (line.startsWith(this.getPluginName() + "_COMMENT"))
			{
				String comment = "#" + line.trim().substring(line.indexOf(":") + 1);

				if (comment.startsWith("# +-"))
				{
					if (headerLine == 0)
					{
						config.append(comment + "\n");
						lastLine = 0;
						headerLine = 1;
					}
					else if (headerLine == 1)
					{
						config.append(comment + "\n\n");
						lastLine = 0;
						headerLine = 0;
					}
				}
				else
				{
					String normalComment;
					if (comment.startsWith("# ' "))
					{
						normalComment = comment.substring(0, comment.length() - 1).replaceFirst("# ' ", "# ");
					}
					else
					{
						normalComment = comment;
					}

					if (lastLine == 0)
					{
						config.append(normalComment + "\n");
					}
					else if (lastLine == 1)
					{
						config.append("\n" + normalComment + "\n");
					}

					lastLine = 0;
				}
			}
			else
			{
				config.append(line + "\n");
				lastLine = 1;
			}
		}
		return config.toString();
	}
}