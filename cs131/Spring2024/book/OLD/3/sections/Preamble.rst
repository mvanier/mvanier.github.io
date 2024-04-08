Preamble
========

Due date
--------

This assignment is due on |dueday|, |lab3due| at |duetime|.


Coverage
--------

This assignment covers the material up to lecture 11.


Code base
---------

We are supplying you with a zip file called ``lab3_code.zip``.
It contains two subdirectories: ``sexpr`` and ``uscheme_plus``.
The ``sexpr`` subdirectory contains the code
to parse S-expressions from strings and files.
The ``uscheme_plus`` subdirectory contains the base implementation
of the |uschemeplus| language,
which (like |uscheme|) is spread over a number of files.
You should unzip the file in a directory and do the following:

* ``cd`` into the ``sexpr`` subdirectory and run ``make``.
* Then ``cd`` into the ``uscheme_plus`` subdirectory and run ``make``.

If all goes well, this should create the ``uscheme_plus`` executable,
which you can run as follows:

.. code-block:: text

   $ ./uscheme_plus

.. note::

   This version of ``uscheme_plus`` (compiled from the code we supply
   you with) is unfinished, but it will still run. However, it will not
   pass all the tests. The following sections describe what you have to do
   to complete it.


What to hand in
---------------

For this assignment, you will only need to modify the following files
from the supplied code:

* ``ast.ml``
* ``ast.mli``
* ``error.ml``
* ``error.mli``
* ``eval.ml``

Submit these files as the CodePost assignment named ``Assignment 3``.


