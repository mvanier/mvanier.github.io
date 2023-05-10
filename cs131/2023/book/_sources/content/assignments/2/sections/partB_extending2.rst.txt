Part B: Finishing and extending |uscheme| : evaluation
======================================================

Now that we've dealt with the parsing issues,
we can concentrate on making the entire extended version of |uscheme| work.


1. Literals
-----------

Edit the literal-handling code in ``eval.ml`` to work with the new literal
type.


2. The ``=`` primitive function
-------------------------------

Make the ``=`` primitive function (defined in ``basis.ml``)
work with unit values and symbols as well as all other atomic values.
(It doesn’t work for compound data types like pairs;
the ``equal`` basis function takes care of that.)
Note that the function ``prim_eq`` is the one that defines the ``=`` primitive.

The only file you need to change for this problem is ``basis.ml``.

.. admonition:: Examples
   :class: example

   .. code-block:: text

      >>> (= 0 0)
      #t
      >>> (= 0 1)
      #f
      >>> (= #u #u)
      #t
      >>> (= #f #f)
      #t
      >>> (= #f #t)
      #f
      >>> (= 'foo 'foo)
      #t
      >>> (= 'foo 'bar)
      #f


3. Extending pairs
------------------

Change the representation of pairs in ``env.ml``'s ``value`` type
to be pairs of ``value ref``\s instead of pairs of ``value``\s.
In other words, make pairs mutable.
Once you have done this,
change the definition of the pair primitives ``cons``, ``car``, and ``cdr``
(in ``basis.ml``) to use the new representation.
Add two new primitives to ``basis.ml``: ``set-car`` and ``set-cdr``.
They both take two arguments: a ``cons`` pair and a value.
``set-car`` changes the ``car`` of the pair to the value,
while ``set-cdr`` changes the ``cdr``.
Both primitives return unit (``#u``) values.

Make whatever other changes may be necessary after making this change.
The compiler will helpfully point out all places where the old pair
definition is no longer valid. (Strongly statically-typed languages like
OCaml are really good for refactorings of this sort.)

The files you need to change for this problem are ``env.ml``,
``env.mli``, ``quote.ml``, and ``basis.ml``. Look at the way primitive
functions are defined in ``basis.ml`` to understand how to define a new
one.

.. admonition:: Examples
   :class: example

   .. code-block:: text

     >>> (val p (cons 1 2))
     val p = '(1 . 2)
     >>> (car p)
     1
     >>> (cdr p)
     2
     >>> (set-car p 42)
     >>> (car p)
     42
     >>> p
     '(42 . 2)
     >>> (set-cdr p 101)
     >>> (cdr p)
     101
     >>> p
     '(42 . 101)


4. The ``equal?`` basis function
--------------------------------

There is a function called ``equal?`` in ``basis.ml``
which is commented out (indicated by a ``TODO`` comment).
Remove the ``TODO`` comment and uncomment the function.


5. The ``valrec`` definition form
---------------------------------

In part A you implemented the syntax for the ``valrec`` definition form.
This form is used to define mutually-recursive functions at the top level.
It looks like this:

.. code-block:: text

   (valrec
     [f ...]
     [g ...])

The ``...`` parts are normally ``lambda`` expressions. The semantics are
similar to ``letrec``:

* Extend the environment with new bindings of all the names to be
  defined in the ``valrec`` (here, just ``f`` and ``g``) to unspecified
  values.

* Evaluate the expressions (the ``...`` parts) in the context of the
  extended environment.

* Rebind the defined names to the values of the corresponding
  expressions.

Also, an empty ``valrec`` form (|ie| ``(valrec)``) should be a syntax
error.

.. note::

   The main difference between ``valrec`` and ``letrec``
   is that ``valrec`` has no body and the bindings are visible
   in the rest of the file.

Implement the ``valrec`` semantics in the evaluator code in ``eval.ml``
(in the function ``eval_def``).

Once this is done, you’ll be able to define mutually-recursive functions
at the top level easily:

.. code-block:: text

   (valrec
     [even? (lambda (n) (if (= n 0) #t (odd? (- n 1))))]
     [odd? (lambda (n) (if (= n 0) #f (even? (- n 1))))])

When a ``valrec`` form is entered at the REPL,
it needs to be evaluated and then the names bound
should be printed out along with their values,
just like is done with a ``val`` expression.
Extend the REPL printing code in ``uscheme.ml``
(specifically, the ``repl_print`` helper function) to achieve this.

.. note::

   ``valrec`` returns an environment and a |uscheme| value,
   like all definition forms.
   In this case, the |uscheme| value
   needs to be able to contain multiple values
   (for multiple mutually-recursive functions).
   To do this, create a |uscheme| value
   which is a |uscheme| list containing the values.
   (Recall that a |uscheme| list
   is made from pairs ``cons``\ed to each other
   and ending in a ``nil`` value.)

The files you need to change for this problem
are ``eval.ml`` and ``uscheme.ml``.


6. Functions with variable numbers of arguments
-----------------------------------------------

In part A, you implemented the syntax
for functions with variable numbers of arguments.
Here, you have to make them work.

At the level of the IR, there is only one form we have to worry about,
which is the (extended) ``Lambda`` form.  (Hooray for desugaring!)
Here is its definition:

.. code-block:: text

   (* In ir.ml: *)

   type expr =
     | ...
     | Lambda of loc * id list * id option * expr
     | ...

The ``id list`` represents the required arguments,
the ``id option`` represents the "rest" argument
for all the extra arguments after the required arguments
(if a rest argument is used and if there are any more arguments),
and the ``expr`` is the function body.

Here is what you need to do:

#. You need to add an extra error constructor
   to the ``uscheme_error_tag`` type in ``error.ml``:

   .. code-block:: text

      ...
      | CallErrorRest of int * int
      ...

   This represents invalid calls to functions with rest arguments (|ie|
   functions which can take arbitrary numbers of arguments). The first
   ``int`` is the number of required arguments expected, while the
   second is the number received. The number of received arguments must
   be at least as large as the number of required arguments, or it’s an
   error. Define an OCaml function ``call_err_rest`` in analogy to
   ``call_err`` in ``error.ml`` and ``error.mli`` to make it easier to
   signal errors of this kind, and then use it where appropriate
   (|ie| in ``eval.ml``).

#. Modify the ``value`` type definition in ``env.ml`` and ``env.mli``;
   specifically, change the ``UserFuncVal`` constructor to this:

   .. code-block:: text

      type env = ...
      and value =
        | ...
        | UserFuncVal of Ast.id list * Ast.id option * Ir.expr * env
        | ...

   The ``Ast.id option`` field is ``None`` if the user function
   doesn’t have a "rest" argument for non-required arguments, and it’s
   ``Some <name>`` if it does.

#. Extend the evaluator code in ``eval.ml`` to handle function calls
   involving the extended ``Lambda`` form.
   Here are the semantics:

   * If there is a "rest" argument:

     * If there are too few required arguments,
       raise a ``CallErrorRest`` exception
       using the ``call_err_rest`` function.
     * Otherwise, bind the required arguments as usual,
       collect all other arguments into a |uscheme| list,
       and bind that list to the rest argument.

   * If there is no "rest" argument,
     just call the function the usual way.

You need to modify several different files in this problem:
``env.ml``, ``env.mli``, ``error.ml``, ``error.mli``, and ``eval.ml``.

7. Other modifications
----------------------

You need to make a trivial modification in ``uscheme.ml``
on line 52:

.. code-block:: ocaml

   let scheme_basis = ""

Change this to:

.. code-block:: ocaml

   let scheme_basis = Basis.scheme_basis

This will make the interpreter load the |uscheme| "basis"
(the built-in |uscheme| functions) when it starts.
The reason this change wasn't already in the code
is because, if it were there,
the |uscheme| interpreter compiled from the unmodified code
would crash as soon as it started.
As is, it doesn't, so you can run |eg| syntax tests
even if you haven't made any changes to the code.

