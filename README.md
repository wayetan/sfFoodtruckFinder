# SF Foodtruck Finder

To use this as a command line program, make sure current folder is /src, and run: 
```
javac -cp ../lib/jackson-core-2.9.7.jar:../lib/jackson-databind-2.9.7.jar:../lib/jackson-annotations-2.9.7.jar FoodTruckFinder.java FoodTruck.java
java -cp .:../lib/jackson-core-2.9.7.jar:../lib/jackson-databind-2.9.7.jar:../lib/jackson-annotations-2.9.7.jar FoodTruckFinder
```
This program is used to get current open food trucks using San Francisco government’s API. 

User could see 10 trucks per single ENTER prompt based on current date/time. 

The program use lazy fetch mechanism by calling api using "limit=?" and "offset=?" to make sure the data is fetched only by user request.

For a command line program, it's a fetch-as-needed single thread solution and not involved with local database.

If it's going to be used for a full scale web application, data would be needed to store in a local database or cache for multiple "GET"(read) requests and avoid calling API multiple times. So I would call it fetch-and-store solution. The focus would be more on how to synchronize the database with live data with a "window". The size of the window could be daily, weekly, monthly based on local database storage. Accordingly, update the database every day/week/month. 
