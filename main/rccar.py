#--- self-driving radio controlled car
#--- make sure to run it as root (sudo)

import sys, socket, serial, time

#--- initializations and stuff
#--- car's state
x = 0
y = 0
theta = 0
#--- arduino commands
forward = '\x04'
reverse = '\x08'
right = '\x02'
left = '\x01'
blank = '\x00'

#--- open serial port to arduino
try:
    ser = serial.Serial('/dev/tty.usbmodem411', 9600)
except serial.SerialException:
    print 'arduino not yet connected'
    sys.exit()

#--- open udp socket to get measurement data
port = 12346
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind(("", port))
except socket.error, e:
    print 'failed opening socket: %s' % e
    
#--- open file for measurements
t = time.localtime(time.time())
filename = 'meas_' + str(t.tm_year) + '_' + str(t.tm_mon) + '_' + str(t.tm_mday) + '_' + str(t.tm_hour) + '_' + str(t.tm_min) + '_' + str(t.tm_sec)
f = open(filename, 'w+')

while True:
    try:
        data, addr = s.recvfrom(1024)
        meas = eval(data)
        
        #--- for now, just print
        print 'Longitude: %f, Latitude: %f, Heading: %f' % (float(meas["Lon"]), float(meas["Lat"]), float(meas["oX"]))
        #print '\a'
        
        #--- store to file for measurements
        f.write(str(time.time()) + ',' + meas["Lon"] + ',' + meas["Lat"] + ',' + meas["oX"] + '\n')
        
        #--- kalman / particle filter
        
        
        #--- steer command to arduino
        ser.write(forward)
        
    except socket.error, e:
        print 'socket failure: %s' % e
        break
        
    except serial.SerialException:
        print 'serial port failure'
        break
            
    except KeyboardInterrupt:
        ser.write(reverse)
        ser.close()
        f.close()
        print 'bye!'
        break
