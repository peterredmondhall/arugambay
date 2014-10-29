package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class ImageManagerTest
{

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    ImageManager manager = new ImageManager();

    @Before
    public void setUp()
    {
        helper.setUp();
    }

    @After
    public void tearDown()
    {
        helper.tearDown();
    }

    @Test
    public void should_create_an_image()
    {
        byte[] image = "xyz".getBytes();
        Long id = manager.addImage(image);
        image = manager.getImage(id);
        assertEquals(image.length, 3);
        assertEquals(image[0], 'x');
        assertEquals(image[1], 'y');
        assertEquals(image[2], 'z');
    }
}
