# Extend Assignment  

Assignment for backend position at Extend.

## Idea behind
The requirement is to consume Extend's public API as would any client trying to integrate Extend's services to their own system.  

This application assumes a microservice environment where it would be consumed by other internal APIs. It also assumes 
the users within the organization may or may not have an Extend account.
That is why it holds the credentials for each user. So, in order to use the application one has to add the user's Extend's acount. 
Once added it can be queried without them. Take into consideration this is nowhere near production-ready as the 
credentials are being saved in plaintext without encryption on a redis database in the cloud.

## How to use?

Set the values shared by email for these properties located in the `application.properties` file on the root of the project:
>spring.redis.host   
>spring.redis.password

Then run locally with `./gradlew bootRun`

Once running add a user and its Extend's credentials:
```
curl -L -X POST 'localhost:8080/user' \
-H 'Content-Type: application/json' \
--data-raw '{
  "id": "68e75c4c-a5f1-4872-9d4a-747caac43257",
  "extendEmail": "user.email@extend-client.com",
  "extendPassword": "extendPassword" }'
  ```
>Note the `id` is a string in UUID format, you can use [any UUID you like](https://duckduckgo.com/?t=ffab&q=random+uuid&ia=answer).

To verify the user has effectively been added you can check out the full list of users by means of this request:
```
curl -L -X GET 'localhost:8080/users'
```
>Please bear in mind this request in particular is for testing purposes only as unlike the others this one is quite inefficient.

If the organization allows users to freely access their Extend credentials/accounts they may change their password,
in which case the password needs to be updated in the application using the users's ID with the following request:
```
curl -L -X PUT 'localhost:8080/user' \
-H 'Content-Type: application/json' \
--data-raw '{
"id": "68e75c4c-a5f1-4872-9d4a-747caac43257",
"extendEmail": "user.email@extend-client.com",
"extendPassword": "newPassword" }'
```

If for whatever reason the user should no longer exist it can be removed using its ID and the following request:
```
curl -L -X DELETE 'localhost:8080/user/68e75c4c-a5f1-4872-9d4a-747caac43257' \
-H 'Content-Type: application/json' \
--data-raw '{
  "id": "68e75c4c-a5f1-4872-9d4a-747caac43257",
  "extendEmail": "john.smith@seriousextendclient.com",
  "extendPassword": "l337P@szswrd" }'
```

Finally, if you wish to see the virtual cards and their transactions for a given user you can do so with this request:
```
curl -L -X GET 'localhost:8080/virtualcards/{user-id}'
```
which, if you copypasted the requests above, comes down to:
```
curl -L -X GET 'localhost:8080/virtualcards/68e75c4c-a5f1-4872-9d4a-747caac43257'
```

Lastly if you want to run the tests you can do so with `./gradle test`

Also, in the file [Improvements.md](https://github.com/Licanueto/extend-assignment/blob/develop/improvements.md) located
in the root of the project are listed things that crossed my mind that 
I've not managed to implement, but that could be on the backlog for a future release.
