package org.fsr.collect.android.formentry.saving;

import android.net.Uri;

import org.fsr.collect.android.javarosawrapper.FormController;
import org.fsr.collect.android.tasks.SaveFormToDisk;
import org.fsr.collect.android.tasks.SaveToDiskResult;
import org.fsr.collect.android.utilities.MediaUtils;
import org.fsr.collect.entities.storage.EntitiesRepository;
import org.fsr.collect.forms.instances.InstancesRepository;

import java.util.ArrayList;

public class DiskFormSaver implements FormSaver {

    @Override
    public SaveToDiskResult save(Uri instanceContentURI, FormController formController, MediaUtils mediaUtils, boolean shouldFinalize, boolean exitAfter,
                                 String updatedSaveName, ProgressListener progressListener, ArrayList<String> tempFiles, String currentProjectId, EntitiesRepository entitiesRepository, InstancesRepository instancesRepository) {
        SaveFormToDisk saveFormToDisk = new SaveFormToDisk(formController, mediaUtils, exitAfter, shouldFinalize,
                updatedSaveName, instanceContentURI, tempFiles, currentProjectId, entitiesRepository, instancesRepository);
        return saveFormToDisk.saveForm(progressListener);
    }
}
