# DeliverySystem

## start the server
```shell
mvn package
sh target/bin/webapp
```

## API Description
The API url is "localhost:8080/deliver".

The end point is available with GET and POST request.
 
For the GET request, it is responsible for getting the min cost for
an order, and the request must have one query params "order" which contains the order

>Example : localhost:8080/deliver?order=A-1, C-3,I-2,G-2,D-10,C-10,A-10

And for the POST request, it is responsible for create the system with diff structure and it has
two query params "stocksProductsPath" which is the absolute path of the json file containing the productStocks
and "centersPath" which is the absolute path of the json file containing the centers Graph

>Example : localhost:8080/deliver?stocksProductsPath=/mnt/01D43FA6387D16F0/DeliverySystem/src/main/resources/defaultStocksProducts&centersPath=/mnt/01D43FA6387D16F0/DeliverySystem/src/main/resources/defaultCenters

## Assumptions and Notes

- The GET request's "order" param must be in the next format

>A-1, C-3,I-2,G-2,D-10,C-10,A-10 

As the orders element is separated with commas and for each element its name and quantity should be separated with "-"

- when the server start it initialize the system with stocks products and centers with default system 
which is same as the given in the task doc.

- The system centers structure is flexible with any centers places but for each center they must have a connection to the **drop place**.

- The **Centers** file should look like follows

```
{
  "centers" : [{"c1" : [
    {
      "neighbour": "l1",
      "units": "3"
    },
    {
      "neighbour": "c2",
      "units": "4"
    }
  ]},

    {"c2": [
    {
      "neighbour": "l1",
      "units": "2.5"
    },
    {
      "neighbour": "c1",
      "units": "4"
    },
    {
      "neighbour": "c3",
      "units": "3"
    }
  ]},
    {"c3": [
    {
      "neighbour": "l1",
      "units": "2"
    },
    {
      "neighbour": "c2",
      "units": "3"
    }
  ]},
    {
      "l1": [
        {
          "neighbour": "c1",
          "units": "3"
        },
        {
          "neighbour": "c2",
          "units": "2.5"
        },
        {
          "neighbour": "c3",
          "units": "2"
        }
      ]
    }
    ],
  "dropLocationName" : "l1"
}
```

- The **Stocks** Products file should look like follows

```
{"c1" : [
    {
      "name" : "A",
      "weight" : "3"
    },
    {
      "name" : "B",
      "weight" : "2"
    },
    {
      "name" : "C",
      "weight" : "8"
    }
  ],

    "c2" : [
      {
        "name" : "D",
        "weight" : "12"
      },
      {
        "name" : "E",
        "weight" : "25"
      },
      {
        "name" : "F",
        "weight" : "15"
      }

    ],
    "c3" : [
      {
        "name" : "G",
        "weight" : "0.5"
      },
      {
        "name" : "H",
        "weight" : "1"
      },
      {
        "name" : "I",
        "weight" : "2"
      }

    ]
}
```

- Products Names and Center Names are not case sensitive.
