Preamble
========

Due date
--------

This assignment is due on |dueday|, |lab2due| at |duetime|.


Coverage
--------

This assignment covers the material up to lecture 7.


Code base
---------

We are supplying you with a zip file called ``uscheme.zip``. It contains two
subdirectories: ``sexpr`` and ``uscheme``. The ``sexpr`` subdirectory contains
the code to parse S-expressions from strings and files. The ``uscheme``
subdirectory contains the base implementation of the ``uscheme`` language,
which (unlike *Imp*) is spread over a number of files. You should unzip the
file in a directory and do the following:

* ``cd`` into the ``sexpr`` subdirectory and run ``make``.
* Then ``cd`` into the ``uscheme`` subdirectory and run ``make``.

If all goes well, this should create the ``uscheme`` executable, which
you can run as follows:

.. code-block:: ocaml

   $ ./uscheme

If you've installed ``rlwrap`` (see the last assignment), you should run it
like this:

.. code-block:: ocaml

   $ rlwrap ./uscheme

That way you will get nice line-editing features.

.. warning::

   The version of ``uscheme`` we supply you with is unfinished.
   If you run it, it will start up but you won't be able to do much
   until you add a few missing features.
   The sections below describe what you have to do.


What to hand in
---------------

You will hand in these files:

* ``ast.mli``
* ``ast.ml``
* ``ir.mli``
* ``ir.ml``
* ``env.mli``
* ``env.ml``
* ``error.mli``
* ``error.ml``
* ``quote.ml``
* ``eval.ml``
* ``basis.ml``
* ``uscheme.ml``
* ``booleans.scm``  (from the ``tests`` subdirectory)
* ``lab2c.scm``  (from the ``tests`` subdirectory)


