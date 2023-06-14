![image](https://github.com/omerugi/SpringCampinApp/assets/57361655/fed90094-e015-4a06-9aca-3f0f08e6a4fe)
# Campaign App

<a href="https://spring.io/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="40" height="40"/> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/> </a><a href="https://www.postgresql.org" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/postgresql/postgresql-original-wordmark.svg" alt="postgresql" width="40" height="40"/>

  This project is to create a campaign creator, the idea is to let a seller add their product with a category that it's related to and then create campaigns for the products with a bid to push and promote their goods in the platform.
  
  To find the highest bided product under a certain category the system is pulling them based on the highest bid of the campaign the product is related to.
  
  Also, campaigns have an "expiration date" and after 10 days they become inactive using the scheduler in the app that will run every day to update.
  
### Assumptions
  * Products are inserted with there own serial number that is manually, as there can be many product with different serial number that are not platform related.
  * Campaigns that are created/updated with start day that is more then 10 days ago will be saved but inactive.
  * Campaigns names are not unique as there can be many campaigns in the long run that are not usually deleted but the names could be used again.
  * Category was crated as an entity as it seems more appropriate.
  * Category cannot be deleted if it's attached to a product. (Should migrate the data before deleting).

# Project Stractue
  Each one of the entities has a controller, service, and repo as part of the design and also a DTO with constraints for API validation + projection class for custom DB response for serveAd call.
  
  There are few util class for general class based functions and a const class to store the messages sent by the app.
  
  Also, there is a exception handler + logger that follows the project, the exc handler, and the campaign scheduler. 
  
# Run The project
  * JDK 17
  * Postgres
  * application.properties replace the DB credentials.
  
# Data Dase
  The database is based on Postgres for the app and H2 for the tests with 3 tables each representing an entity.
  
  * Category - represent the categories a product can choose to be under.
    * Id - a unique generated long.
    * Name - a unique non null string that and limited to be at size 2-25.
    * Products - a one-to-many relation with product (several products can have the same category).
  * Product - represent product that the seller is adding to the system.
    * Product serial number - a uniqe user generated string that can be a mix of numbers and letter and limited to be at size 2-25.
    * Title - a unique non null string that and limited to be at size 2-25.
    * Price - a non negative double.
    * Active - a boolean with default as true.
    * Campaigns - a many-to-many relation with campaigns.
    * Category - a non null many-to-one relation with a category. (each product most have at least one).
  * Campaign - represent campaigns that a user is creating.
    * id - a unique generated long.
    * Name - a non null string that and limited to be at size 2-25.
    * Start date - a non null date of yyyy-MM-dd.
    * Bid - a non negative double.
    * Product - a many-to-many relation with product.
 
 # API's
  Each one of the entities has it's own RestAPI that follows CRUD operations.
  * Category:
    * **POST** ```.../category``` - receives as a payload a valid category to save/update, save without id in the payload update with.
    * **GET** ```.../category/{id}``` - receives an id as a path var and return the category with that id if it was found.
    * **DELET** ```.../category/{id}``` - receives an id as a path var and delete the category **only** if no product is using it.
  * Product:
    * **POST** ```.../product``` - receives as a payload a valid product to save/update, save without id in the payload update with.
    * **GET** ```.../product/{serialNumber}``` - receives a serialNumber as a path var and return the product with that serialNumber if it was found.
    * **DELET** ```.../product/{serialNumber}``` - receives an serialNumber as a path var and delete it from the DB without deleting campaigns.
    * **GET** ```.../product/serveAd/{category}``` - receives a category name and return the product with the highet bid from that category, if there is non, will return from another.
  * Campaign:
    * **POST** ```.../campaign``` - receives as a payload a valid campaign to save/update, save without id in the payload update with.
    * **GET** ```.../campaign/{id}``` - receives an id as a path var and return the campaign with that id if it was found.
    * **DELET** ```.../campaign/{id}``` - receives an id as a path var and delete the it from the DB without deleting products.
  
# Campaign Scheduler
  To implement the logic of "campaigns with start date that is more then 10 days ago sould be inactive" there is a scheduler with a function that runs once a day at mid night and updates the DB, meaning that every campaign with start date that is more then 10 days ago will be inactive.
  
  To change the timing can modify application.properties ``app.scheduler.time=`` to the cron time desired.
  
# Test
  There is also unit test and integration tests using mockito that is running on H2 test DB.
  
  In the test folder the are `.sql` for creating the tables, inserting date, and dropping the tables after usage.
