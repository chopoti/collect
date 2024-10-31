package org.fsr.collect.android.openrosa;

import org.jetbrains.annotations.Nullable;
import org.kxml2.kdom.Document;
import org.fsr.collect.forms.FormListItem;
import org.fsr.collect.forms.MediaFile;

import java.util.List;

public interface OpenRosaResponseParser {

    @Nullable List<FormListItem> parseFormList(Document document);
    @Nullable List<MediaFile> parseManifest(Document document);
}
