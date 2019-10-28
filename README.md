# Microservices

## Geo: 

Given two points on Earth, this service returns the geodesic distance between them. This service will be used later by a shopping cart application to estimate the drone delivery time. Each point is specified using its latitude (t) and longitude (n) expressed as signed decimal degrees with East of Greenwich being a negative longitude and south of the Equator being a negative latitude. The distance (in km) between two such points is given by:
`12742 * atan2[sqrt(X), sqrt(1-X)]
where X = sin2[(t2-t1)/2] + Y * sin2[(n2-n1)/2]
and Y = cos(t1) * cos(t2)`

## Auth: 

Given a username and password, this service authenticates these credentials and returns "OK" or "FAILURE" accordingly. This service will be used later by the shopping cart application to authenticate users. Authentication is done by adding a long salt to the password; using a cryptographic function to hash the result; and then repeating the process count times. More on this in lecture. The CLIENT table in the Sqlite3 database stores the salt, count, and hash of each user. The table adopts PBKDF2 (Password-Based Key Derivation Function 2) to perform the hash, which is the current best practice.

## Quote: 

Given the ID of a product and a return format (json or xml), this service looks up the ID in the Product table in the hr schema of the Derby database and returns the product's ID, name, and price in the desired format. If the ID is not found, the return should have "<ID> not found" as ID, an empty name, and 0.0 as price. Use GSON and JAXB to serialize and marshal, and use "id", "name", and "price" as identifiers for the json elements and XML nodes. Create a "Product" bean to facilitate the transformation (more on this in lecture).

## Loc: 

Given an address anywhere in the World, this service returns a JSON object representing its specs; most importantly, its latitude and longitude. Use the Google's map API* (https://maps.googleapis.com/maps/api/geocode/json?) and supply the address and your API key to perform this lookup. The URL class in the Java library has a convenient openConnection method that creates an HTTP socket (similar to your service's TCP socket) through which you can communicate with the Google API.

## Stateful: 

This "pedagogical" service delegates to the stateless Geo service in a stateful manner to shed light on session management. It receives the coordinates of the first point in one request and the coordinates of the second point in another. It needs to somehow link the two request (despite the multithreading nature of the service) and then supply all four real numbers to Geo and return its response.

## Gateway: 

This "pedagogical" service sheds light on the challenges involved in building an API Gateway. It adopts the HTTP protocol so you can test it with a browser using the URL: http://host:port/SRV?p1=v1&p2=v2... where SRV is either Geo or Auth or Quote or Loc and where v1, v2, ... are the parameters of the SRV service. The job of this service is thus to discover the needed service (extract its name from the request and find its IP and port if alive); perform inter-protocol transformation; invoke the service; and then return its response.
