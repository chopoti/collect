/*
 * Copyright (C) 2009 University of Washington
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.fsr.collect.android.listeners;

import org.fsr.collect.android.formmanagement.download.FormDownloadException;
import org.fsr.collect.android.formmanagement.ServerFormDetails;

import java.util.Map;

/**
 * @author Carl Hartung (carlhartung@gmail.com)
 */
public interface DownloadFormsTaskListener {
    void formsDownloadingComplete(Map<ServerFormDetails, FormDownloadException> result);

    void progressUpdate(String currentFile, int progress, int total);

    void formsDownloadingCancelled();
}