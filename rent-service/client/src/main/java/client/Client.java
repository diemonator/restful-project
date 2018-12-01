package client;

import org.glassfish.jersey.client.ClientConfig;
import service.model.Product;
import service.model.Student;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private static int port = 0;
    public static void main(String[] args)
    {
        int choice = -1;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Set port: ");
        port = scanner.nextInt();
        connect();
        System.out.println("Creating student account (POST)");
        System.out.print("Enter student number: ");
        int studentNr = scanner.nextInt();
        System.out.print("Enter your name: ");
        String name = scanner.next();
        createStudent(name,studentNr);
        while (choice != 0)
        {
            System.out.println("\n**************************************");
            System.out.println("Enter 0 to exit");
            System.out.println("Enter 1 to (GET) the list of rooms");
            System.out.println("Enter 2 search (GET) for rooms by city");
            System.out.println("Enter 3 reserve (PUT) a room");
            System.out.println("Enter 4 cancel (DELETE) reservation");
            System.out.println("**************************************\n");
            switch (choice = scanner.nextInt())
            {
                case 0:
                    break;
                case 1:
                    getListOfRooms();
                    break;
                case 2:
                    System.out.print("Which city: ");
                    String city = scanner.next();
                    getListOfRoomsByCity(city);
                    break;
                case 3:
                    System.out.println("Warning once a reservation is canceled you can no longer reserve it!");
                    System.out.print("Enter the number of the room which you want to reserve: ");
                    int roomNr = scanner.nextInt();
                    reserveRoom(studentNr,roomNr);
                    break;
                case 4:
                    cancelReservation(studentNr);
                    break;
                default:
                    System.out.println("Invalid Input!");
                    continue;
            }
        }
    }

    private static WebTarget creteStudentWebTarget() {
        URI baseURI = UriBuilder.fromUri("http://localhost:"+ port +"/student-rent/rest/students").build();
        javax.ws.rs.client.Client client = ClientBuilder.newClient(new ClientConfig());
        return client.target(baseURI);
    }

    private static WebTarget cretetProductWebTarget() {
        URI baseURI = UriBuilder.fromUri("http://localhost:"+ port +"/student-rent/rest/products").build();
        javax.ws.rs.client.Client client = ClientBuilder.newClient(new ClientConfig());
        return client.target(baseURI);
    }

    private static void createStudent(String name, int id) {
        WebTarget serviceTarget = creteStudentWebTarget();
        Student student = new Student(id,name);
        Entity<Student> entity = Entity.entity(student,MediaType.APPLICATION_JSON);
        Invocation.Builder requestBuilder = serviceTarget.request().accept(MediaType.TEXT_PLAIN);;
        Response response = requestBuilder.post(entity);
        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode())
            System.out.println("createStudent "+student.getName());
        else {
            String error = response.readEntity(String.class);
            System.out.println(error);
        }
    }

    private static void getListOfRooms() {
        WebTarget serviceTarget = cretetProductWebTarget();
        Invocation.Builder requestBuilder = serviceTarget.path("rooms").request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<ArrayList<Product>> genericType = new GenericType<>() {};
            ArrayList<Product> entity = response.readEntity(genericType);
            for (Product room: entity) {
                System.out.println("Room id: "+ room.getRoomNumber() + ", location: "+ room.getLocation() + ", reserved by: "+room.getTenant());
            }
        } else System.err.println("Listing rooms error: "+ response);
    }

    private static void getListOfRoomsByCity(String city) {
        WebTarget serviceTarget = cretetProductWebTarget();
        Invocation.Builder requestBuilder = serviceTarget.path(city).request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            GenericType<ArrayList<Product>> genericType = new GenericType<>() {};
            ArrayList<Product> entity = response.readEntity(genericType);
            for (Product room: entity) {
                System.out.println("Room id: "+ room.getRoomNumber() + ", location: "+ room.getLocation() + ", reserved by: "+room.getTenant());
            }
        } else {
            String error = response.readEntity(String.class);
            System.out.print(error);
        }
    }

    private static void reserveRoom(int studentNr,int roomNr)
    {
        Form form = new Form();
        form.param("studentNr",Integer.toString(studentNr));
        form.param("roomNr",Integer.toString(roomNr));
        Entity<Form> entity = Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED);
        WebTarget serviceTarget = cretetProductWebTarget();
        Invocation.Builder requestBuilder = serviceTarget.request();
        Response response = requestBuilder.put(entity);
        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Success");
        } else System.err.println("Room reservation error: "+ response);
    }

    private static void cancelReservation(int studentNr) {
        WebTarget serviceTarget = cretetProductWebTarget();
        Invocation.Builder requestBuilder = serviceTarget.queryParam("id",Integer.toString(studentNr)).request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.delete();
        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
            System.out.println("Successfully canceled reservation");
        } else {
            String error = response.readEntity(String.class);
            System.out.println(error);
        }
    }

    private static void connect()
    {
        WebTarget serviceTarget = creteStudentWebTarget();
        Invocation.Builder requestBuilder = serviceTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String answer = response.readEntity(String.class);
            System.out.println(answer);
        } else System.err.println("No students"+ response);
    }

}
