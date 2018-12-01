using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Data;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json.Serialization;

namespace rest_client
{
    class Program
    {
        static int port = 0;
        static void Main(string[] args)
        {
            Console.Write("Set Port: ");
            port = int.Parse(Console.ReadLine());
            Connect();
            Console.Write("Enter student number: ");
            int studentNr = int.Parse(Console.ReadLine());
            Console.Write("Enter student name: ");
            string name = Console.ReadLine();
            CreateStudent(studentNr, name);
            int choice = -1;
            while (choice != 0)
            {
                Console.WriteLine("\n**************************************");
                Console.WriteLine("Enter 0 to exit");
                Console.WriteLine("Enter 1 to (GET) the list of rooms");
                Console.WriteLine("Enter 2 search (GET) for rooms by city");
                Console.WriteLine("Enter 3 reserve (PUT) a room");
                Console.WriteLine("Enter 4 cancel (DELETE) reservation");
                Console.WriteLine("**************************************\n");
                switch (choice = int.Parse(Console.ReadLine()))
                {
                    case 0:
                        break;
                    case 1:
                        GetListOfRooms();
                        break;
                    case 2:
                        Console.Write("Which city: ");
                        string city = Console.ReadLine();
                        GetListOfRoomsByCity(city);
                        break;
                    case 3:
                        Console.WriteLine("Warning once a reservation is canceled you can no longer reserve it!");
                        Console.Write("Enter the number of the room which you want to reserve: ");
                        int roomNr = int.Parse(Console.ReadLine());
                        ReserveRoom(studentNr, roomNr);
                        break;
                    case 4:
                        CancelReservation(studentNr);
                        break;
                    default:
                        Console.WriteLine("Invalid Input!");
                        continue;
                }
            }
        }

        private static void CancelReservation(int studentNr)
        {
            WebClient client = new WebClient();
            client.QueryString.Add("id", studentNr.ToString());
            var response_data = client.UploadValues("http://localhost:" + port + "/student-rent/rest/products/", "DELETE", client.QueryString);

            // Parse the returned data (if any) if needed.
            var responseString = UnicodeEncoding.UTF8.GetString(response_data);
        }

        private static void ReserveRoom(int studentNr, int roomNr)
        {
            WebClient client = new WebClient();
            var parameters = new NameValueCollection
            {
                { "studentNr", studentNr.ToString() },
                { "roomNr", roomNr.ToString() },
            };

            var response_data = client.UploadValues("http://localhost:" + port + "/student-rent/rest/products/", "PUT", parameters);
            var responseString = UnicodeEncoding.UTF8.GetString(response_data);
        }

        private static void GetListOfRoomsByCity(string city)
        {
            WebClient myClient = new WebClient();
            WebRequest request = WebRequest.Create("http://localhost:" + port + "/student-rent/rest/products/"+city);
            request.Method = "GET";
            WebResponse response = request.GetResponse();
            Stream dataStream = response.GetResponseStream();
            StreamReader reader = new StreamReader(dataStream);
            var responseFromServer = reader.ReadToEnd();
            List<Product> students = JsonConvert.DeserializeObject<List<Product>>(responseFromServer);
            Console.WriteLine("Rooms which are available:");
            foreach (var item in students)
            {
                Console.WriteLine("{0}, {1} and {2}", item.RoomNr, item.Location, item.StudentNr);
            }
            reader.Close();
            response.Close();
        }

        private static void GetListOfRooms()
        {
            WebClient myClient = new WebClient();
            WebRequest request = WebRequest.Create("http://localhost:" + port + "/student-rent/rest/products/rooms");
            request.Method = "GET";
            WebResponse response = request.GetResponse();
            Stream dataStream = response.GetResponseStream();
            StreamReader reader = new StreamReader(dataStream);
            var responseFromServer = reader.ReadToEnd();
            List<Product> students = JsonConvert.DeserializeObject<List<Product>>(responseFromServer);
            Console.WriteLine("Rooms which are available:");
            foreach (var item in students)
            {
                Console.WriteLine("{0}, {1} and {2}",item.RoomNr,item.Location, item.StudentNr);
            }
            reader.Close();
            response.Close();
        }

        private static void Connect()
        {
            WebClient myClient = new WebClient();
            WebRequest request = WebRequest.Create("http://localhost:" + port + "/student-rent/rest/students");
            request.Method = "GET";
            WebResponse response = request.GetResponse();
            Stream dataStream = response.GetResponseStream();  
            StreamReader reader = new StreamReader(dataStream);
            string responseFromServer = reader.ReadToEnd();
            Console.WriteLine(responseFromServer);
            reader.Close();
            response.Close();
        }

        private static void CreateStudent(int id, string name)
        {
            WebRequest request = WebRequest.Create("http://localhost:" + port + "/student-rent/rest/students");
            request.Method = "POST"; 
            string postData = "{ \"id\": " + id + ", \"name\": \""+name+"\" }";
            byte[] byteArray = Encoding.UTF8.GetBytes(postData);
            request.ContentType = "application/json";
            request.ContentLength = byteArray.Length;
            Stream dataStream = request.GetRequestStream();
            dataStream.Write(byteArray, 0, byteArray.Length);
            dataStream.Close();
            using (WebResponse response = request.GetResponse())
            {
                Console.WriteLine(((HttpWebResponse)response).StatusDescription);
                dataStream = response.GetResponseStream();
                using (StreamReader reader = new StreamReader(dataStream))
                {
                    string responseFromServer = reader.ReadToEnd();
                    Console.WriteLine(responseFromServer);
                }
                dataStream.Close();
            }
        }
    }
}
