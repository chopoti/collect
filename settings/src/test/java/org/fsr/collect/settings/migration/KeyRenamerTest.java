package org.fsr.collect.settings.migration;

import static org.fsr.collect.settings.migration.MigrationUtils.renameKey;
import static org.fsr.collect.settings.support.SettingsUtils.assertSettings;
import static org.fsr.collect.settings.support.SettingsUtils.initSettings;

import org.junit.Test;
import org.fsr.collect.shared.settings.InMemSettings;
import org.fsr.collect.shared.settings.Settings;

public class KeyRenamerTest {

    private final Settings prefs = new InMemSettings();

    @Test
    public void renamesKeys() {
        initSettings(prefs,
                "colour", "red"
        );

        renameKey("colour")
                .toKey("couleur")
                .apply(prefs);

        assertSettings(prefs,
                "couleur", "red"
        );
    }

    @Test
    public void whenNewKeyExists_doesNotDoAnything() {
        initSettings(prefs,
                "colour", "red",
                "couleur", "blue"
        );

        renameKey("colour")
                .toKey("couleur")
                .apply(prefs);

        assertSettings(prefs,
                "colour", "red",
                "couleur", "blue"
        );
    }
}
