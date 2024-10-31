package org.fsr.collect.settings.migration;

import static org.fsr.collect.settings.migration.MigrationUtils.removeKey;
import static org.fsr.collect.settings.support.SettingsUtils.assertSettingsEmpty;
import static org.fsr.collect.settings.support.SettingsUtils.initSettings;

import org.junit.Test;
import org.fsr.collect.shared.settings.InMemSettings;
import org.fsr.collect.shared.settings.Settings;

public class KeyRemoverTest {

    private final Settings prefs = new InMemSettings();

    @Test
    public void whenKeyDoesNotExist_doesNothing() {
        initSettings(prefs);

        removeKey("blah").apply(prefs);

        assertSettingsEmpty(prefs);
    }
}
