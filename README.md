# REFIT SCALA/FLINK

Flink is a stream processing and batch processing framework. Scala is programming language that I used to code and used Scala Flink's API for all data processing.

In this project, I have created a python client to stream words one by one. To create a client, I used flask server and created a socket connection between the python server and scala client.

I have also removed all prepositions, new lines, carriage returns, multiple spaces and tabs characters in the python script.

On the Scala Node, I created a socket connection with python client on a given server and then created Flink environment to execute the Streams.

I am using Stream windowing to create n-grams (in our case bigrams where n is 2) and then processing the n-grams/bigrams to find the probabiltiy distribution of each token.

I am setting a limit of 200000 words as a training and finding the probabiltiy distribution of each word to predict the next 5 words.


Once the 200000 words are streamed, then I have started using predict_next_5_words method to predict the next 5 words.


Note: In this project, I haven't done a lot of pre-processing and cleaning of corpus like removal of stop words and removal of numerics, etc.
But we can write few methods and lines of code to do the cleaning of data and it will improve the predictability of the next 5 words.


# Pre-Requisites
Install Scala@2.12
```bash
brew install scala@2.12
```
Install Scala Plugin in Intellij from Marketplace

Install Flink if not installed in your machine

Install sbt - Scala build tool to build the scala projects

Note: Flink is compatible with Scala 2. I tried to fetch Flink Libraries for Scala 3 and it didn't work. Maybe they are not supporting for now.

# Run the program
Python Streaming Client is present in refit-client/server.py. Run the python server.
```bash
python server.py
```

The Scala code can be run by adding a configuration in Intellij.


# Important Links
These links helped me a lot to understand regex, scala and flink apis, etc.
```bash
https://medium.com/factory-mind/regex-tutorial-a-simple-cheatsheet-by-examples-649dc1c3f285
https://medium.com/factory-mind/regex-cookbook-most-wanted-regex-aa721558c3c1

https://www.analyticsvidhya.com/blog/2022/01/building-language-models-in-nlp/
https://stackoverflow.com/questions/54166390/flink-datastream-keyby-api
https://nightlies.apache.org/flink/flink-docs-release-1.0/apis/streaming/windows.html#windows-on-unkeyed-data-streams
https://stackoverflow.com/questions/41931495/flink-streaming-apply-function-in-windows
https://regex101.com/r/cO8lqs/4
https://gist.github.com/rambabusaravanan/a0811f8c9bff440f06ca04d06abdd363

To remove special characters from string:    /[^a-zA-Z ]/g
To remove special characters from string except numbers and underscore [^a-zA-Z0-9_]/g

```

# Git Commands
Set User name and email for git config<br/>
```bash
git config --global user.name "Prakhar Rastogi" 
git config --global user.email email_address
```

Generate Keygen
```bash
ssh-keygen -t ed25519 -C "email_address"<br/>
ssh-add -K ~/.ssh/id_ed25519<br/>
ssh-add -K ~/.ssh/id_ed25519
```

Add ssh key in github<br/>

# Other Important Git Commands
```bash
git clone git@github.com:prakhar-iit/refit-scala.git<br/>
git init<br/>  
git add --all<br/>
git commit -m "Message while Committing"<br/>
git push -u origin main<br/>

git pull<br/>
```

