Part A: Implementing capture-avoiding substitution
==================================================

The code you will write for this section is in the ``Subst`` module,
which consists of the files ``subst.ml`` and ``subst.mli``.
You will only have to modify the ``subst.ml`` file.


The ``subst.mli`` file
----------------------

The exported functions are given in the ``subst.mli`` file; they are:

.. code-block:: ocaml

   (** Return a set of all the free type variables in a type environment. *)
   val free_tyvars_gamma : Type.type_env -> IdSet.t

   (** Instantiate a quantified type by substituting actual types for
       type variables.  All substituted types must be of kind "*".
       The `loc` argument is for error cases. *)
   val ty_instantiate :
     Loc.loc -> ty_expr -> ty_expr list -> Type.kind_env -> ty_expr

   (** Check if two types are equivalent. *)
   val equiv_type : ty_expr -> ty_expr -> bool

Even though you won’t be directly implementing any of these, it’s worth
spending a little time describing what they do.

* ``free_tyvars_gamma``

   We have been using the Greek letter "gamma" (|ie| |Gamma|)
   in our type checking equations to refer to type environments.
   A type environment maps variable names to their types,
   and at certain points in type checking,
   these types can contain type variables.
   The function ``free_tyvars_gamma`` will return a set
   of all the free type variables in all the types in a type environment.
   It uses an internal function called ``free_tyvars``
   to compute the free type variables of each type.

   This function is implemented for you
   and is used in a couple of places in the type checker.

* ``ty_instantiate``

   This function takes a type expression (``ty_expr``), which should be
   a ``ForAll`` type (or else it’s an error). It also takes a list of
   types to substitute for the type variables of the ``ForAll`` type, as
   well as a kind environment. The function checks that the number of
   type variables in the ``ForAll`` type is the same as the number of
   supplied types, and that all the supplied types have kind ``*``
   (which is a restriction of this language). [1]_ If these checks pass,
   the function substitutes the given types for the type variables. It
   does this by creating a new type environment and calling the
   ``subst`` function.

   This function is implemented for you and is used in the type checker.

* ``equiv_type``

   The ``equiv_type`` function compares two type expressions and returns
   ``true`` if they are equivalent. As discussed in lecture 18, two type
   expressions don’t have to be identical to be equivalent. For
   instance, ``(forall ['a] ('a -> 'a))`` and
   ``(forall ['b] ('b -> 'b))`` are not identical but they are
   equivalent.

   This function is implemented for you, based on the specification
   given in lecture 18. The only complicated case is the ``ForAll``
   case, which uses the ``rename`` function, which itself is defined in
   terms of the ``subst`` function.

   This function is used in the type checker.


The ``subst.ml`` file
---------------------

Even though all of the exported functions in the ``Subst`` module (those
described above) have been written for you, the last two
(``ty_instantiate`` and ``equiv_type``) depend on a function called
``subst``. ``subst``, in turn, depends on another function called
``rename_for_all_avoiding``, which depends on a function called
``rename``, which depends on ``subst`` (so the three functions are
mutually recursive). You will only need to complete the implementations
of ``subst`` and ``rename_for_all_avoiding``.

The type signatures of these functions are as follows, with some
explanatory comments:

.. code-block:: ocaml

   (* Substitute type variables in a type expression with the bindings of those
      variables, yielding a new type. *)
   val subst : ty_expr -> Type.type_env -> ty_expr

   (* Rename a list of type variable names to a new list of type variable names
      in a type, yielding a new type. *)
   val rename : id list -> id list -> ty_expr -> ty_expr

   (* Rename all type variables in a `ForAll` type so as to avoid a particular
      set of possibly-captured variable names, yielding a new type. *)
   val rename_for_all_avoiding : id list -> ty_expr -> IdSet.t -> ty_expr

We need to say a few more things about these functions.

* ``subst``

   In this function, the first argument can be any type expression. The
   second is a "type environment", which in this program just means a
   binding between identifiers and types. However, this is *not* the
   usual type environment |Gamma| that is in all the type checking
   equations. It’s not really an "environment" at all; it’s just a
   mapping between identifiers (in this case, type variables) and the
   types that should be substituted for those type variables.
   All this means is that you are substituting multiple types
   for multiple type variables simultaneously in a particular type,
   yielding a new type.

   In lecture 18, we listed the specification for substituting a
   *single* type for a *single* type variable while avoiding variable
   capture; in your code you will need to generalize this to handle
   multiple substitutions. (Don’t worry; you don’t have to write out new
   equations!)

   This function has five different cases (because types have five
   different constructors); four of these are trivial and are provided
   for you. The one non-trivial case is for ``ForAll`` types, which you
   have to write yourself. The reason this is non-trivial is that this
   is the only case where variable capture can occur. We provide a
   walkthrough of the algorithm below.

* ``rename``

   This function takes two equal-length lists of identifiers
   representing type variables, and renames all of the identifiers in
   the first list to those of the second list in a type expression. It
   does this by creating a mapping between the original and the new
   names and calling the ``subst`` function.

* ``rename_for_all_avoiding``

   This function renames the type variables of a ``ForAll`` type to
   avoid variable captures. The first two arguments are the components
   of the ``Forall`` type (the list of type variables and the rest of
   the expression). The ``IdSet.t`` argument is a set of type variables
   which may be captured; this is determined in the ``subst`` function.
   The return value is the new ``ForAll`` type with all the
   substitutions made. The new type must be equivalent to the original
   type.

Now we’ll go into more detail on how to implement ``subst`` and
``rename_for_all_avoiding``.


1. Implementing ``subst``
-------------------------

``subst`` takes two arguments: a type (represented as a ``ty_expr``) and
a mapping between type variable names and types (represented as a
``type_env``). The mapping represents the type variable substitutions
that are to be made in the type. We’ll refer to the first argument as
the "source type" and the mapping as the "type map". We’ll refer to the
output type as the "result type".


Variable capture
~~~~~~~~~~~~~~~~

Naively, you might think that all you have to do is to find the
locations of the type variables from the type map in the source type,
and then replace them with the types that the type variables map to.
However, this can easily lead to variable capture. For instance, naively
substituting the type variable ``'a`` for the type ``('b -> 'b)`` in
the source type ``(forall ['b] ('a -> 'b))`` would yield the
result type ``(forall ['b] (('b -> 'b) -> 'b))``, which is
clearly wrong. In this case we say that the ``(forall ['b] ...)`` has
*captured* the type variable ``'b``, which was originally a *free*
variable in ``('b -> 'b)``. This changes the meaning of the source
type, so that the result type is no longer equivalent to the source
type, and substitution is broken.

Instead, this substitution should yield a result type which is like
``(forall ['c] (('b -> 'b) -> 'c))``. We can get this if we
rename the source type from ``(forall ['b] ('a -> 'b))`` to the
equivalent type ``(forall ['c] ('a -> 'c))`` before doing the
substitution. But to do this correctly we must make sure that we know
how to do the renaming.


Substitution algorithm
~~~~~~~~~~~~~~~~~~~~~~

The substitution algorithm is as follows. We will use the term "alphas
list" to refer to the list of quantified variables in the ``ForAll``
type.

#. Compute all the free type variables in the ``ForAll`` type. (By
   definition, these do *not* include any of the quantified variables.)

#. Collect all the types in the type map that the free type variables map to.
   These types can potentially cause variable captures.
   If a free type variable isn’t mapped to a type in the type map,
   include it too.
   (Wrap it in a ``TyVar`` constructor,
   since a type variable name by itself isn't a type).

#. Collect all the free type variables in all of these types (including
   the unmapped free type variables). Call these "potential captures".

#. Check to see if any of the free type variables are also in the alphas
   list.

   a. If not, then we are in luck! No variable capture can occur.
      Remove all the type variables in the alphas list from the type map
      (because the alphas list prevents them from being substituted)
      and substitute the remaining type map
      into the body of the ``ForAll`` type.
      (This will involve a recursive call to ``subst``.)

   b. Otherwise, we have a variable capture situation. Rename all the
      quantified values (the alphas list) in the ``ForAll`` type so that
      they don’t conflict with any of the potential captures. You’ll do
      this by calling the ``rename_for_all_avoiding`` function described
      in the next section.  Once this is done, then do the substitution.

This is not particularly hard code to write, but small mistakes can lead
to extremely subtle bugs that are hard to track down. The algorithm we
give is purposely very conservative so as to avoid problems.

Our solution required us to write less than 25 extra lines of code (not
including comments).


2. Implementing ``rename_for_all_avoiding``
-------------------------------------------

The function ``rename_for_all_avoiding`` takes three arguments:

#. the alphas list from a ``ForAll`` type (|ie| the list of quantified
   variable names)

#. the body of a ``ForAll`` type (|ie| everything but the list of
   quantified variable names)

#. a list of potentially captured type variables

Note that the first two arguments are obtained
by taking the parts of a ``ForAll`` type.

The function returns a new ``ForAll`` type
formed by renaming any type variables
that are in the list of potential captures.
A partial implementation is provided for you.

Here is the renaming algorithm:

#. If a type variable in the alphas list is not in the potential
   captures list, it can remain as it is.

#. Otherwise, pick a new type variable:

   *  which is different from all other type variables in the alphas
      list,

   *  which is different from any new type variables that may have been
      added previously in this algorithm,

   *  which is not in the set of free variables of the body of the
      ``ForAll`` type,

   *  and which is not in the list of potential captures.

#. Repeat the previous two steps for all the type variables in the
   alphas list. Then use the list of new type variables to rename the
   entire type expression (by calling the ``rename`` function, provided
   for you) and construct a ``ForAll`` type out of the result.

Our solution required us to write less than 10 extra lines of code (not
including comments).

----

.. rubric:: Footnotes

.. [1] Some languages, notably Haskell, don’t have this restriction,
   so we say that they support "higher-kinded types".

