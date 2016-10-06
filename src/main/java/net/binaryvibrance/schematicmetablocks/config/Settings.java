package net.binaryvibrance.schematicmetablocks.config;

import net.binaryvibrance.schematicmetablocks.utility.Reference;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by Steven on 15/05/2015.
 */
public enum Settings
{
    INSTANCE;

    public static final String CATEGORY = Configuration.CATEGORY_GENERAL;
    private static boolean creatorMode = true;

    public static boolean creatorMode()
    {
        return INSTANCE.creatorMode;
    }

    public static void syncConfig(Configuration configFile)
    {
        creatorMode = configFile.getBoolean("creatorMode", Configuration.CATEGORY_GENERAL, true, Reference.SettingsHelp.CreatorMode);

        if(configFile.hasChanged())
            configFile.save();
    }
}
