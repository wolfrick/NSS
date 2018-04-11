import sys
import datetime


#Reading the time file to get the time returned by GMT Server
with open("time.txt","r") as f:
	time = f.read();

time = [int(x) for x in time.split("/")]
time_tuple = ( time[0],time[1],time[2],time[3],time[4],time[5],0,)


#Function to set system time on windows platform
def _win_set_time(time_tuple):
    import pywin32
    dayOfWeek = datetime.datetime(time_tuple).isocalendar()[2]
    pywin32.SetSystemTime( time_tuple[:2] + (dayOfWeek,) + time_tuple[2:])


#Function to set system time on linux platform
def _linux_set_time(time_tuple):
    import ctypes
    import ctypes.util
    import time

    CLOCK_REALTIME = 0
    class timespec(ctypes.Structure):
        _fields_ = [("tv_sec", ctypes.c_long),
                    ("tv_nsec", ctypes.c_long)]
    librt = ctypes.CDLL(ctypes.util.find_library("rt"))
    ts = timespec()
    ts.tv_sec = int( time.mktime( datetime.datetime( *time_tuple[:6]).timetuple() ) )
    ts.tv_nsec = time_tuple[6] * 1000000 # Millisecond to nanosecond
    librt.clock_settime(CLOCK_REALTIME, ctypes.byref(ts))

#Driver code
if sys.platform=='linux2':
    _linux_set_time(time_tuple)

elif  sys.platform=='win32':
    _win_set_time(time_tuple)
