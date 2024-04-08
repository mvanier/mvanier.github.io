Preamble
========

Due date
--------

This assignment is due on |dueday2|, |lab5due| at |duetime|.


Coverage
--------

This assignment covers the material up to lecture 18.


Code base
---------

We are supplying you with a zip file called ``lab5.zip``.
It contains two subdirectories: ``sexpr`` and ``typed_uscheme``.
The ``sexpr`` subdirectory contains the code
to parse S-expressions from strings and files.
The ``typed_uscheme`` subdirectory contains the base implementation
of the |typeduscheme| language (which is spread over a number of files)
as well as test scripts in the ``tests`` subdirectory.
You should unzip the file in a directory and do the following:

* ``cd`` into the ``sexpr`` subdirectory and run ``make``.
* Then ``cd`` into the ``typed_uscheme`` subdirectory and run ``make``.

If all goes well, this should create the ``typed_uscheme`` executable,
which you can run as follows:

.. code-block:: text

   $ ./typed_uscheme

.. note::

   This program won’t run as-is; you will just see a ``Failure "TODO"``
   exception. You will need to complete the implementation before it
   will run correctly.

Once you have completed your implementation, type

.. code-block:: text

   $ make test

to run the test scripts.


What to hand in
---------------

For this assignment,
you will need to modify two files from the supplied code:

* ``subst.ml``
* ``typecheck.ml``

.. note::

   Please do not modify or submit any other files!

The places in these files where you have to add code are marked with

.. code-block:: ocaml

   failwith "TODO"

You should remove the ``failwith "TODO"``\s
and replace them with your code.

Please submit these two files to CodePost as ``Assignment 5``.

