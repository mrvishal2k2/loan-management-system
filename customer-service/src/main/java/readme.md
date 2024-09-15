#Docs for customer endpoint

## GET, POST, PUT, DELETE : USER

> http://localhost:8080/customer-service/api/customers

#### Post

{
  "customerName": "name",
  "address": "INdia",
  "phoneNo": "1245678",
  "email": "name@example.com",
  "fileStatus": true
}

#### get: no data, just /id

#### put: /id in url and updated full full CustomerDto as above for post

#### delete /id

## PUT : ADMIN

> http://localhost:8080/customer-service/api/customers/update/kyc/id

