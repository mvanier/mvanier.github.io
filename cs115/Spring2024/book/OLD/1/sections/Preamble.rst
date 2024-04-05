Preamble
========

Due date
--------

This assignment is due on |dueday|, |lab1due| at |duetime|.


Prerequisites
-------------

We will be assuming henceforth that you have taken and passed CS 1 or CS 1x (or
placed out of one of them). We also hope that you have taken and passed CS 4,
though this is not a hard prerequisite (knowing the material in CS 4 will make
CS 115 much easier). We will also assume that you have a rudimentary knowledge
of Linux/Unix terminal commands (at roughly the level required for CS 1). If
not, please read a Linux tutorial, because we will be using the terminal a lot
in this course.

.. note::

   You are **not** required to use Linux in this course. The terminal in
   MacOS works pretty much the same way (both Linux and MacOS are Unix
   systems), so the commands should work fine assuming the software is
   installed correctly. Windows terminal commands are completely
   different. If you have Windows, you should install
   `Windows Subsystem for Linux`_ (WSL),
   which gives you a full version of Linux running inside of Windows. 
   (Use that instead of trying to run Haskell natively on Windows.)
   Make sure you are installing WSL version 2 or later,
   since version 1 is known not to work properly with the GHC compiler
   used in this course.


Syllabus
--------

Please make sure you have read the
:doc:`Syllabus </content/admin/Syllabus>` page and the
:doc:`Collaboration policies </content/admin/Collaboration_policies>`
page before submitting any assignments,
so you know how the course is organized,
what the grading policies are,
what is and is not acceptable collaboration |etc|.


Software installation
---------------------

See the :doc:`Software </content/Software>` page for instructions on how
to install the course software.


Starting code base
------------------

All assignments will have a code base, which includes test code
and sometimes template code for modules.
These are posted on the course Canvas site in the Modules section.


External websites
-----------------

In addition to this book, we will be using the following websites:

* The course Canvas page, which is where lectures will be posted,
  as well as source code for assignments, papers of interest,
  and teaching assistant contact information.

* The course Piazza page, which is where most course-related announcements
  will be posted, as well as serving as a question-and-answer forum.

* The course CodePost_ page, which is where assignments will be submitted
  and graded.

Students will be enrolled in Piazza and CodePost by the course instructor.
If you added the course late, you may need to remind the instructor to add you.

In addition, some TAs and the instructor may choose to host office hours online
on Zoom.


Background reading
------------------

We recommend that you read the first four chapters of the `Gentle
Introduction to Haskell <http://www.haskell.org/tutorial/index.html>`__.
*Warning*: It isn’t that gentle! [1]_


Writing and testing your code
-----------------------------

Here are some general guidelines for writing your code.
These apply to all assignments, not just to this one.
We won't repeat these in future assignments,
but you can reread this later if necessary.

Compiler warnings
~~~~~~~~~~~~~~~~~

**All of your Haskell functions, even internal ones, should have explicit type
signatures.**  This is not a Haskell requirement, but we consider it to be good
style as well as good documentation, and it is likely to save you from some
problems that might occur if you let ``ghc`` infer the types.  Also, error
messages will be easier to understand.

All of your code should be tested using ``ghci`` with the ``-W``
(warnings enabled) command-line option.
Any warnings will be considered errors,
so make sure your code does not give any warnings.
(*NOTE*: On rare cases, we may make exceptions to this for specific problems,
but if so we will tell you.)

You can invoke ``ghci`` explicitly with the ``-W`` option as follows:

.. code-block:: text

   $ ghci -W

However, since you always want the ``-W`` option to be enabled,
a better solution is to define this once and for all in a ``.ghci`` file.
The ``.ghci`` file is just a regular file
that contains stuff to be automatically loaded into ``ghci``
when it's launched. All you need to put in this file is this line:

.. code-block:: text

   :set -W

and you are all set.  We recommend that this file be placed in your home
directory.  To test whether it worked, run ``ghci`` and type the following line:

.. code-block:: text

   let f (x:xs) = x

If this line gives you a warning about non-exhaustive pattern matches, it's
working.

.. warning::

   If your code generates any warnings
   (and we haven't explicitly said that it's OK for that problem),
   you will lose half marks for each problem that generates a warning.
   (If the code for multiple problems generates warnings,
   you will lose half marks for each of them.)
   Your TA is also allowed to not grade your assignment
   until you fix the warnings,
   but they must inform you of this so you know what you need to do.

If you are getting a warning but don't understand it
or don't know how to fix it, post a question to Piazza
or come to office hours.


Essay/evaluation questions
~~~~~~~~~~~~~~~~~~~~~~~~~~

Evaluations and answers to questions should be written as Haskell
comments. We recommend that you use the multi-line comment syntax
``{- ... -}``, for example:

.. code-block:: haskell

   {-

   The answer to this question is...

   -}


Test script
~~~~~~~~~~~

We are supplying you with a short test script called ``Tests_Lab1.hs``
which you should download into the directory where your ``Lab1.hs`` file is.
Before running the test script, make sure that the ``HUnit`` and
``HUnit-approx`` libraries are installed.
In a terminal, type:

.. code-block:: text

   $ cabal install --lib HUnit
   $ cabal install --lib HUnit-approx

To run the tests, just load the test script into ``ghci`` and type
``main`` like this:

.. code-block:: text

   ghci> :set -package base
   ghci> :l Tests_Lab1.hs
   ghci> main

.. note::

   The line ``:set -package base`` is needed to import the base libraries.
   It's quite annoying to have to do this every time you start ``ghci``,
   so we recommend that you add this line to your ``.ghci`` file.

We are also supplying you with a ``Makefile``
so you can run the test script without using ``ghci``.
To do this, make sure that the ``Makefile`` is in the same directory
as the files ``Lab1.hs`` and ``Tests_Lab1.hs``,
``cd`` into that directory, and type

.. code-block:: text

   $ make

to compile and run the test script, and 

.. code-block:: text

   $ make clean

to remove all files generated during compilation.


What to hand in
---------------

Write all of your code in a single text file called ``Lab1.hs``. This
file should start with this line:

.. code-block:: haskell

   module Lab1 where

followed by the rest of the code on subsequent lines. Submit your
assignment as the CodePost assignment called ``Assignment 1``.

----

.. rubric:: Footnotes

.. [1]
  This document is very old, but it describes the most basic aspects of
  Haskell that haven’t changed over the years.
