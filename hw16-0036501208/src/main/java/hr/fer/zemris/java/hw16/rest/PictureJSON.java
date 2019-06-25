package hr.fer.zemris.java.hw16.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import hr.fer.zemris.java.hw16.gallery.PicturesDB;

/**
 * A class using REST technology for returning
 * the state of the web-application.
 *
 * @author Filip Nemec
 */
@Path("/gallery")
public class PictureJSON {
	
	/**
	 * Returns the JSON encoded collection of all the picture tags.
	 *
	 * @return the JSON encoded collection of all the picture tags
	 */
	@Path("tags")
	@GET
	@Produces("application/json")
	public Response getAllTags() {
		String json = new Gson().toJson(PicturesDB.getAllTags());
		return Response.status(Status.OK).entity(json).build();
	}

    /**
     * Returns the JSON encoded list of all the pictures with the specified tag.
     * 
     * @param tag the tag
     * @return the JSON encoded list of all the pictures with the specified tag
     */
    @Path("{tag}")
    @GET
    @Produces("application/json")
    public Response getPicturesForTag(@PathParam("tag") String tag) {
        String json = new Gson().toJson(PicturesDB.getPicturesWithTag(tag));
        return Response.status(Status.OK).entity(json).build();
    }
    
    /**
     * Returns the JSON encoded {@code Picture} with the specified title.
     *
     * @param title the title of the {@code Picture}
     * @return the JSON encoded {@code Picture} with the specified title
     */
    @Path("picture/{title}")
    @GET
    @Produces("application/json")
    public Response getOriginalPicture(@PathParam("title") String title) {
    	String json = new Gson().toJson(PicturesDB.getPictureWithTitle(title));
    	return Response.status(Status.OK).entity(json).build();
    }
}