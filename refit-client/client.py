# import socket

# s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# s.connect((socket.gethostbyname("localhost"), 1235))

# while True:
#     msg = s.recv(1024)
#     print (repr(msg))
#     print(msg.decode("utf-8"))
#     print("....")
import socket

HOST = 'localhost'
PORT = 50010

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))

while True:
    data = s.recv(1024)
    print (repr(data))
s.close()