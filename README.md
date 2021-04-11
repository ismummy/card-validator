## Java Backend Interview Test
We want to give our customer the best experience possible. So we should inform
 them about the details of his/her card like:
* valid/not valid
* the scheme (i.e VISA, MASTERCARD or AMEX)
* the bank when it is available

####Question 1
Create a simple Spring REST API service that will understand the following call and response structure:
1. Authenticate the request using these values passed in the header.
   appKey = “test_20191123132233”
   timeStamp = “1617953042”
   hashed = “4n+F7tDHDaFCoPkDDCtHMX6fvNIolyzMLFONT5c4XSYBg7VYFg1uMDYW7b3wDOs+rKL4QjaY2A100Jufsg
   1XFA==”
   

    {
       authorization: 3line + “ ” + hashed*,
       timeStamp: timestamp*,
       appKey: appKey* 
    }

   - a. Note that in the JSON header payload, all values with asterisks are variables and should be
   replaced accordingly.
   - b. The hashed value is generated using a SHA-512 hash of the appKey + timestamp
   - c. Verify that these values are present in the headers, else return an invalid message request.
   - d. If authorization value does not match after comparing, return an invalid authorization key
   message.

####Question 2
2. Verify Card: GET /card-scheme/verify/234564562....


    {
       "success": true,
        "payload": {
           "scheme": "visa",
           "type": "debit",
           "bank": "UBS"
        }
    }

####Question 3
3. Number of hits (Hit Count): GET /card-scheme/stats?start=1&limit=3
   
       {
          "success": true,
          "start": 1,
          "limit": 3,
          "size": 133,
          "payload": {
          "545423": 5,
          "679234": 4,
          "329802": 1
          }
       }

###SOLUTION
####Assumption/Third Party Resources
- In Order to get card scheme and provider bank, I used an open source [https://lookup.binlist.net](https://lookup.binlist.net)
- In order to count the hit count I used local/temp database h2

#### Deployment Link
- The link to the api on PAAS(Heroku) [here](http://q2-card-validator.herokuapp.com/api/v1/card-scheme/).

- The github repository for my submission [here](https://github.com/ismummy/card-validator).

- The app can be cloned ran locally and tested with postman, on Local, its run on PORT: 3003.

Example request.
- http://localhost:3003/api/v1/card-scheme/verify/5399419107596587
- http://q2-card-validator.herokuapp.com/api/v1/card-scheme/verify/5399419107596587

response:
```json
{
    "success": true,
    "payload": {
        "scheme": "mastercard",
        "type": "debit",
        "bank": "ZENITH BANK"
    }
}
```

- localhost:3003/api/v1/card-scheme/verify/1234
- http://q2-card-validator.herokuapp.com/api/v1/card-scheme/verify/1234
```json
{
  "error": "400 BAD_REQUEST",
  "message": "Invalid Card Number Input"
}

```

- localhost:3003/api/v1/card-scheme/stats?start=1&limit=3
- http://q2-card-validator.herokuapp.com/api/v1/card-scheme/stats?start=1&limit=3

```json
{
   "success": true,
   "start": 1,
   "limit": 3,
   "size": 3,
   "payload": {
      "539923": 1,
      "539941": 1,
      "539983": 1
   }
}
```
#####Olalekan, ISMAIL