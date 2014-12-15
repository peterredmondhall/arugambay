package com.gwt.wizard.server;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.io.Resources;
import com.gwt.wizard.server.entity.ArugamImage;

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

    @Test
    public void should_dump() throws IOException
    {
        URL url = Resources.getResource("Boston City Flow.jpg");
        byte[] image = Resources.toByteArray(url);

        Long id = manager.addImage(image);

        String dump = manager.dump(ArugamImage.class);
        System.out.println(dump);

    }
}
