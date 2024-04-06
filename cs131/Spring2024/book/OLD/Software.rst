.. _Software:

Installing the course software
==============================

Here are the steps you should go through to install OCaml on your computer.
If you have already installed OCaml (|eg| because you took CS 4),
you can skip most of these instructions.
Please make sure that you are running a current version of OCaml
(version |ocamlversion| or later).
If not, use ``opam`` to upgrade your OCaml version as described below.

Installing OCaml and ``opam``
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

See the `instructions <https://ocaml.org/learn/tutorials/up_and_running.html>`_
on the OCaml website.

.. note::

   ``opam`` is the OCaml package manager.
   It should be installed along with OCaml.

   Be aware that the version numbers may have changed
   since that document was written. Be sure to install the correct version!
   For this term, we are using OCaml version |ocamlversion|.


MacOS
^^^^^

The instructions for MacOS refer to the Homebrew_ package manager,
which needs to be installed first.

.. note::

   Homebrew does not come pre-installed on Macs,
   but it is easy to install and is incredibly useful.
   Almost any open-source command-line software you can imagine
   can be installed using Homebrew with a single command.
   (This includes most programming languages, including OCaml).

Whatever you do, don't use the MacPorts package manager (incorrectly referred
to as "MacPort" in the OCaml website).  Similarly, don't use
Fink.  Use Homebrew.  (Trust us on this.)

Linux
^^^^^

If you are using Linux, you can install OCaml using your system's
package manager.  If your Linux is an Ubuntu variant (the most common kind),
you should install OCaml using the Ubuntu package manager ``apt``
(also known as ``apt-get``).
Installation instructions for Ubuntu and other Linux distributions
are available on `this page <https://opam.ocaml.org/doc/Install.html>`_.
For Ubuntu the commands are:

.. code-block:: text

   $ sudo apt update
   $ sudo apt install opam

The ``sudo`` is so you don't have to install the software as the root user.
You may be asked to enter your password, which you should do.

.. note::

   The instructions on the website also say do this first:

   .. code-block:: text

      $ sudo add-apt-repository ppa:avsm/ppa

   This is unnecessary, since we will show you how to upgrade OCaml below.


Windows
^^^^^^^

If you are using Windows, your should install a Linux system inside of
Windows using the Windows Subsystem for Linux (WSL_).
Then you can use ``apt`` like on any Ubuntu Linux system.
Don't try to install OCaml natively on Windows;
although this may be possible, you are very likely to run into problems
beyond the capability of the instructors to solve.  Use WSL.


Initialization
--------------

Once you have a version of OCaml and ``opam`` installed, you need to set it up,
and, if necessary, upgrade the versions. Go through the following steps.

* Start a terminal.  If you are running MacOS, you can use the ``Terminal``
  program.  If you're using Windows/WSL, you can either use
  `Windows Terminal`_ or use `Visual Studio Code`_ and start a terminal
  inside the editor.  If you're using Windows, **make sure** you are
  running WSL inside the terminal and not Powershell!  Both Windows Terminal
  and VS Code allow you to select either one, assuming WSL has been installed.

* Initialize ``opam`` by typing ``opam init`` in a terminal 
  and following the instructions.

.. note::

   There is an issue with initializing ``opam`` inside of WSL
   (Windows Subsystem for Linux).
   Basically, due to the way WSL works,
   you have to initialize ``opam`` differently by disabling sandboxing.
   This theoretically can cause some problems
   if a package does something really stupid, but it probably won't.
   The fix is to use this command instead of just ``opam init``:

   .. code-block:: text

      $ opam init --disable-sandboxing

* At the end of the ``opam init`` command, 
  it will ask you to run ``eval ${opam env}`` [1]_
  to set up the ``PATH`` variable of your shell
  to point to the ``opam`` directories.
  This is important, because if you don't do this,
  adding new packages (and new OCaml versions) will not work.
  Opam will also ask you if it can change your shell initialization file
  (|eg| ``~/.bashrc`` for ``bash`` or ``~/.zshrc`` for ``zsh``)
  by adding some commands.
  You should say yes, because if you don't,
  every time you start up a new terminal
  you will have to type ``eval ${opam env}``
  to get ``opam`` to work correctly.

* In your terminal, type:

  .. code-block:: text

     $ opam update
     $ opam upgrade

  to make sure the package repository is up to date and all OCaml packages have
  been upgraded to the most recent versions.

* Check the OCaml version by typing ``ocaml --version``.
  If it's the desired version (which is |ocamlversion| this term),
  you are done with this part.
  Otherwise do

  .. code-block:: text

     $ opam switch create 4.14.1

  and wait for the new version to be installed.
  (This will take a while.)

* Install some libraries and programs:

  .. code-block:: text

    $ opam install utop dune ocamlformat

  This will install the ``utop``, ``dune``, and ``ocamlformat`` programs,
  as well as a number of OCaml libraries you don't have to worry about.

  ``utop`` is the OCaml interactive interpreter.
  We will be working with this a lot!

  .. note::

     There is a more primitive interactive interpreter just called ``ocaml``
     that comes with the OCaml distribution.  We won't use this, because
     ``utop`` is vastly more featureful and nicer to use.
  
  ``dune`` is the OCaml compilation manager.  We will introduce this
  when we need it (which won't be for a few weeks).

  ``ocamlformat`` is an auto-formatter for OCaml code.
  You don't actually need this, but it gets handy when you are working
  with longer files of code and you want to make sure everything is
  formatted neatly.


Using OCaml
-----------

There are only a few things you need to know in order to use OCaml effectively.

Starting OCaml
^^^^^^^^^^^^^^

Open a terminal and type:

.. code-block:: text

    $ utop

at the terminal prompt. (The ``$`` is the terminal prompt; don't type that.)
This will bring up the OCaml interactive interpreter,
which is a good environment for experimenting with the language
and for testing code you've written.
(This is similar to the Python interactive interpreter.)

.. note::

   You can also type ``ocaml`` instead of ``utop``, which will bring up
   the simpler interactive interpreter that ships with the OCaml distribution.
   However, ``utop`` is so much better than ``ocaml`` that there is no
   need to do this unless ``utop`` isn't working for some reason.

``utop`` makes it possible to easily recall and edit
previously-input lines of text by using the up and down arrow keys.
To see what it can do, start up ``utop``:

.. code-block:: text

    $ utop

and type the following commands (one per line, hitting the return key at the
end of each line):

.. code-block:: ocaml

    # Printf.printf "hello\n" ;;
    # Printf.printf "goodbye\n" ;;

Note that the OCaml interpreter prompt is the hash sign (``#``);
don't type that!
These lines should, when evaluated,
print the words ``"hello"`` and ``"goodbye"`` respectively.
Once you've done this,
you should be able to recall either line by using the up arrow key.
For instance, you can hit the up arrow key once to get the line:

.. code-block:: ocaml

    Printf.printf "goodbye\n" ;;

and edit it so that it says:


.. code-block:: ocaml

    Printf.printf "hasta la vista\n" ;;

When you hit return, it should print out ``"hasta la vista"`` on a separate
line.  This feature makes line editing much easier.
You can also use control-a (hold the control key down and type ``a``)
to get to the beginning of a line you are editing
and control-e to get to the end.
control-l (lower-case L) clears the terminal and puts the cursor
at the top of the terminal window.

Exit the interpreter by typing control-d (this is just like Python).
You can also type ``#quit;;`` at the prompt:

.. code-block:: ocaml

   # #quit;;

and that will also work.


Configuring ``utop``
^^^^^^^^^^^^^^^^^^^^

One annoying thing about ``utop`` is that it tends to go overboard on
command completion.  You can disable this by typing this inside ``utop``:

.. code-block:: ocaml

   # #utop_prompt_dummy;;
   # UTop.set_show_box false;;

It's annoying to type this every time you start ``utop``,
so what I do is make a ``utop`` initialization file called ``init.ml``
and put it in the directory ``~/.config/utop``
(creating that directory if necessary).
Then these commands will be run every time ``utop`` starts.

.. note::

   Unfortunately, this will not work properly
   if used in a directory that has a ``.ocamlinit`` file in it
   (usually your home directory).
   There are workarounds, but most of the time it will work properly.
   Other than that, ``utop`` is far superior
   to the basic ``ocaml`` interpreter.


Using Visual Studio Code
^^^^^^^^^^^^^^^^^^^^^^^^

You can use any plain text editor you like to write OCaml code,
but we recommend you try `Visual Studio Code`_ (also known as VS Code),
which has excellent OCaml support.
In order to use it, you need to do the following steps:

* Install the OCaml Language Server Protocol by typing this into a terminal:

  .. code-block:: text

     $ opam install ocaml-lsp-server

  A language server protocol provides a way for editors to query
  languages so that things like command completion and type information
  can be displayed in the editor while you're editing code.

* Install Visual Studio Code from
  `the VS Code website <https://code.visualstudio.com>`_.

* When inside VS Code, look at the Extensions pane
  (select View/Extensions from the menu).
  Type "OCaml" into the search bar at the top,
  and select and install "OCaml Platform".

Now you will get nice syntax highlighting and code completion when you edit
OCaml source code.  You can also start a terminal while inside VS Code to test
your code by running the OCaml interpreter.

.. _Homebrew: https://brew.sh

.. _WSL: https://docs.microsoft.com/en-us/windows/wsl/install-win10

.. _`Windows Terminal`: https://apps.microsoft.com/store/detail/windows-terminal/9N0DX20HK701?hl=en-us&gl=us

.. _`Visual Studio Code`: https://code.visualstudio.com/

----

.. rubric:: Footnotes

.. [1] Or possibly ``eval ${opam config env}`` or something similar.
   It all does the same thing.

