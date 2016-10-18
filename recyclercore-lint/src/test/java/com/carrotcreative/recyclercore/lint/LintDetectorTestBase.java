package com.carrotcreative.recyclercore.lint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.utils.SdkUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;

/**
 * Created by kaushiksaurabh on 10/19/16.
 */

public abstract class LintDetectorTestBase extends LintDetectorTest
{
    protected abstract String getTestResourcesPath();

    @Override
    protected InputStream getTestResource(String relativePath, boolean expectExists)
    {
        String path = (getTestResourcesPath() + relativePath).replace("/", File.separator);
        File file = new File(getTestDataRootDir(), path);
        if(file.exists())
        {
            try
            {
                return new BufferedInputStream(new FileInputStream(file));
            }
            catch(FileNotFoundException e)
            {
                fail("Filed to find file :" + relativePath);
            }
        }
        return super.getTestResource(relativePath, expectExists);
    }

    private File getTestDataRootDir()
    {
        CodeSource source = getClass().getProtectionDomain().getCodeSource();
        if(source != null)
        {
            URL location = source.getLocation();
            try
            {
                File classesDir = SdkUtils.urlToFile(location);
                return classesDir.getParentFile().getAbsoluteFile().getParentFile().getParentFile();
            }
            catch(MalformedURLException e)
            {
                fail(e.getLocalizedMessage());
            }
        }
        return null;
    }
}
