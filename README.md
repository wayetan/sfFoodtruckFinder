# SF Foodtruck Finder
This program is used to get current open food trucks using San Francisco government’s API. 
User could see 10 trucks per single ENTER prompt based on current date/time.
## Installation
To use this as a command line program, make sure current folder is /src, and run: 
```
javac -cp ../lib/jackson-core-2.9.7.jar:../lib/jackson-databind-2.9.7.jar:../lib/jackson-annotations-2.9.7.jar FoodTruckFinder.java FoodTruck.java
java -cp .:../lib/jackson-core-2.9.7.jar:../lib/jackson-databind-2.9.7.jar:../lib/jackson-annotations-2.9.7.jar FoodTruckFinder
```
## Implementation Details: 
For a command line program, it's a fetch-as-needed single thread solution and not involved with local database.

The program use lazy fetch mechanism by calling api using "limit=?" and "offset=?" to make sure the data is fetched only by user request. Further more, the result page is trimmed for exact 10 results every time. Since not all the result returned by API are going to be valid, if not valid, fetch more until the size is achieved. 

For example: 
Initial state: limit = 10, means fetch 10 trucks, 6 out of 10 is valid. 
Next state: limit = 4 since 4 more trucks needed, and so on.. 

The downside of this implementation is when most of the trucks is not valid, and it will keep calling the api until the result set size is achieved. It'll be implemented better if with a local storage solution. 

## Extend to Full-scale Web Application.
If it's going to be used for a full scale web application, data is better to be stored in a local database or cache for multiple "GET"(read) requests and avoid calling API multiple times. It's more like a fetch-and-store solution. The focus would be more on how to synchronize the database with live data with a "window". The size of the window could be daily, weekly, monthly based on local database storage. Accordingly, update the database every day/week/month. 
