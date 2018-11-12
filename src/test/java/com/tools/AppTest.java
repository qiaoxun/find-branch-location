package com.tools;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        try {
            Runtime.getRuntime().exec("cmd /c  cd C:\\Project\\ppm942-patches && start \"\" \"C:\\Joey\\software\\Git\\git-bash.exe\" --login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
