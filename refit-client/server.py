# import socket
# import string
# import time

# from nltk.tokenize import word_tokenize
# string.punctuation = string.punctuation +'“'+'”'+'-'+'’'+'‘'+'—'
# string.punctuation = string.punctuation.replace('.', '')

# s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# s.bind((socket.gethostbyname("localhost"), 1235))
# s.listen(5)

# print("Server Started......")
# with open("/Users/prakharrastogi/Desktop/refit-scala/src/main/scala/raw_text_1.txt") as fileobject:
# 	print("File Loading......")
# 	for line in fileobject:
# 		print(line)
# 		line_new = line.replace("\n", " ")
# 		preprocessedCorpus = "".join([char for char in line_new if char not in string.punctuation])
# 		words = word_tokenize(preprocessedCorpus)

# 		for word in words:
# 			print(word)
# 			clientsocket, address = s.accept()
# 			print(clientsocket)
# 			print(address)
# 			clientsocket.sendall(bytes(word, "utf-8"))
# 		print("Words end.....")
# 	print("Line end.....")

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
				#print(word)
				conn.send(bytes(word +'\n', "utf-8"))    
				#time.sleep(2)
	print("File Streamed Completely....")
	conn.close()