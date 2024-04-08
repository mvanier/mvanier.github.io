Part A: Implementing control operators
======================================

In the file ``eval.ml``, there are a number of places marked
``failwith "TODO"`` where you need to finish the implementation of
various features. In this section we will ignore one of those places
(inside the ``push_env_frame`` function); that will be the topic of the
next section. All the other ``TODO``\s are in two functions:

* ``eval_val_in_frame``
* ``reduce_expr``

In addition, all of the ``TODO``\s relate to the implementation of the
following control operators:

* ``break``
* ``continue``
* ``return``
* ``throw``
* ``try-catch``

Your job in this section is to implement these control operators, using
the small-step operational semantics from lecture 9 as your guide. When
editing ``eval.ml``, always write your code in one of the locations
marked ``TODO`` unless the instructions below specifically say not to.
(Of course, replace the ``failwith "TODO"`` code with your code.) Some
features require you to edit code in other files as well; this will be
described below, but there are no ``TODO`` indicators for this code.

Pay attention to the functional stack operations in the helper functions
section in ``eval.ml``. Use these to do all stack operations. Note that
there is an ``Fstack`` module in the code base which implements
functional stacks; the stack helper functions in ``eval.ml`` are just
wrappers around these functions. (The name ``Fstack`` means "functional
stack"; it’s called that mainly to distinguish itself from the ``Stack``
module, which is imperative and is part of the OCaml standard library.)
Notably, the ``stack_push`` function converts an ``Fstack`` ``Overflow``
exception to a |uschemeplus| exception. You will lose marks if you use
``Fstack`` functions directly.

Here are some notes about each form:


1. ``break``
------------

File to edit:

* ``eval.ml`` (``reduce_expr`` only)

There are two ways to implement ``break``. One is a direct
implementation in the code of the small-step operational semantics. The
other is an optimized form, which optimizes ``break`` expressions much
like we optimized ``while`` expressions in assignment 1 for the *Imp*
language. Either way is acceptable. The optimized form will require you
to define a helper function.

Make sure you don’t allow ``break`` to cross function call boundaries!
Also, if ``break`` ends up with an empty context stack, it’s an error.
Use the ``Error.break_outside_while`` function
to signal ``break``-related errors.


2. ``continue``
---------------

Files to edit:

* ``ast.ml``
* ``ast.mli``
* ``error.ml``
* ``error.mli``
* ``eval.ml`` (``reduce_expr`` only)

To implement ``continue``, first you have to implement syntactic support
for it. In the files ``ast.ml`` and ``ast.mli``, add a
``Continue`` constructor to the ``expr`` datatype with a single ``loc``
argument (like the ``Break`` constructor). Update the ``loc_of_expr``
function to cover the new case, then extend ``parse_expr`` to parse
``continue`` expressions. Don’t forget to update ``ast.mli`` with
whatever changes are needed.

Since ``continue`` is a keyword, make sure to add it to the list of
keywords in ``ast.ml``.

Then you should add an extra error constructor called
``ContinueOutsideWhile`` to the ``uscheme_error_tag`` type in
``error.ml``. This constructor takes no arguments. Update the
``print_err`` function to handle the additional constructor. Add a
``continue_outside_while`` error function in analogy to
``break_outside_while`` to ``error.ml`` and put its signature in
``error.mli``.

Everything else you need to do for ``continue`` is in the file
``eval.ml``, in the function ``reduce_expr``. (Note that there is no
``TODO`` location for this; you should figure out an appropriate place
to put the code.) Once again, you can implement this in a basic or an
optimized way, analogous to what you did with ``break``. And again, make
sure that ``continue`` doesn’t cross function call boundaries. Use the
``Error.continue_outside_while`` function to signal ``continue``-related
errors. As with ``break``, if the context stack becomes empty during a
``continue``, it’s an error.


3. ``return``
-------------

File to edit:

* ``eval.ml`` (both the ``reduce_expr`` and ``eval_val_in_frame``
  functions)

Again, there is a basic and an optimized way to implement ``return``. If
you choose to do this the basic way, you will have to change
``eval_val_in_frame`` to a ``let rec`` definition, which we will allow.
If you do this the optimized way, you can leave it as a plain ``let``.

Use the ``Error.return_outside_func`` function to signal
``return``-related errors. As with ``break`` and ``continue``, it’s an
error if the context stack becomes empty during a ``return``.

Note that ``(return)`` is illegal in |uschemeplus|. If you want the
effect of returning "nothing", you can write ``(return #u)``. ``return``
must always have a single expression whose corresponding value is the
returned value.


4. ``throw`` and ``try-catch``
------------------------------

File to edit:

* ``eval.ml`` (both the ``reduce_expr`` and ``eval_val_in_frame``
  functions)

Implementing ``throw`` is pretty straightforward; it’s just a direct
implementation of the operational semantics. Unwinding the stack is done
in ``eval_val_in_frame``, not in ``reduce_expr``, since you need to
evaluate the expression to throw before you unwind the stack. The
unwinding can again be done in a basic or an optimized way, and again
either is acceptable. You have to unwind the stack until you hit a
``TryCatchFrame``, and you *can* cross function call boundaries (because
that’s kind of the whole point of exception handling).

``try-catch`` is a bit subtle; you first have to evaluate the *catch*
expression to a value, which should be a function (that’s the exception
handler). In ``reduce_expr``, you have to push a
``TryCatchEvalHandlerFrame`` onto the stack before evaluating the
exception handler. (This corresponds to the ``Try-Catch`` operational
semantics rule in lecture 9.) Once the exception handler has been
evaluated to a value, then (in ``eval_val_in_frame``) you need to push a
different frame (``TryCatchFrame``) onto the stack. (This is called
"installing the exception handler".) Before you do this, you need to
check that the exception handler is in fact a function (either a user
function or a primitive is acceptable). We’ve included that code for
you. Once this is done, you need to first push a ``LetEnvFrame`` frame
onto the context stack, followed by the ``TryCatchFrame``. (This
corresponds to the ``Try-Catch-Handler`` operational semantics rule in
lecture 9.) The case where no exception is thrown inside a ``try-catch``
form corresponds to the ``Try-Catch-Finish`` operational semantics rule;
this rule is implemented for you.

Note that ``throw`` unwinds the stack like ``break``, ``continue`` and
``return`` do, but unlike them it unwinds through function call
boundaries. The only thing that can stop ``throw`` from unwinding is
hitting a ``TryCatchFrame`` (at which point the exception handler is
invoked) or emptying out the stack completely without hitting a
``TryCatchFrame`` (which is an error; use the ``Error.uncaught_throw``
function to signal this). Invoking the exception handler should be done
by calling the ``eval_call`` function.


5. Question about ``LetEnvFrame``
---------------------------------

In lecture 9, it says that the reason for installing the ``LetEnvFrame``
first and then installing the ``TryCatchFrame`` is in case there are
more forms to evaluate after the ``try-catch`` expression completes. But
what about the exception handler itself? Why don’t we have to install
the environment in the ``LetEnvFrame`` *before* executing the exception
handler? We certainly don’t want the exception handler to execute in the
context of an environment from a different function. Think about this,
and write the answer in a comment in the ``eval.ml`` file
(near the top).

Once all of these features have been implemented, all of the test
scripts except for ``recurse_n.scm`` and ``tail.scm`` should work. These
scripts require tail call optimization, which is the topic of part B.
