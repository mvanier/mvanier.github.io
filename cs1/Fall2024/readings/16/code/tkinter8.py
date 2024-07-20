from tkinter import *

root = Tk()
root.geometry('800x800')
c = Canvas(root, width=800, height=800)
c.pack()
ovals = []
for i in range(50, 350, 50):
    x1 = 100 + i
    y1 = x1
    x2 = 700 - i
    y2 = x2
    oval = c.create_oval(x1, y1, x2, y2, outline='blue', width=4)
input('Press <return> to quit.')

