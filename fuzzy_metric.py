__author__ = 'rkhozinov'

import fuzzy.storage.fcl.Reader
system = fuzzy.storage.fcl.Reader.Reader().load_from_file("your_definitions.fcl")

# preallocate input and output values
my_input = {
        "in_var1" : 0.0,
        "in_var2" : 0.0
        }
my_output = {
        "out_var" : 0.0
        }

# if you need only one calculation you do not need the while
while ...:
        # set input values
        my_input["in_var1"] = ...
        my_input["in_var2"] = ...

        # calculate
        system.calculate(my_input, my_output)

        # now use outputs
        ... = my_output["out_var"]