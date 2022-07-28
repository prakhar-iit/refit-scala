# A python client to stream the words to the Flink Data stream

import socket
import time
from random import randint
import string
from nltk.tokenize import word_tokenize

HOST = 'localhost'
PORT = 50012

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(2)
string.punctuation = string.punctuation +'“'+'”'+'-'+'’'+'‘'+'—'
print("Server Started....")

while True:
	conn, addr = s.accept()
	print ("Client connection accepted", addr)
	with open("/Users/prakharrastogi/Desktop/refit-scala/src/main/scala/raw_text.txt") as fileobject:
		for line in fileobject:
			preprocessedCorpus = "".join([char for char in line if char not in string.punctuation])
			words = word_tokenize(preprocessedCorpus)
			for word in words:
				conn.send(bytes(word +'\n', "utf-8"))
	print("File Streamed Completely....")
	conn.close()