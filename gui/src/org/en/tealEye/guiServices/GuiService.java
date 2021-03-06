package org.en.tealEye.guiServices;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Properties;

public interface GuiService {

    Map<String, Font> getFontMap();

    Map<String, ImageIcon> getIconMap();

    void updateProps(Properties props);

    void updateProperty(String key, String value);

    void updateConfigFile();
//    void updateIcons();

//    void updateFonts();

}

