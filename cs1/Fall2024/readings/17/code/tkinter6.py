from tkinter import *

root = Tk()
root.geometry('800x600')
c = Canvas(root, width=800, height=600)
c.pack()
oval = c.create_oval(100, 100, 700, 500, outline='blue', fill='yellow', width=4)
input('Press <return> to quit.')

