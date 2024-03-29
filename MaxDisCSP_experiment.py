#!/usr/bin/env python2
# """
# FRODO: a FRamework for Open/Distributed Optimization
# Copyright (C) 2008-2019  Thomas Leaute, Brammert Ottens & Radoslaw Szymanek

# FRODO is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# FRODO is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.

# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.


# How to contact the authors: 
# <https://frodo-ai.tech>
# """

# """ This script attempts to reproduce the experimental results in the following paper. 
# The results we obtain are different because they used a lexicographic variable ordering heuristic, 
# while FRODO uses a (smarter) max-width heuristic (see report below). 
# http://infoscience.epfl.ch/record/188532?ln=en

# Amir Gershman, Roie Zivan, Tal Grinshpoun, Alon Grubshtein, and Amnon Meisels. 
# Measuring distributed constraint optimization algorithms. 
# In Proceedings of the AAMAS'08 Distributed Constraint Reasoning Workshop (DCR'08), 
# pages 17-24, Estoril, Portugal, May 13 2008.
# """

# Add the FRODO benchmarks folder to the Python path and import the frodo2 module
import sys
sys.path.append("../frodo2.17.1.jar/frodo2/benchmarks")
import frodo2
import Tkinter as tk
import matplotlib.pyplot as plt
import pandas as pd

# matplotlib.path.append("../frodo2.17.1.jar/frodo2/benchmarks");
# The command to call java and the JVM parameters
java = "java"
javaParams = [
			"-Xmx2G", # sets the Java heap space to 2 GB
			"-classpath", "../frodo2.17.1.jar", # sets the Java classpath to include FRODO
			]

# Define the random problems to be generated
generator = "frodo2.benchmarks.maxdiscsp.MaxDisCSPProblemGenerator"
genParams = [
			[5], # the number of variables
			20, # the domain size
			.9, # the target p1 value
			[.2,0.6,0.7,0.8,0.9], # the varying target p2 values
			]
problemFile = "random_Max-DisCSP.xml"
nbrProblems = 1 # for each combination of generator options, the number of problems to run the algorithms on

# Define the algorithms to run
# Each algorithm is a list [algoName, solverClassName, agentConfigFilePath, inputProblemFilePath]
algos = [
        ["ADOPT", "frodo2.algorithms.adopt.ADOPTsolver", "../agents/ADOPT/ADOPTagentJaCoP.xml", problemFile], 

        # ["AFB", "frodo2.algorithms.afb.AFBsolver", "../agents/AFB/AFBagent.xml", problemFile], 

		# ["DPOP", "frodo2.algorithms.dpop.DPOPsolver", "../agents/DPOP/DPOPagentJaCoP.xml", problemFile], 
#         ["ASO-DPOP", "frodo2.algorithms.asodpop.ASODPOPsolver", "../agents/DPOP/ASO-DPOP/ASO-DPOPagent.xml", problemFile], 
        # ["MB-DPOP", "frodo2.algorithms.dpop.DPOPsolver", "../agents/DPOP/MB-DPOP/MB-DPOPagentJaCoP.xml", problemFile], 
#          ["O-DPOP", "frodo2.algorithms.odpop.ODPOPsolver", "../agents/DPOP/O-DPOP/O-DPOPagent.xml", problemFile], 
#          ["P-DPOP", "frodo2.algorithms.dpop.privacy.P_DPOPsolver", "../agents/DPOP/P-DPOP/P-DPOPagent.xml", problemFile], 
#          ["P3/2-DPOP", "frodo2.algorithms.dpop.privacy.P3halves_DPOPsolver", "../agents/DPOP/P-DPOP/P1.5-DPOPagent.xml", problemFile], 
#         ["P2-DPOP", "frodo2.algorithms.dpop.privacy.P2_DPOPsolver", "../agents/DPOP/P-DPOP/P2-DPOPagent.xml", problemFile], 
# 
#       ["DUCT", "frodo2.algorithms.duct.DUCTsolver", "../agents/DUCT/DUCTagent.xml", problemFile], 
#
        ["DSA", "frodo2.algorithms.localSearch.dsa.DSAsolver", "../agents/DSA/DSAagentJaCoP.xml", problemFile], 
# 
        ["MaxSum", "frodo2.algorithms.maxsum.MaxSumSolver", "../agents/MaxSum/MaxSumAgentJaCoP.xml"], 
# 
        ["MGM", "frodo2.algorithms.localSearch.mgm.MGMsolver", "../agents/MGM/MGMagentJaCoP.xml", problemFile], 
#         ["MGM2", "frodo2.algorithms.localSearch.mgm.mgm2.MGM2solver", "../agents/MGM/MGM2agent.xml", problemFile], 

        # ["SynchBB", "frodo2.algorithms.synchbb.SynchBBsolver", "../agents/SynchBB/SynchBBagent.xml", problemFile], 
        ]

# algos = [
# ["ADOPT", "frodo2.algorithms.adopt.ADOPTsolver", "../agents/ADOPT/ADOPTagentJaCoP.xml"], 

# ["AFB", "frodo2.algorithms.afb.AFBsolver", "../agents/AFB/AFBagentJaCoP.xml"], 

# ["DPOP", "frodo2.algorithms.dpop.DPOPsolver", "../agents/DPOP/DPOPagentJaCoP.xml"], 
# #         ["ASO-DPOP", "frodo2.algorithms.asodpop.ASODPOPsolver", "../agents/DPOP/ASO-DPOP/ASO-DPOPagent.xml", problemFile], 
# #         ["MB-DPOP", "frodo2.algorithms.dpop.DPOPsolver", "../agents/DPOP/MB-DPOP/MB-DPOPagent.xml", problemFile], 
# #          ["O-DPOP", "frodo2.algorithms.odpop.ODPOPsolver", "../agents/DPOP/O-DPOP/O-DPOPagent.xml", problemFile], 
# #          ["P-DPOP", "frodo2.algorithms.dpop.privacy.P_DPOPsolver", "../agents/DPOP/P-DPOP/P-DPOPagent.xml", problemFile], 
# #          ["P3/2-DPOP", "frodo2.algorithms.dpop.privacy.P3halves_DPOPsolver", "../agents/DPOP/P-DPOP/P1.5-DPOPagent.xml", problemFile], 
# #         ["P2-DPOP", "frodo2.algorithms.dpop.privacy.P2_DPOPsolver", "../agents/DPOP/P-DPOP/P2-DPOPagent.xml", problemFile], 
# # 
# #       ["DUCT", "frodo2.algorithms.duct.DUCTsolver", "../agents/DUCT/DUCTagent.xml", problemFile], 
# #
# #         ["DSA", "frodo2.algorithms.localSearch.dsa.DSAsolver", "../agents/DSA/DSAagent.xml", problemFile], 
# # 
# #         ["MaxSum", "frodo2.algorithms.maxsum.MaxSumSolver", "../agents/MaxSum/MaxSumAgent.xml", problemFile], 
# # 
# #         ["MGM", "frodo2.algorithms.localSearch.mgm.MGMsolver", "../agents/MGM/MGMagent.xml", problemFile], 
# #         ["MGM2", "frodo2.algorithms.localSearch.mgm.mgm2.MGM2solver", "../agents/MGM/MGM2agent.xml", problemFile], 

# ["SynchBB", "frodo2.algorithms.synchbb.SynchBBsolver", "../agents/SynchBB/SynchBBagentJaCoP.xml"], 
# ]
timeout = 120 # in seconds

# The CSV file to which the statistics should be written
output = "outputMaxDisCSP.csv"

# Run the experiment
frodo2.run(java, javaParams, generator, genParams, nbrProblems, algos, timeout, output, saveProblems = False)

# frodo2.runFromRepo(java, javaParams, "/home/ahmad/Documents/Frodo/frodo2/experiments/problems",	1 , algos, timeout, output)


# Tip: if some of the algorithms tend to time out most of the time on some problem files, 
# you can run 2 experiments: one for all algorithms on the smaller problem sizes, 
# and one with only the faster algorithms on the larger problem sizes

# Plot the graphs
frodo2.plot(output, xCol = 9, yCol = 14) # yCol = 12 is the NCCC count (the first column has index 0)
frodo2.plot(output, xCol = 9, yCol = 13) # yCol = 12 is the NCCC count (the first column has index 0)
frodo2.plot(output, xCol = 9, yCol = 19) # yCol = 12 is the NCCC count (the first column has index 0)
# frodo2.plot(output, xCol = 0, yCol = 5) # yCol = 12 is the NCCC count (the first column has index 0)
# frodo2.plot(output, xCol = 7, yCol = 14) # yCol = 12 is the NCCC count (the first column has index 0)
# frodo2.plot(output, xCol = 7, yCol = 14) # yCol = 12 is the NCCC count (the first column has index 0)
# data = pd.read_csv(output, sep=',',header=None, index_col =0)

# data.plot(kind='bar')
# plt.ylabel('Time')
# plt.xlabel('Algorithm')
# plt.title('Time ')

# plt.show()