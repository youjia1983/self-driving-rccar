#--- self-driving radio controlled car
#--- make sure to run it as root (sudo)

import socket, serial

#--- initializations and stuff
x = 0
y = 0
theta = 0

#--- open serial port to arduino
#ser = serial.Serial('/dev/tty.usbserial', 9600)

#--- open udp socket to get measurement data
port = 12346
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind(("", port))

while True:
    try:
        data, addr = s.recvfrom(1024)
        meas = eval(data)
        
        #--- for now, just print
        print 'Longitude: %f, Latitude: %f, Heading: %f' % (float(meas["Lon"]), float(meas["Lat"]), float(meas["oX"]))
        
        #--- kalman / particle filter
        
        
        #--- steer command to arduino
        #ser.write('5')
        
        
    except KeyboardInterrupt:
        break
