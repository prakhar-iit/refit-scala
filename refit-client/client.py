#A code to test the socket connection from client to server
import socket

HOST = 'localhost'
PORT = 50010

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))

while True:
    data = s.recv(1024)
    print (repr(data))
s.close()