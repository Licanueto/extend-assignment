# Possible improvements

> Here are ideas that crossed my mind while developing this API which for some reason (either time or knowledge) I don't implement, but that I don't want to forget about either.

- Performance / Latency
  - Preemptively retrieve cards  
Save Virtual Cards' IDs on DB. While querying the user, preemptively query its saved cards.  
Once the Virtual Cards' IDs belonging to the user arrive: 
    - If user has a new card/s, retrieve data for the new card/s
    - If a card retrieved no longer belongs to user, ditch the retrieved data for the card (nothing anyway)
    - If user cards match with those on DB (most likely), reply with the preemptively retrieved data


- Quality
  - Add Logger and logs
  - Add CI with to run unit tests and check code lint and style
  - Add Swagger?


- Security
  - Set redis host/password as secret / system variable. Jasypt seems like a good simple solution, but..  
    - Jasypt seems to work only with Maven.
    - Found a Gradle-compatible fork, but doesn't seem to be compatible with (at least) Gradle version 7.4+  
    I'm particularly stuck with this issue https://github.com/moberwasserlechner/jasypt-gradle-plugin/issues/5  
  
  - Change redis server from AWS to GCP or Azure, which encrypt stored data by default, even on redis' free tier which I'm using here. Not knowing this, I chose aws when setting it up as it had the region physically closest to nyc.  
  See: https://docs.redis.com/latest/rc/security/encryption-at-rest/

