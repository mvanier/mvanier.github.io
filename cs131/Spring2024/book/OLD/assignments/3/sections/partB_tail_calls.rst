Part B: Implementing tail call optimization
===========================================

Tail call optimization is a way of making sure that the context stack
doesn’t grow unnecessarily. For instance, consider the following
functions:

.. code-block:: text

   (define fact-iter (n r)
     (if (= n 0)
         r
         (fact-iter (- n 1) (* r n))))
   (define fact (n) (fact-iter n 1))

The ``fact`` function computes factorials of non-negative integers. It
does this by dispatching to the tail-recursive function ``fact-iter``.
With tail call optimization (often abbreviated as TCO, and often
inaccurately referred to as "tail recursion optimization")  [1]_, the
recursive calls to ``fact-iter`` will not have to return to the call
site and the entire function can execute in a constant context stack
size (|ie| not dependent on the value of ``n``). Without TCO, every
recursive call will put down a ``CallEnvFrame`` onto the context stack,
even if the top of the context stack was another ``CallEnvFrame``. This
is unnecessary; the previous ``CallEnvFrame`` is sufficient because the
subsequent ``CallEnvFrame`` will change the environment when removed
from the stack and then the previous one will immediately change it
again. It’s also undesirable because it grows the context stack; in a
very deep recursion, the context stack might grow to a point where the
implementation runs out of stack space (the dreaded "stack overflow").
Having TCO is what allows us to use recursion in place of explicit
``while`` loops in functional languages. This is necessary to enable the
functional style of programming you learned about in CS 4.

To see how ``CallEnvFrame``\s can build up without TCO, consider the
function call ``(fact-iter 5 1)``. This will push an ``ApplyFrame``
frame onto the context stack. Once all the argument values are
evaluated, it will be replaced by a ``CallEnvFrame``. Then the body of
``fact-iter`` will be evaluated, which will push an ``IfFrame`` onto the
context stack while ``(= n 0)`` is evaluated. Once that happens, the
``IfFrame`` is popped off, to be replaced (in the false case) with the
recursive call to ``fact-iter``. This gives rise to another
``ApplyFrame``, and once the arguments to the recursive call are
evaluated, the ``ApplyFrame`` will be popped off the context stack and
another ``CallEnvFrame`` will be pushed onto the stack so that the body
of ``fact-iter`` can be evaluated again. From this we can see that we
will eventually have a sequence of consecutive ``CallEnvFrame``\s on
the context stack, even though the only one that matters is the lowest
one. TCO will get rid of this problem.

The good news is that implementing TCO is quite easy!
There are two changes you need to make.

1. ``push_env_frame``
---------------------

First, you need to edit the ``push_env_frame`` function in ``eval.ml``.
This function is only used to push either
``LetEnvFrame`` or ``CallEnvFrame`` frames,
each of which just wraps an environment which needs to be restored.
If ``push_env_frame`` determines that pushing the environment is unnecessary,
it doesn’t push the frame; otherwise, it does.
The rules that you should use to decide
if the environment push is unnecessary are the following:

#. Pushing a ``CallEnvFrame`` onto another ``CallEnvFrame`` is
   unnecessary. This is the "classic" tail-call optimization.

#. Pushing a ``LetEnvFrame`` onto a ``CallEnvFrame`` is also
   unnecessary.

#. Pushing a ``LetEnvFrame`` onto another ``LetEnvFrame`` is also
   unnecessary.

#. Pushing a ``CallEnvFrame`` onto a ``LetEnvFrame``\... here’s where
   things get weird. On the one hand, you might think that you don’t
   need to do this because the ``CallEnvFrame`` environment will be
   immediately replaced by a ``LetEnvFrame`` environment. On the other
   hand, a ``CallEnvFrame`` is used to indicate where to unwind a
   ``return`` to, and to signal an error if a ``break`` unwinds to this
   point, so you can’t just get rid of it. On the other other hand, just
   doing nothing isn’t optimal either. Think about this and figure out
   the best approach. (Ask a TA or the instructor if you need a hint.
   The test scripts will *definitely* be checking this.)
   The good news is that the correct approach is very simple
   and doesn't require much code.

There is some debugging/tracing code in ``push_env_frame`` which you
shouldn’t alter. The part you should edit consists of this line:

.. code-block:: ocaml

   stack_push l frame context  (* TODO FIXME: This is incorrect. *)

Of course, this line will need to be replaced by more than one line.


2. Taking account of ``begin``
------------------------------

The last expression in a ``begin`` expression is considered to be
"in tail position" if the ``begin`` expression itself is.
What this means, basically, is that when you are evaluating the
last subexpression of a non-empty ``begin`` expression,
you shouldn't push an empty ``BeginFrame`` onto the stack,
because if you do, you will break tail call optimization.
Fortunately, this is very easy to implement,
and the tests will check that you did it right.


Testing tail-call optimization
------------------------------

We’ve added some instrumentation to the supplied code to make it easy to
check if your implementation of tail-call optimization is, well,
optimal. The built-in function ``trace-fs`` (which stands for **"trace
frame stack"**) takes a single integer, normally ``0``, ``1``, or ``2``.
Any integer larger than ``2`` is equivalent to ``2``, and any integer
less than ``0`` is an error. Here’s what these settings mean:

``(trace-fs 0)``
  Turn stack tracing off. Return the maximum depth of the stack since
  the last time stack tracing was turned on. (If it was never turned
  on, return 0.)

``(trace-fs 1)``
  Turn stack tracing on. Return ``0``. Every time ``push_env_frame`` is
  called, print the current stack frame depth.

``(trace-fs 2)``
  Turn stack tracing on. Return ``0``. Every time ``push_env_frame`` is
  called, print a representation of the state of the stack frame as
  well as the stack frame depth.

Let’s see how this works through two example functions:

.. code-block:: text

   ;; not tail-recursive:
   (define fact (n)
     (if (= n 0)
         1
         (* n (fact (- n 1)))))

   ;; tail-recursive:
   (define fact-iter (n r)
     (if (= n 0)
         r
         (fact-iter (- n 1) (* r n))))

If we set stack tracing to ``0``, no tracing happens:

.. code-block:: text

   >>> (trace-fs 0)
   0
   >>> (fact 10)
   3628800
   >>> (fact-iter 10 1)
   3628800
   >>> (trace-fs 0)
   0

If we turn on stack tracing, look what happens:

.. code-block:: text

   >>> (trace-fs 1)
   0
   >>> (fact 10)
   Stack frame depth: 0
   Stack frame depth: 2
   Stack frame depth: 4
   Stack frame depth: 6
   Stack frame depth: 8
   Stack frame depth: 10
   Stack frame depth: 12
   Stack frame depth: 14
   Stack frame depth: 16
   Stack frame depth: 18
   Stack frame depth: 20
   3628800
   >>> (trace-fs 0)
   20

The non-tail-recursive ``fact`` function grew the context stack to 20
frames when executing ``(fact 10)``. If we try this with
``(fact-iter 10 1)``, **and if we have properly implemented tail call
optimization**, we see something very different:

.. code-block:: text

   >>> (trace-fs 1)
   0
   >>> (fact-iter 10 1)
   Stack frame depth: 0
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   Stack frame depth: 1
   3628800
   >>> (trace-fs 0)
   1

Note that the stack depth doesn’t grow beyond ``1``.

On the other hand, if tail call optimization is **not** implemented, we
see this:

.. code-block:: text

   >>> (trace-fs 1)
   0
   >>> (fact-iter 10 1)
   Stack frame depth: 0
   Stack frame depth: 1
   Stack frame depth: 2
   Stack frame depth: 3
   Stack frame depth: 4
   Stack frame depth: 5
   Stack frame depth: 6
   Stack frame depth: 7
   Stack frame depth: 8
   Stack frame depth: 9
   Stack frame depth: 10
   3628800
   >>> (trace-fs 0)
   10

Now the stack is growing proportional to the size of the ``n`` argument.

If we really want to see exactly what’s happening on the context stack,
we should use tracing level 2, which prints out the state of the entire
stack instead of just the depth. Let’s see it in both the
non-tail-recursive and tail-recursive cases.

.. code-block:: text

   >>> (trace-fs 2)
   0
   >>> (fact 3)
   Stack frame depth: 0

   == Frame stack ==

     <empty>

   Stack frame depth: 2

   == Frame stack ==

     ApplyFrame
     CallEnvFrame

   Stack frame depth: 4

   == Frame stack ==

     ApplyFrame
     CallEnvFrame
     ApplyFrame
     CallEnvFrame

   Stack frame depth: 6

   == Frame stack ==

     ApplyFrame
     CallEnvFrame
     ApplyFrame
     CallEnvFrame
     ApplyFrame
     CallEnvFrame

   6

Note the alternating ``ApplyFrame``\s and ``CallEnvFrame``\s on the
stack. Compare this to the corresponding call to ``fact-iter``:

.. code-block:: text

   >>> (trace-fs 2)
   0
   >>> (fact-iter 3 1)
   Stack frame depth: 0

   == Frame stack ==

     <empty>

   Stack frame depth: 1

   == Frame stack ==

     CallEnvFrame

   Stack frame depth: 1

   == Frame stack ==

     CallEnvFrame

   Stack frame depth: 1

   == Frame stack ==

     CallEnvFrame

   6

On the other hand, if tail call optimization is **not** implemented, we
see this:

.. code-block:: text

   >>> (trace-fs 2)
   0
   >>> (fact-iter 3 1)
   Stack frame depth: 0

   == Frame stack ==

     <empty>

   Stack frame depth: 1

   == Frame stack ==

     CallEnvFrame

   Stack frame depth: 2

   == Frame stack ==

     CallEnvFrame
     CallEnvFrame

   Stack frame depth: 3

   == Frame stack ==

     CallEnvFrame
     CallEnvFrame
     CallEnvFrame

   6

Note how the ``CallEnvFrame``\s pile up on the context stack.

Using stack tracing will be very helpful in debugging tail call
optimization. It’s also used by the test scripts.

----

.. rubric:: Footnotes

.. [1] A tail call does not have to be recursive.

