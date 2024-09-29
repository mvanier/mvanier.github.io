from tkinter import *

root = Tk()
root.geometry('800x600')
c = Canvas(root, width=800, height=600)
c.pack()
a = c.create_rectangle(0, 0, 50, 50, 
      fill='red', outline='red')
input('Press <return> to quit.')

