package com.gwt.wizard.server;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import com.gwt.wizard.server.entity.ArugamImage;
import com.gwt.wizard.server.jpa.EMF;

/**
 * The server-side implementation of the RPC service.
 */
public class ImageManager
{
    private static final Logger logger = Logger.getLogger(ImageManager.class.getName());

    private static EntityManager getEntityManager()
    {
        return EMF.get().createEntityManager();
    }

    public Long addImage(byte[] image) throws IllegalArgumentException
    {
        ImagesService imagesService = ImagesServiceFactory.getImagesService();

        Image oldImage = ImagesServiceFactory.makeImage(image);
        Transform resize = ImagesServiceFactory.makeResize(200, 300);

        Image newImage = imagesService.applyTransform(resize, oldImage);
        image = newImage.getImageData();
        Long id = null;
        EntityManager em = getEntityManager();
        try
        {
            ArugamImage arugamImage = new ArugamImage();
            arugamImage.setImage(new Blob(image));
            em.getTransaction().begin();
            em.persist(arugamImage);
            em.getTransaction().commit();
            em.detach(arugamImage);
            id = arugamImage.getKey().getId();
        }
        catch (Exception e)
        {
            logger.severe(e.getMessage());
        }
        finally
        {
            em.close();
        }
        return id;
    }

    public byte[] getImage(Long imageId)
    {
        ArugamImage image = getEntityManager().find(ArugamImage.class, imageId);
        return image.getImage().getBytes();
    }
}
