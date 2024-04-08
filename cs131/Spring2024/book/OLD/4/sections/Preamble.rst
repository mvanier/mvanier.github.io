Preamble
========

Due date
--------

This assignment is due on |dueday|, |lab4due| at |duetime|.


Coverage
--------

This assignment covers the material up to lecture 14.


Code base
---------

We are supplying you with a zip file called ``lab4.zip``.
It contains two subdirectories: ``sexpr`` and ``typed_imp``.
The ``sexpr`` subdirectory contains the code
to parse S-expressions from strings and files.
The ``typed_imp`` subdirectory contains the base implementation
of the |typedimp| language,
as well as test scripts in the ``tests`` subdirectory.
You should unzip the file in a directory and do the following:

* ``cd`` into the ``sexpr`` subdirectory and run ``make``.
* Then ``cd`` into the ``typed_imp`` subdirectory and run ``make``.

If all goes well, this should create the ``typed_imp`` executable, which
you can run as follows:

.. code-block:: ocaml

   $ ./typed_imp

.. note::

   This version of ``typed_imp`` (compiled from the code we supply
   you with) is unfinished, but it will still run. However, it will not
   pass all the tests. The following sections describe what you have to do
   to complete it.


What to hand in
---------------

For this assignment, you will need to modify the following files from
the supplied code:

* ``ast.ml``
* ``ast.mli``
* ``error.ml``
* ``error.mli``
* ``value.ml``
* ``value.mli``
* ``typecheck.ml``
* ``eval.ml``
* ``tests/lab4.timp``

.. note::

   Please do not modify or submit any other files!

Submit these files as the CodePost assignment named ``Assignment 4``.


