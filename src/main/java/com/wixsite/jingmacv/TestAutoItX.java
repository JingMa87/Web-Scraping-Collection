package com.wixsite.jingmacv;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;

public class TestAutoItX {

	@Test
	public void test() {
		File file = new File("lib", "jacob-1.18-x64.dll"); //path to the jacob dll
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());

        AutoItX x = new AutoItX();
        String notepad = "Untitled - Notepad";
        String testString = "this is a test.";
        x.run("notepad.exe");
        x.winActivate(notepad);
        x.winWaitActive(notepad);
        x.send(testString);
        Assert.assertTrue(x.winExists(notepad, testString));
        x.winClose(notepad, testString);
        x.winWaitActive("Notepad");
        x.send("{ALT}n");
        Assert.assertFalse(x.winExists(notepad, testString));
	}
}
