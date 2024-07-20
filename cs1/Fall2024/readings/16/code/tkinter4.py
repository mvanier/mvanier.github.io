from tkinter import *

root = Tk()
root.geometry('800x600')
c = Canvas(root, width=800, height=600)
c.pack()
line1 = c.create_line(0, 0, 800, 600, fill='blue', width=3)
line2 = c.create_line(800, 0, 0, 600, fill='red', width=6)
input('Press <return> to quit.')

