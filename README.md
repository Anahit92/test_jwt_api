# test_jwt_api

**Use the Docker to build and test locally**

1. mvn clean
2. mvn compile
3. mvn package
4. run Dockerfile 
5. run docker-compose.yml

 
Registration API link:      _/authenticate/register_
Method: POST

Headers:
    key: Content-Type
    value: application/x-www-form-urlencoded

Body:
    key: phone 
    value: 4200103662
    key: id_number 
    value: 9999999999
    key: password 
    value: 4200103662
    key: re_password 
    value : 4200103662
    
Login API Link:             _/authenticate/login_
Method: POST

Headers:
    key: Content-Type
    value: application/x-www-form-urlencoded

Body:
    key: id_number 
    value: 9999999999
    key: password 
    value: 4200103662

Return:
    {
        "token": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJ0ZXN0VG9rZW4iLCJzdWIiOiI5OTk5OTk5OTk5IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYwNzAxMTcyNywiZXhwIjoxNjA3MDEyMzI3fQ.XOrB_addIR5iRzHOnRSz00cpXILqpsx5Lys9KR-_9CIM3EsmkAxV60Hny-7YU05cJRLajqQ4Nas2S3QfRPN4ng"
    }

User verification API link: _/authenticate/verify_
Method: POST

Headers:
    key: Content-Type
    value: application/x-www-form-urlencoded
    key: Authorization 
    value: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJ0ZXN0VG9rZW4iLCJzdWIiOiI5OTk5OTk5OTk5IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYwNzAxMTcyNywiZXhwIjoxNjA3MDEyMzI3fQ.XOrB_addIR5iRzHOnRSz00cpXILqpsx5Lys9KR-_9CIM3EsmkAxV60Hny-7YU05cJRLajqQ4Nas2S3QfRPN4ng

Body: 
    key: token
    value: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJ0ZXN0VG9rZW4iLCJzdWIiOiI5OTk5OTk5OTk5IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYwNzAxMTcyNywiZXhwIjoxNjA3MDEyMzI3fQ.XOrB_addIR5iRzHOnRSz00cpXILqpsx5Lys9KR-_9CIM3EsmkAxV60Hny-7YU05cJRLajqQ4Nas2S3QfRPN4ng

Retrun:
    {
        "id_number": "9999999999",
        "phone": "4200103662"
    }

Cards information API link: _/data/cards_
Method: POST

Headers:
    key: Content-Type
    value: application/x-www-form-urlencoded
    key: Authorization 
    value: Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJ0ZXN0VG9rZW4iLCJzdWIiOiI5OTk5OTk5OTk5IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYwNzAxMTcyNywiZXhwIjoxNjA3MDEyMzI3fQ.XOrB_addIR5iRzHOnRSz00cpXILqpsx5Lys9KR-_9CIM3EsmkAxV60Hny-7YU05cJRLajqQ4Nas2S3QfRPN4ng

Body: 
    key: token
    value: Bearer Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJ0ZXN0VG9rZW4iLCJzdWIiOiI5OTk5OTk5OTk5IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTYwNzAxMTcyNywiZXhwIjoxNjA3MDEyMzI3fQ.XOrB_addIR5iRzHOnRSz00cpXILqpsx5Lys9KR-_9CIM3EsmkAxV60Hny-7YU05cJRLajqQ4Nas2S3QfRPN4ng

return:
    {
        "currency_accounts": [
            {
                "amount": "200",
                "currency": "USD"
            },
            {
                "amount": "50",
                "currency": "EUR"
            }
        ],
        "card_number": "4444-5555-0000-5555",
        "total_amount": 260.5766900896535,
        "card_type": "MASTERCARD"
    }

