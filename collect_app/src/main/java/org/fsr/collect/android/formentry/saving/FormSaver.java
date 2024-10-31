package org.fsr.collect.android.formentry.saving;

import android.net.Uri;

import org.fsr.collect.android.javarosawrapper.FormController;
import org.fsr.collect.android.tasks.SaveToDiskResult;
import org.fsr.collect.android.utilities.MediaUtils;
import org.fsr.collect.entities.storage.EntitiesRepository;
import org.fsr.collect.forms.instances.InstancesRepository;

import java.util.ArrayList;

public interface FormSaver {
    SaveToDiskResult save(Uri instanceContentURI, FormController formController, MediaUtils mediaUtils, boolean shouldFinalize, boolean exitAfter,
                          String updatedSaveName, ProgressListener progressListener, ArrayList<String> tempFiles, String currentProjectId, EntitiesRepository entitiesRepository, InstancesRepository instancesRepository);

    interface ProgressListener {
        void onProgressUpdate(String message);
    }
}
