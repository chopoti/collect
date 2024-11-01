package org.fsr.collect.formstest;

import org.junit.Before;
import org.fsr.collect.forms.FormsRepository;
import org.fsr.collect.shared.TempFiles;

import java.util.function.Supplier;

public class InMemFormsRepositoryTest extends FormsRepositoryTest {

    private String tempDirectory;

    @Before
    public void setup() {
        tempDirectory = TempFiles.createTempDir().getAbsolutePath();
    }

    @Override
    public FormsRepository buildSubject() {
        return new InMemFormsRepository(savepointsRepository);
    }

    @Override
    public FormsRepository buildSubject(Supplier<Long> clock) {
        return new InMemFormsRepository(clock, savepointsRepository);
    }

    @Override
    public String getFormFilesPath() {
        return tempDirectory;
    }
}
