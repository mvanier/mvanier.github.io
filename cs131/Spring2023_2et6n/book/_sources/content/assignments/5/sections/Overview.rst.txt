Overview
========

This assignment continues the theme of "type checking"
begun in assignment 4,
but this time we are type checking a much more complex language.
The main difference is that this language contains *polymorphic types*,
which is manifested by the presence of a number of features:

* type variables (like ``'a``, ``'b`` |etc|)

* universally-quantified (``forall``) types (|eg|
  ``(forall ['a] ('a -> 'a))``)

* ``type-lambda`` expressions

* type application (the ``@`` operator)

In addition, this language, like |uscheme|, is a functional language,
so it features first-class functions (``lambda`` expressions).

Even though there is not a large amount of code to write,
we expect that this assignment will be challenging.
Don’t be shy about asking for help if you get stuck!
Fortunately, we are supplying you with an extremely thorough test suite,
so if your code passes all the tests, it probably has no major bugs. [1]_


Code organization
-----------------

Most of the source code files will be familiar to you
after working with |uscheme|.
There are a few new files, which we describe here.

* ``util.ml``, ``util.mli``

  The ``Util`` module contains definition of the ``id`` type
  (an alias for ``string`` which is used for identifiers),
  the ``IdSet`` and ``IdMap`` types
  (sets of identifiers and maps from identifiers to another type),
  and the ``eq_list`` function
  (which tests that two lists are equal
  according to some equality criterion for the elements).

* ``type.ml``, ``type.mli``

  The ``Type`` module contains the type definitions for both types and kinds,
  as well as the (trivial) definitions of type and kind environments.
  It contains simple utility functions for creating type/kind environments
  (``make_type_env`` and ``make_kind_env``),
  for looking up types and kinds in their environments
  (``find_type`` and ``find_kind``),
  and for binding types and kinds in their respective environments
  (``bind_type``, ``bind_types``, and ``bind_kind``).
  For convenience, it also exports the types of literals
  (unit, boolean, integer, and ``nil``)
  as ``unit_ty``, ``bool_ty``, ``int_ty``, and ``nil_ty``.

  Most importantly, it defines the ``kind_of_type`` function,
  which determines the kind of a type given a kind environment.
  It also does kind checking. This includes:

  *  checking that a type variable is found in the kind environment,
     and that it has kind ``*``,

  *  checking that a type constructor is found in the type environment,

  *  checking that function types are composed of arguments
     and return types that only have kind ``*``,

  *  checking that a type constructor applied to type arguments
     is applied to the right number of type arguments,
     and that each type argument has the correct kind,

  *  checking that ``ForAll`` types have kind ``*``, assuming that
     their quantified type variables also have kind ``*``.

  Note that a kind environment maps types to kinds,
  but not all types are represented directly in the kind environment. [2]_
  In fact, the only types that are present in our kind environments
  are built-in type constructors like ``int``, ``bool``, ``unit``
  ("nullary" type constructors), ``list``, ``pair``
  (type constructors which take type arguments) and type variables.
  Other types (|eg| ``(int -> int)``) can be checked in a kind environment
  by decomposing the type into its parts, checking their kinds,
  and checking the kind of the type given the kinds of the parts.

  .. note::

     See lecture 16 for the detailed type and kind formation rules.

* ``subst.ml``, ``subst.mli``

   The ``Subst`` module contains functions for substituting types
   for type variables and renaming type variables.
   It implements the capture-avoiding substitution specification
   described in lecture 18.

* ``typecheck.ml``, ``typecheck.mli``

   The ``Typecheck`` module contains
   the actual type checker for |typeduscheme|.

The code in the ``Utils`` and ``Type`` modules is supplied for you.
You will need to complete the implementation of the ``Subst``
and ``Typecheck`` modules.


Error handling
--------------

In both of the files you will be editing,
various error scenarios can come up.
There are only two functions you need to use to raise type error exceptions:

#. If you are type checking (or kind checking) an expression or a definition,
   and you expect one type but get another,
   use the ``Error.type_err`` function.
   Identify what you expected to find and what you did find.

#. For any other kind of type error
   (for instance, the wrong number of arguments to a function),
   us the ``Error.type_other_err`` function
   and provide a meaningful error message.

Once you’ve finished the assignment and got it to pass the tests,
congratulate yourself!
You’ve written a type-checker
for a non-trivial functional programming language with polymorphic types,
something that very few programmers
(even ones who write compilers for a living) can claim.

----

.. rubric:: Footnotes

.. [1] Or at least, no major bugs not shared with the instructors'
   implementation.

.. [2] This is analogous to type environments,
   which map all variables to types
   but don't include types for all expressions formed from variables.
