Part B: Implementing the type checker
=====================================

The code you will write for this section is in the ``Typecheck`` module,
which consists of the files ``typecheck.ml`` and ``typecheck.mli``. You
will only have to modify the ``typecheck.ml`` file. In that file you are
provided with a partial implementation of the type checker. You have to
complete the definition of the following functions:

* ``typecheck_expr``

* ``appears_unprotected_in``

* ``typecheck_valrec``

We describe these three functions below.

.. note::

   Makes sure that you read through the code that is provided for you!
   There are lots of useful helper functions defined that will make your
   code much more concise and understandable if you use them. In
   particular, the ``assert_types_equal`` function is extremely useful.


1. ``typecheck_expr``
---------------------

This is the main type checking function. It takes in three arguments:

#. a kind environment mapping type names to their kinds (also known as
   |Delta|)

#. a type environment mapping variable names to their types (also known
   as |Gamma|)

#. an expression to be type checked

and returns the type of the expression. If there is a type error, a
suitable exception is raised.

Most of the cases are just translations into code of the operational
semantics of type checking described in lecture 17. There are a few
cases that are a bit more subtle, so we discuss them here.


``Lambda``
~~~~~~~~~~

Remember that all the formal argument types are required to have the
kind ``*``.


``TypeApply``
~~~~~~~~~~~~~

This case can (and should) be written in a single line by taking
advantage of the ``Subst`` module.


``TypeLambda``
~~~~~~~~~~~~~~

This is the only slightly tricky case. The operational semantics for
``TypeLambda`` given in lecture 17 talks about picking new type
variables if any of the type variables exist in the type environment
|Gamma|. That makes sense in an operational semantics, because we are
describing how to derive a proof of the type-correctness of a
``type-lambda`` expression given the correctness of its parts. Here we
are working in the opposite direction; we are given a particular
``type-lambda`` and want to know what its type is. Instead of trying to
rename it to be compatible with the type environment, we choose a
simpler solution and simply don’t allow ``type-lambda`` expressions
whose type variables are present in |Gamma|. [1]_ If any of the
``type-lambda`` type variables are present in |Gamma|, simply raise a
type error exception with the error message
``"type-lambda type variables are free in type environment"``. Then the
programmer can manually rename the type variables as necessary so that
everything type checks.

You’ll find that the ``free_tyvars_gamma`` function from the ``Subst``
module is useful here.

The other subtle issue here is that when checking the body of a
``type-lambda`` expression, the kind environment |Delta| must be
extended with the type variable names, each of which must have the kind
``*``. (This kind is represented in the code as ``Type``.) The body is
then typechecked in this new kind environment, and then the type of the
entire ``type-lambda`` expression is returned as a ``ForAll`` type.


2. ``appears_unprotected_in``
-----------------------------

We know that the ``val-rec`` definition form is almost always used to
define a recursive function, where the function being defined is
referenced inside the body of the definition. However, nothing actually
requires us to do that. We could use ``val-rec`` to define simple
variables, for instance:

.. code-block:: ocaml

   (val-rec int x 10)

and there’s nothing wrong with that. However, we should disallow uses of
the name being defined (here, ``x``) if they are used outside of a
``lambda`` expression, since then you have a self-referential definition
which can never terminate when evaluated. That would be something like
this:

.. code-block:: ocaml

   (val-rec int x (+ x 1))

We refer to errors of this sort as the defined name (here, ``x``)
appearing "unprotected" in the body of the definition (here,
``(+ x 1)``). The "unprotected" means "not occurring inside a ``lambda``
expression" since a ``lambda`` expression delays evaluation of the
name. [2]_

We define the function ``appears_unprotected_in`` which takes two
arguments: a variable name and a (value) expression. It returns ``true``
if the name appears unprotected in the expression, and otherwise returns
``false``. The basic rules for this are:

#. any name inside a ``lambda`` expression is protected

#. any literal has no unprotected names inside it

#. a name is unprotected in a variable if the variable name is the same
   as the name

#. a name is unprotected in any other expression with subexpressions if
   it’s unprotected in *any* of the subexpressions

This code is a straightforward case analysis on the structure of value
expressions. There is an extensive test suite to make sure that you
don’t miss any cases. This function is (obviously) going to be used in
the ``val-rec`` type checking code, which we now turn to.


3. ``typecheck_valrec``
-----------------------

This function type checks ``val-rec`` expressions. It’s a simple code
translation of the operational semantics of ``val-rec`` given in lecture
17. A partial implementation is provided.

[End of assignment]

----

.. rubric:: Footnotes

.. [1] You might wonder how "real" languages handle this problem.
   You should keep on wondering, since almost no "real" language
   I know of actually has a ``type-lambda`` expression
   (except Scala, and Scala is pretty complicated even without type lambdas).
   Instead, real languages use *type inference*
   to get rid of the need for ``type-lambda`` expressions.

.. [2] Note that this doesn’t rule out definitions of functions
   which never terminate,
   but it does rule out some obviously incorrect cases.
