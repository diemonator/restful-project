package service.endpoint;


import service.model.Product;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("products")
public class ProductsResources {
    private List<Product> rooms = new ArrayList<>();

    public ProductsResources() {
        rooms.add(new Product(0,1,"Eindhoven"));
        rooms.add(new Product(0,2,"Eindhoven"));
        rooms.add(new Product(0,3,"Helmond"));
        rooms.add(new Product(0,4,"Venlo"));
    }

    @GET
    @Path("rooms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getRooms()
    {
        return rooms;
    }

    @GET
    @Path("{city}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableRoomsByCity(@PathParam("city") String city) {
        ArrayList<Product> temp = new ArrayList<>();
        for (Product room: rooms) {
            if (room.getTenant() == 0 && room.getLocation().equals(city))
                temp.add(room);
        }
        if (temp.size() == 0)
            return Response.serverError().entity("No such city or everything is rented out.").build();
        return Response.ok(temp).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void reserveRoom(@FormParam("studentNr") int studentNr, @FormParam("roomNr") int roomNr) {
        Product room = findRoom(roomNr);
        if (studentNr != 0 && room != null ) {
            if (room.getTenant() == 0)
                room.setTenant(studentNr);
            else
                throw new RuntimeException("Room reserved already");
        }
    }

    private Product findRoom(int roomNr)
    {
        for (Product room: rooms) {
            if (room.getRoomNumber() == roomNr)
                return room;
        }
        return null;
    }

    @DELETE
    public Response cancelReservation(@QueryParam("id") int studentNr)
    {
        Product room = null;
        for (Product item : rooms) {
            if (item.getTenant() == studentNr)
                room = item;
        }
        if (room == null)
            return Response.serverError().entity("You have no reservations").build();
        else {
            rooms.remove(room);
            return  Response.noContent().build();
        }
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String connectProducts() {
        return "Here you can reserve a room";
    }
}
