package org.fsr.collect.settings.migration;

import org.fsr.collect.shared.settings.Settings;

public interface Migration {
    void apply(Settings prefs);
}
