package org.fsr.collect.settings.migration;

import static org.fsr.collect.settings.migration.MigrationUtils.translateValue;
import static org.fsr.collect.settings.support.SettingsUtils.assertSettings;
import static org.fsr.collect.settings.support.SettingsUtils.initSettings;

import org.junit.Test;
import org.fsr.collect.shared.settings.InMemSettings;
import org.fsr.collect.shared.settings.Settings;

public class ValueTranslatorTest {

    private final Settings prefs = new InMemSettings();

    @Test
    public void translatesValueForKey() {
        initSettings(prefs,
                "key", "value"
        );

        translateValue("value").toValue("newValue").forKey("key").apply(prefs);

        assertSettings(prefs,
                "key", "newValue"
        );
    }

    @Test
    public void doesNotTranslateOtherValues() {
        initSettings(prefs,
                "key", "otherValue"
        );

        translateValue("value").toValue("newValue").forKey("key").apply(prefs);

        assertSettings(prefs,
                "key", "otherValue"
        );
    }

    @Test
    public void whenKeyNotInPrefs_doesNothing() {
        initSettings(prefs,
                "otherKey", "value"
        );

        translateValue("value").toValue("newValue").forKey("key").apply(prefs);

        assertSettings(prefs,
                "otherKey", "value"
        );
    }
}
