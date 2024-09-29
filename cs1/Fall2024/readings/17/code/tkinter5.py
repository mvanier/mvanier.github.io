from tkinter import *

root = Tk()
root.geometry('800x600')
c = Canvas(root, width=800, height=600)
c.pack()
line1 = c.create_line(100, 100, 400, 100, 100, 400, 400, 400, 
          fill='blue', width=3)
input('Press <return> to quit.')

