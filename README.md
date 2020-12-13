# Memorize sentences

Have you trouble learning a new language? You know the basics of the language but still struggling to build sentences.
You have tried lots of apps on Android/IPhone and you feel still as a beginner. That's because you haven't been given the 
possibility to learn the language reflexes. Everything someone salutes you, you salutes back in the same way you always do.
Not because you are thinking what you are saying. It just comes out because you are so used to these words. 

This web application will give you the possibility to memorize and train the same sentences over and over again. 
The features of this project is:

    - Implement own sentences in any foreign language.
    - Learn how to pronounce them by listen to the media player.
    - Learn how to spell each sentence and check if you have spelled correct.
    - If you don't know how to spell that sentence, there is an option that you can select so the answers reveals.
    - Suitable for mobile, tablet as well as desktop. 

Translate French to your language 

![a](https://raw.githubusercontent.com/DanielMartensson/Learn-French/main/Pictures/Picture%201.png)

It will correct you if you enter wrong sentence

![a](https://raw.githubusercontent.com/DanielMartensson/Learn-French/main/Pictures/Picture%202.png)

You can also do the reverse way

![a](https://raw.githubusercontent.com/DanielMartensson/Learn-French/main/Pictures/Picture%203.png)

Upload new languages

![a](https://raw.githubusercontent.com/DanielMartensson/Learn-French/main/Pictures/Picture%204.png)

Here you can modify your database with the sentences

![a](https://raw.githubusercontent.com/DanielMartensson/Learn-French/main/Pictures/Picture%205.png)

Desktop version

![a](https://raw.githubusercontent.com/DanielMartensson/Learn-French/main/Pictures/Picture%206.png)

# How to upload new languages

1. Begin to create a CSV file that looks like CSV file examples. Go to folder: `Source -> Languages`
2. Download and install `ffmpeg`
3. Select a desired Youtube video e.g `Learn...by sleeping` 
4. Download that video that you find useful. The video need to have that have a very clear speech from a native speaker
5. Cut the video in this example

Where `$1$` is the start time argument, `$2` is how long time the cut should have, `$3` is the name of the `.mp3` file that being saved. 
```
ffmpeg -ss $1 -i input.mp4 -t $2 "$3".mp3
```

Example:

```
ffmpeg -ss 00:12:21 -i YourVideoName.mp4 -t 5 "The food was good".mp3
```

Collect lots of sentences and collect them all into a `.csv` file as I have done. See examples in `Source -> Languages`
 

# How to use pre-made languages

1. Go to folder: `Source -> Languages`
2. Select a CSV file that you like
3. Upload it
4. Select the same audios and upload them as well. You can find them from folder: `Source -> Audios`

# How to install - Ubuntu user

1. Install Java 11, Maven, NodeJS

Java 11
```
sudo apt-get install openjdk-11-jdk
```

Maven
```
sudo apt-get install maven
```

NodeJS
```
curl -sL https://deb.nodesource.com/setup_14.x | sudo -E bash -
sudo apt-get install -y nodejs
```

2. Begin first to install MySQL Community Server

```
sudo apt-get install mysql-server
```

3. Then create a user e.g `myUser` with the password e.g `myPassword`

Login and enter your `sudo` password or mysql `root` password
```
sudo mysql -u root -p
```

Create user with the host `%` <-- That's important if you want to access your server from other computers.
```
CREATE USER 'myUser'@'%' IDENTIFIED BY 'myPassword';
```

Set the privileges to that user
```
GRANT ALL PRIVILEGES ON *.* TO 'myUser'@'%';
```

4. Change your MySQL server so you listening to your LAN address

Open this file
```
/etc/mysql/mysql.conf.d/mysqld.conf
```

And change this
```
bind-address            = 127.0.0.1
```

To your LAN address where the server is installed on e.g
```
bind-address            = 192.168.1.35
```

Then restart your MySQL server
```
sudo /etc/init.d/mysql restart
```

If you don't know your LAN address, you can type in this command in linux `ifconfig` in the terminal

5. Download `Memorize-Sentences`

Download the `Memorize-Sentences` and change the `application.properties` in the `/src/main/resources` folder.
Here you can set the configuration for your database LAN address, user and password.

```
server.port=8080
# Ensure application is run in Vaadin 14/npm mode
vaadin.compatibilityMode = false
logging.level.org.atmosphere = warn

# Database
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.datasource.url=jdbc:mysql://YOUR_MYSQL_SERVER_ADDRESS:3306/MemorizeSentences?createDatabaseIfNotExist=true&serverTimezone=CET
spring.datasource.username=myUserSQL
spring.datasource.password=myPasswordSQL
```

6. Run this project

First stand inside of the folder `Memorize-Sentences` and write inside your terminal
```
mvn spring-boot:run -Pproduction
```

Now you can go to your web browser and type in the local IP address of the computer there you started this Vaadin application.
