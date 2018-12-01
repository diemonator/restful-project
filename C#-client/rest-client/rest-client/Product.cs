using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Runtime.Serialization;
using System.ServiceModel;
using Newtonsoft.Json;

namespace rest_client
{
    class Product
    {
        [JsonProperty("tenant")]
        public int StudentNr { get; set; }
        [JsonProperty("roomNumber")]
        public int RoomNr { get; set; }
        [JsonProperty("location")]
        public string Location { get; set; }

        public Product(int studentNr, int roomNr, string location)
        {
            StudentNr = studentNr;
            RoomNr = roomNr;
            Location = location;
        }

        public Product() { }
    }
}
