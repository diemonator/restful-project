using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace rest_client
{
    class Student
    {
        [JsonProperty("id")]
        public int StudentNr { get; set; }
        [JsonProperty("name")]
        public string Name { get; set; }

        public Student(int id, string name)
        {
            StudentNr = id;
            Name = name;
        }

        public Student() { }
    }
}
