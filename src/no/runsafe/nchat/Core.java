package no.runsafe.nchat;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.configuration.IConfigurationFile;

import java.io.InputStream;

public class Core extends RunsafePlugin implements IConfigurationFile
{
    @Override
    protected void PluginSetup()
    {
        this.addComponent(Globals.class);
        this.addComponent(ChatChannelHandler.class);
        this.addComponent(ChatHandler.class);
        this.addComponent(EventManager.class);
        this.addComponent(ChannelCommand.class);
    }

    @Override
    public String getConfigurationPath()
    {
        return Constants.CONFIGURATION_FILE;
    }

    @Override
    public InputStream getDefaultConfiguration()
    {
        return this.getResource(Constants.DEFAULT_CONFIGURATION_FILE);
    }
}
