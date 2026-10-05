# Verba FGPS - Facebook Group Posts Scraper

This tool was responsible as synchronization mechanism between FB group and site storage
Written in Java and `Selenide Web Driver`. Simple `jar` file scrapes facebook group posts and push them to database

### Important info
The tool has been developed to store data into Firebase.
Currently, export data into `.xsl` file and `postgres` is not supported 

#### What does it scrape
- post text
- post images
- post link
- post date
- author name
- author link
- author avatar

#### How it scrapes
1. tool reads last `N` posts from specified facebook group
2. store them into `database` (needs to be intalled on computer)
3. wait `Z` minutes 
4. go to step 1

`N`, `Z` and `database` parameters are configurable

#### Requirements:  
- java 8;   
- maven 3.6.0
- FireFox 60.3.0esr (64-bit);
- PostgreSQL

#### Database setup:
 1. Create database `fbdata` in PostgreSQL
 2. Run the application once — Hibernate will auto-create the `group_posts` table
 3. After first run, execute the following SQL to add the unique constraint for deduplication:
 ```sql
 ALTER TABLE group_posts ADD CONSTRAINT uk_group_post UNIQUE (group_id, post_id);
 ```
 > This constraint prevents duplicate posts from being inserted on subsequent scheduler runs.

#### Build project: 
 1. Adjust project settings in `PostDataToFirebase.properties` and `application.properties` files
 2. Navigate to project dir from cmd
 2. Run `mvn clean install` command
 
#### Run project: 

 1. Run JAR: `java -jar fbreaper-MILESTONE-2.1.jar --scheduling.enabled=false`
 2. Run JAR with scheduling: `java -jar target\fbreaper-MILESTONE-2.1.jar --scheduling.enabled=true --cron.expression="* */5 * * * *"`

#### Run with Docker (Linux server recommended):
 1. Build JAR first: `mvn clean package -DskipTests`
 2. Build Docker image: `docker build -t fbreaper .`
 3. Run once: `docker run --rm fbreaper --scheduling.enabled=false`
 4. Run with scheduling:
 ```bash
 docker run -d \
   -e SCHEDULING_ENABLED=true \
   fbreaper \
   --scheduling.enabled=true \
   --cron.expression="* */5 * * * *"
 ```
 > Firefox is pre-installed in the Docker image. No display or Xvfb needed — headless is enabled automatically via `--browser.headless=true` in Dockerfile.
 
### Availabel parameters:  
  
**PostDataToFirebase.properties:**  
 
    fb.login fb.pass   
    fb.group.url
    posts.to.fetch 
     
**application.properties:**  
 
    firebase.jsonfile.path
    firebase.storage.bucket
    cron.expression
    fb.big.images.limit
    fb.big.images.load.timeout
    selenide.timeout
    browser.headless     (false = visible browser, true = headless for Linux server)

Questions? Feel free to email me postullat2@gmail.com
