package org.fsr.collect.settings.migration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.fsr.collect.settings.migration.MigrationUtils.moveKey;
import static org.fsr.collect.settings.support.SettingsUtils.assertSettings;
import static org.fsr.collect.settings.support.SettingsUtils.initSettings;

import org.junit.Test;
import org.fsr.collect.shared.settings.InMemSettings;
import org.fsr.collect.shared.settings.Settings;

public class KeyMoverTest {

    private final Settings prefs = new InMemSettings();
    private final Settings other = new InMemSettings();

    @Test
    public void movesKeyAndValueToOtherPrefs() {
        initSettings(prefs,
                "key", "value"
        );

        moveKey("key")
                .toPreferences(other)
                .apply(prefs);

        assertThat(prefs.getAll().size(), is(0));
        assertSettings(other,
                "key", "value"
        );
    }

    @Test
    public void whenKeyNotInOriginalPrefs_doesNothing() {
        moveKey("key")
                .toPreferences(other)
                .apply(prefs);

        assertThat(prefs.getAll().size(), is(0));
        assertThat(other.getAll().size(), is(0));
    }

    @Test
    public void whenKeyInOtherPrefs_doesNothing() {
        initSettings(prefs,
                "key", "value"
        );

        initSettings(other,
                "key", "other-value"
        );

        moveKey("key")
                .toPreferences(other)
                .apply(prefs);

        assertSettings(prefs,
                "key", "value"
        );

        assertSettings(other,
                "key", "other-value"
        );
    }
}
