package org.fsr.collect.settings.migration;

import static org.fsr.collect.settings.migration.MigrationUtils.replace;

import org.fsr.collect.shared.settings.Settings;

public class KeyRenamer implements Migration {

    String oldKey;
    String newKey;

    KeyRenamer(String oldKey) {
        this.oldKey = oldKey;
    }

    public KeyRenamer toKey(String newKey) {
        this.newKey = newKey;
        return this;
    }

    public void apply(Settings prefs) {
        if (prefs.contains(oldKey) && !prefs.contains(newKey)) {
            Object value = prefs.getAll().get(oldKey);
            replace(prefs, oldKey, newKey, value);
        }
    }
}
