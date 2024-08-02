package nopey.ibrextras;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import nopey.ibrextras.config.screens.MainScreen;

public class MenuInteg implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() { return MainScreen::new; }
}
