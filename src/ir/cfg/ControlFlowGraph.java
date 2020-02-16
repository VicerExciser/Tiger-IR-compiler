package ir.cfg;

import ir.cfg.*;
import ir.IRUtil;
import ir.IRFunction;
import ir.IRInstruction;
import ir.operand.IROperand;
import ir.operand.IRLabelOperand;
import ir.operand.IRVariableOperand;

import ir.IRPrinter;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


/*		Control Flow Graph

	Class for building a CFG of basic blocks for a particular IRFunction instance.

	NOTE: This CFG implementation is only tailored for individual functions,
			and is not yet capable of generating a CFG for an entire program
			(should we add this functionality? is it necessary or required?)
*/
public class ControlFlowGraph {

	public static final boolean USE_MAXIMAL_BLOCKS = true; //false;	// Else, MinBasicBlock instances will be used

	private IRFunction f;
	private Set<BasicBlockBase> blocks;
	private Set<CFGEdge> edges;
	private Set<IRInstruction> universalDefinitions;

	// private Set<BasicBlockBase> dom;
	// public BasicBlockBase iDom;

	public DominatorTree domTree;

	//// Possible structures for simplifying reaching definition lookups:
	// public Map<String, Set<IRInstruction>> universalUseMap;
	// public Map<String, Set<IRInstruction>> universalDefMap;

	BasicBlockBase entryNode;

	public ControlFlowGraph(IRFunction f) {
		this.f = f;
		this.blocks = new LinkedHashSet<>();
		this.edges = new HashSet<>();
		this.universalDefinitions = new LinkedHashSet<>();
	}

	// NOTE: Currently only implemented for using MaxBasicBlocks;
	// 			for MinBasicBlocks, will simply need to generate a new block
	//			for each and every instruction, making sure to properly
	// 			create CFGEdges for each as well as updating their respective
	// 			predecessors and successors lists.
	public void build() {
		IRPrinter debugPrinter = new IRPrinter(System.out);

		if (USE_MAXIMAL_BLOCKS) {
			// Add a fresh vertex (basic block) to the graph for each leader instruction
			IRUtil.identifyLeaders(this.f);

			for (IRInstruction i : this.f.instructions) {
				//// FOR DEBUG
				String msg = "[CFG.build] Instruction i ";
				if (i.isLeader)
					msg += "is Leader:\t";
				else
					msg += "is NOT Leader:\t";
				System.out.print(msg);
				debugPrinter.printInstruction(i);
				//// FOR DEBUG
			}

			generateInitialBlocks(this.f.instructions);

			BasicBlockBase curr = null;
			//// FOR DEBUG
			IRInstruction currInst = null;
			//// FOR DEBUG

			// Need to construct the basic blocks for each leader in this function first
			for (IRInstruction i : this.f.instructions) {
				//// FOR DEBUG
				System.out.print("[CFG.build] i:\t");
				debugPrinter.printInstruction(i);
				BasicBlockBase a, b;
				//// FOR DEBUG

				if (i.isLeader) {
					if (curr != null) {	// Then add an edge from curr to block with leader i

						boolean skipEdge = false;	//// TODO: Think of more cases for skipping CFG edge creation ////
						if (curr.terminator.opCode == IRInstruction.OpCode.GOTO) {
							if (!((MaxBasicBlock) i.belongsToBlock).beginsWithLabel())
								skipEdge = true;
							else {
								IRInstruction currTarget = IRUtil.getLabelTarget(this.f, 
									(IRLabelOperand) curr.terminator.operands[0]);
								if (((MaxBasicBlock) i.belongsToBlock).topLabel.equals(currTarget) 
										|| ((MaxBasicBlock) i.belongsToBlock).instructions.contains(currTarget))
									skipEdge = false;
								else
									skipEdge = true;
							}
						}

						if (!skipEdge) {
/*
							a = curr;
							b = i.belongsToBlock;
							boolean printDebugInfo = (a.id.equalsIgnoreCase("fib::7")) && (b.id.equalsIgnoreCase("fib::9"));
							if (printDebugInfo) {
								System.out.println("[CFG (i.isLeader)] Connecting blocks "+curr.blocknum+" --> "+i.belongsToBlock.blocknum);
								IRPrinter debugPrinter = new IRPrinter(System.out);
								System.out.print("i: ");
								debugPrinter.printInstruction(i);
								System.out.print("\n________["+curr.blocknum+"] from:\n");
								// debugPrinter.printInstruction(curr.leader);
								for (IRInstruction inst : ((MaxBasicBlock)curr).instructions) {
									debugPrinter.printInstruction(inst);
								}
								System.out.print("\n________["+b.blocknum+"] to:\n");
								for (IRInstruction inst : ((MaxBasicBlock)b).instructions) {
									debugPrinter.printInstruction(inst);
								}
								System.out.println();
							}
*/

							if (addEdge(curr, i.belongsToBlock)) {
								//// FOR DEBUG
								System.out.print("[CFG.build].P0. Adding edge to i from:\t");
								debugPrinter.printInstruction(currInst);
								//// FOR DEBUG
							}
						}
/*
						else 
							System.out.println("[CFG (i.isLeader)] Skipping edge from "+curr.blocknum+" --> "+i.belongsToBlock.blocknum);
*/						
					}
					curr = i.belongsToBlock;
					currInst = i;
				}

				// If i is a goto with target t then add an edge from curr to t
				if (i.opCode == IRInstruction.OpCode.GOTO) {
					IRInstruction t = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);

					((MaxBasicBlock)curr).appendInstruction(i);
/*
					a = curr;
					b = t.belongsToBlock;
					printDebugInfo = (a.id.equalsIgnoreCase("fib::7")) && (b.id.equalsIgnoreCase("fib::9"));
					if (printDebugInfo) {
						System.out.println("[CFG (GOTO)] Connecting blocks "+curr.blocknum+" --> "+t.belongsToBlock.blocknum);
					}
*/

					if (addEdge(curr, t.belongsToBlock)) {
						//// FOR DEBUG
						System.out.print("[CFG.build].P1. Adding edge from i --> t:\t");
						debugPrinter.printInstruction(t);
						//// FOR DEBUG
					}
					// if (i.belongsToBlock != null)
					// 	addEdge(i.belongsToBlock, t.belongsToBlock);
				}
				else if (IRUtil.isConditionalBranch(i)) {
					// Append condition c to curr (i may already belong to block curr, but append is safe)
					((MaxBasicBlock)curr).appendInstruction(i);	// <-- algorithm calls for it, but truly think its unnecessary
/*
					a = curr;
					b = IRUtil.getInstructionAfterThis(i).belongsToBlock;
					printDebugInfo = (a.id.equalsIgnoreCase("fib::7")) && (b.id.equalsIgnoreCase("fib::9"));
					if (printDebugInfo) {
						System.out.println("[CFG (cond branch 1)] Connecting blocks "+curr.blocknum+" --> "+IRUtil.getInstructionAfterThis(i).belongsToBlock.blocknum);
					}
*/
					// Add an edge from curr to i+1	
					IRInstruction next = IRUtil.getInstructionAfterThis(i);
					if (next != null) {

						if (addEdge(curr, next.belongsToBlock)) {
							//// FOR DEBUG
							System.out.print("[CFG.build].P2. Adding edge from i --> next:\t");
							debugPrinter.printInstruction(next);
							//// FOR DEBUG
						}
					}

					// Add an edge from curr to label target t
					IRInstruction t = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
/*
					a = curr;
					b = t.belongsToBlock;
					printDebugInfo = (a.id.equalsIgnoreCase("fib::7")) && (b.id.equalsIgnoreCase("fib::9"));
					if (printDebugInfo) {
						System.out.println("[CFG (cond branch 2)] Connecting blocks "+curr.blocknum+" --> "+t.belongsToBlock.blocknum);
					}
*/
					if (t != null) {

						if (addEdge(curr, t.belongsToBlock)) {
							//// FOR DEBUG
							System.out.print("[CFG.build].P3. Adding edge from i --> t:\t");
							debugPrinter.printInstruction(t);
							//// FOR DEBUG
						}
					}
				}
				else if (i.opCode != IRInstruction.OpCode.LABEL)
					((MaxBasicBlock)curr).appendInstruction(i);

				System.out.println();
			}
		}
		else {
		    // MINIMAL BLOCKS
			for (IRInstruction i : this.f.instructions) {
			    // Generate BBs for every inst
				BasicBlockBase newBlock = new MinBasicBlock(this.f, i);
				////  TODO  ////
				// Will require some analysis of the instruction i here to decide
				// how edges should be created to connect to other blocks...
				////  TODO  ////
			}
			for(IRInstruction i : this.f.instructions) { // categorise instructions
				IROperand x = i.operands[0];
			    IROperand y = i.operands.length > 1 ? i.operands[1] : null;
			    IROperand z = i.operands.length > 2 ? i.operands[2] : null;
			    
			    // if i is a control flow instruction, then assign edges acordingly. otherwise, just set it to the next inst
			    
			    if(i.opCode == IRInstruction.OpCode.GOTO) { 
			        // GOTO //
			        IRInstruction temp = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
			        addEdge(i.belongsToBlock, temp.belongsToBlock);
			    }else if (IRUtil.isConditionalBranch(i)) {
			        // BRANCH //
			        addEdge(i.belongsToBlock, IRUtil.getInstructionAfterThis(this.f, i).belongsToBlock); //i+1
			        IRInstruction temp = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
			        addEdge(i.belongsToBlock, temp.belongsToBlock); //target
			    }
			    //else if(i.opCode == IRInstruction.OpCode.RETURN) {
			        // RETURN //
			        
			        // since a return instruction leaves a cfg, it doesn't have any edges out of it. same logic as call/r
			        
			        //IRInstruction temp = IRUtil.getLabelTarget(this.f, (IRLabelOperand)i.operands[0]);
                    //addEdge(i.belongsToBlock, temp.belongsToBlock);
			    //}//else if(i.opCode == IRInstruction.OpCode.CALL) {
			        // CALL //
			        //to my knowledge, nothing needs to be done for a call or callr because we aren't optimising between functions
			        // piazza @32
			    //}
			    else {
			        IRInstruction t = IRUtil.getInstructionAfterThis(this.f, i);
			        if(t != null) {

			            if (addEdge(i.belongsToBlock, t.belongsToBlock)) { // i+1
							//// FOR DEBUG
							System.out.print("[CFG.build].P4. Added edge from i --> t:\t");
							debugPrinter.printInstruction(t);
							//// FOR DEBUG
						}
			        }
			    }
			    
			}
		}

/*
		// Remove any erroneous CFG edges (e.g., between a block ending with a GOTO and a non-target block)
		// -- Temporary until edge creation logic is patched --
		Set <CFGEdge> toRemove = new HashSet<>();
		for (CFGEdge edge : this.edges) {
			boolean remove = false;
			if (edge.start.terminator.opCode == IRInstruction.OpCode.GOTO) {
				if (!((MaxBasicBlock)edge.end).beginsWithLabel()) remove = true;
				else {
					IRInstruction target = IRUtil.getLabelTarget(this.f, (IRLabelOperand)edge.start.terminator.operands[0]);

					if (edge.end.leader.opCode == IRInstruction.OpCode.LABEL) {
						if (!edge.end.leader.equals(target)) remove = true;
					} else {
						int idx = 0;
						IRInstruction firstInst = ((IRInstruction[])(((MaxBasicBlock)edge.end).instructions.toArray()))[idx];
						while (firstInst.opCode == IRInstruction.OpCode.LABEL) {
							if (((IRLabelOperand)(firstInst.operands[0])).getName().equalsIgnoreCase(
									((IRLabelOperand)((MaxBasicBlock)edge.start).terminator.operands[0]).getName()))
								remove = false;
							else remove = true;
							idx++;
							firstInst = ((IRInstruction[])(((MaxBasicBlock)edge.end).instructions.toArray()))[idx];
						}
					}
				}

				if (remove) toRemove.add(edge);
			}
		}
		this.edges.removeAll(toRemove);
*/

		// Update successors and predecessors for all basic blocks
		System.out.println("[CFG.build] Updating successors & predecessors for all basic blocks");
		for (CFGEdge edge : this.edges) {
			edge.start.successors.addAll(edge.end.successors);
			edge.end.predecessors.addAll(edge.start.predecessors);
		}

		System.out.println("[CFG.build] Generating reaching def sets for all basic blocks");
		generateReachingDefSets();
		System.out.println("[CFG.build] Generating dominator tree for all basic blocks");
		generateDominatorTree();
		System.out.println("[CFG.build] FINISHED.");
	}

	/**	Dominance:
		In a flow graph with entry node b0, node bi dominates node bj, written bi >= bj,
		if and only if bi lies on every path from b0 to bj.
	 */
	//// FIXME: Susceptibility to infinite loop bug below!!!
/*
	private void generateDominatorTree() {
		IRPrinter debugPrinter = new IRPrinter(System.out);

		for (int iters = 0; iters < 2; iters++) {		// Run computations twice
			for (BasicBlockBase block : this.blocks) {
				//// FOR DEBUG
				System.out.print("[CFG.generateDominatorTree] block.leader = ");
				debugPrinter.printInstruction(block.leader);
				System.out.print("\t\t\tblock.terminator = ");
				debugPrinter.printInstruction(block.terminator);
//				System.out.println();
				//// FOR DEBUG

				// All blocks dominated by the root node and themself
				block.dom.add(this.entryNode);
				if (block.equals(this.entryNode)) continue;
				block.dom.add(block);
				
				if (blockHasSingleEntry(block)) {
					System.out.println("\t\t(single entry)");
					for (CFGEdge edge : this.edges) {
						if (edge.end.equals(block)) {
							block.iDom = edge.start;
							// block.dom.addAll(block.iDom.dom);

							Set<BasicBlockBase> predDoms = new LinkedHashSet<>(this.blocks);
							for (BasicBlockBase p : block.predecessors) {
								predDoms.retainAll(p.dom);
							}
							block.dom.addAll(predDoms);
						}
					}
				}
				else {
					System.out.println("\t\t(multiple entries)");
					for (CFGEdge edge : this.edges) {
						if (edge.end.equals(block)) {

							// Should find {B0 --> B1}, {B4 --> B1} 

							boolean uniqueDomFound = false;
							BasicBlockBase pred = edge.start;


							System.out.println("\t\t(found {B"+pred.blocknum+" --> B"+block.blocknum+"})");
							if (pred.equals(this.entryNode)) {
								block.dom.add(pred);
								block.iDom = pred;
								continue;
							}

							Set<BasicBlockBase> predSet = pred.predecessors;
							while (!uniqueDomFound) {
								
								for (BasicBlockBase p : predSet) {
									boolean onConditionalPath = false;
									if (blockHasSingleEntry(p)) {
										if (IRUtil.isControlFlow(p.terminator)) {
//											BasicBlockBase ppreds[] = (BasicBlockBase[])(p.predecessors.toArray());
//											for (int pi = ppreds.length; pi >= 0; --pi) {
//												if (IRUtil.isConditionalBranch(ppreds[pi].terminator)) {
//													onConditionalPath = true;
////													break;
//												}
//											}
											for (CFGEdge pedge : this.edges) {
												if (pedge.end.equals(p)) {
													if (IRUtil.isConditionalBranch(pedge.start.terminator))
														onConditionalPath = true;
												}
											}
											if (!onConditionalPath) {
												uniqueDomFound = true;
												block.dom.addAll(p.dom);
												block.iDom = p;
												break;
											}
//											break;
										}
									}
								}
							}
						}
					}
					System.out.println();
				}
			}
		}

		List<BasicBlockBase> retryList = new ArrayList<>();
		for (BasicBlockBase block : this.blocks) {
			if (block.equals(this.entryNode)) continue;
			if (this.domTree.add(block) == null)	// Add block as node in dominator tree; its parent will be its IDom
				retryList.add(block);
		}
		for (BasicBlockBase retry : retryList)
			this.domTree.add(retry);

		// FOR DEBUG
		this.domTree.printTree();
	}
*/

	/**		Computing Dominators (Approach 1):
	• Formulate problem as a system of data flow constraints:
		– Define dom(n) = set of nodes that dominate n
		– dom(n0)= {n0}
		– dom(n) = ∩ { dom(m) | m ∈ pred(n) } ∪ {n}
		– i.e, the dominators of n include the dominators of all of n’s
			predecessors and n itself

	• Can be solved using iterative algorithm by initializing all dom sets
		except dom(n0) to the universal set (set of all CFG nodes) 

	• A node n dominates m iff n is on every path from n0 to m
		— Every node dominates itself
		— n’s immediate dominator is its closest dominator, IDOM(n)

	— Initialize DOM(n0) = { n0 }, where n0 is the ENTRY node, and DOM(n) = N, set of all CFFG vertices,

	— Iterate on the following recursive equations for all nodes n, until a fixpoint is reached.

		DOM(n) = { n } ∪ (∩p∈preds(n) DOM(p))

	Computing DOM
	• These simultaneous set equations define a simple problem in data-flow analysis
	• Equations have a unique fixed point solution
	• An iterative fixed-point algorithm will solve them quickly 
	**/
	public void generateDominatorTree() {
		System.out.println("\nCFG EDGES:");
		for (CFGEdge edge : this.edges) {
			System.out.println("\t{ "+edge.start + " --> "+edge.end+" }");
		}
		System.out.println();

		BasicBlockBase exitNode = this.entryNode;
		int highestBNum = BasicBlockBase.BLOCKNUM-1;
		for (BasicBlockBase b : this.blocks) {
			if (b.blocknum == highestBNum)
				exitNode = b;
		}

		// printAllPaths(this.entryNode, exitNode);
		// for (BasicBlockBase b : this.blocks) {
		// 	printAllPaths(b, exitNode);
		// }
		for (BasicBlockBase b : this.blocks) {
			printAllPaths(this.entryNode, b);
		}
/*
		// Initialize dom(n0) = {n0} and dom(ni) = {universal set}
		for (BasicBlockBase block : this.blocks) {
			block.dom.clear();
			if (block.equals(this.entryNode))
				block.dom.add(block);
			else
				block.dom.addAll(this.blocks);
		}

		Set<BasicBlockBase> tempdom = new LinkedHashSet<>();
		Set<BasicBlockBase> preddom = new LinkedHashSet<>();
		boolean change = true;
		// If there is a change after the iteration in any of
		// the DOM sets, then 'change' remains true
		int iterations = 0;
		while (change) {
			change = false;
			// ...
			for (BasicBlockBase block : this.blocks) {
				if (block.equals(this.entryNode)) continue;
				// boolean preddomInit = false;
				preddom.clear();
				// preddom.addAll(this.blocks);
				tempdom.clear();
				tempdom.addAll(block.dom);

				for (BasicBlockBase pred : block.predecessors) {
					// if (!preddomInit) {
					// 	preddom.addAll(pred.dom);
					// 	preddomInit = true;		// Alternatively, just initialize preddom to universal set of all CFG nodes...
					// }
					// else 
						preddom.retainAll(pred.dom);
				}
				block.dom.clear();
				block.dom.add(this.entryNode);
				block.dom.add(block);
				// preddom.addAll(block.dom);
				block.dom.addAll(preddom);		// Dom(n) = { n } ∪ (∩ p∈preds(n) DOM(p)) 

				if (!(block.dom.containsAll(tempdom) && tempdom.containsAll(block.dom)))
					change = true;
			}
			iterations++;
		}
		System.out.println("[CFG.generateDominatorTree] Converged after "+iterations+" iterations");
*/
		for (BasicBlockBase m : this.blocks) {
			// A node n dominates m iff n is on every path from n0 to m 
			// Set<BasicBlockBase> nodesOnEveryPathFromRoot = new LinkedHashSet<>(); //this.blocks);
			for (BasicBlockBase n : this.blocks) {
				boolean existsOnEveryPath = true;
				for (int i = 0; i < m.pathsFromRoot.size(); i++) {
					List<BasicBlockBase> path = m.pathsFromRoot.get(i);
					if (!path.contains(n)) {
						existsOnEveryPath = false;
						break;
					}
				}
				if (existsOnEveryPath)
					m.dom.add(n);
			}
			m.dom.add(m);
		}

/*
			for (List<BasicBlockBase> path : m.pathsFromRoot) {
				// nodesOnEveryPathFromRoot.retainAll(path);
				for (BasicBlockBase n : nodesOnEveryPathFromRoot)
				for (BasicBlockBase n : path) {

				}
			}
			m.dom.retainAll(nodesOnEveryPathFromRoot);
			if (!m.dom.containsAll(nodesOnEveryPathFromRoot))
				m.dom.addAll(nodesOnEveryPathFromRoot);
			m.dom.add(m);
		}
*/

		List<BasicBlockBase> retryList = new ArrayList<>();
		for (BasicBlockBase block : this.blocks) {
			if (block.equals(this.entryNode)) continue;
			block.iDom = this.entryNode;
			int prevDistance = block.blocknum - block.iDom.blocknum;
			// Find immediate dominator -- the closest dominating block to current block
			for (BasicBlockBase d : block.dom) {
				if (!d.equals(block) && block.blocknum - d.blocknum < prevDistance)
					block.iDom = d;
			}

			if (this.domTree.add(block) == null)	// Add block as node in dominator tree; its parent will be its IDom
				retryList.add(block);
		}
		for (BasicBlockBase retry : retryList)
			this.domTree.add(retry);


		// FOR DEBUG
		this.domTree.printTree();

/*
		System.out.println("\nCFG EDGES:");
		for (CFGEdge edge : this.edges) {
			System.out.println("\t{ "+edge.start + " --> "+edge.end+" }");
		}
		System.out.println();

		BasicBlockBase exitNode = this.entryNode;
		int highestBNum = BasicBlockBase.BLOCKNUM-1;
		for (BasicBlockBase b : this.blocks) {
			if (b.blocknum == highestBNum)
				exitNode = b;
		}

		// printAllPaths(this.entryNode, exitNode);
		// for (BasicBlockBase b : this.blocks) {
		// 	printAllPaths(b, exitNode);
		// }
		for (BasicBlockBase b : this.blocks) {
			printAllPaths(this.entryNode, b);
		}
*/
	}

	// Prints all paths from 's' to 'd' 
    public void printAllPaths(BasicBlockBase s, BasicBlockBase d) { 
    	System.out.println("ALL PATHS FROM "+s+" TO "+d+":");
        boolean[] isVisited = new boolean[this.blocks.size()]; 
        List<BasicBlockBase> pathList = new ArrayList<>(); 
          
        //add source to path[] 
        pathList.add(s); 
          
        //Call recursive utility 
        printAllPathsUtil(s, d, isVisited, pathList);
        System.out.println();
    } 
  
    // A recursive function to print 
    // all paths from 'u' to 'd'. 
    // isVisited[] keeps track of 
    // vertices in current path. 
    // localPathList<> stores actual 
    // vertices in the current path 
    private void printAllPathsUtil(BasicBlockBase u, BasicBlockBase d, 
			boolean[] isVisited, List<BasicBlockBase> localPathList) { 
        // Mark the current node 
        isVisited[u.blocknum%isVisited.length] = true; 
          
        if (u.equals(d))  
        { 
            System.out.println("\t"+localPathList);
            d.pathsFromRoot.add(new ArrayList<BasicBlockBase>(localPathList));
            // if match found then no need to traverse more till depth 
            isVisited[u.blocknum%isVisited.length]= false; 
            return; 
        } 
          
        // Recur for all the vertices 
        // adjacent to current vertex 
        for (CFGEdge edge : this.edges) {
			if (edge.start.equals(u)) 
        	{
        		BasicBlockBase i = edge.end;
	            if (!isVisited[i.blocknum%isVisited.length]) 
	            { 
	                // store current node  
	                // in path[] 
	                localPathList.add(i); 
	                printAllPathsUtil(i, d, isVisited, localPathList); 
	                // d.pathsFromRoot.add(localPathList);
	                  
	                // remove current node 
	                // in path[] 
	                localPathList.remove(i); 
	            } 
	        } 
        }
        // Mark the current node 
        isVisited[u.blocknum%isVisited.length] = false; 
    } 

	
	// NOTE: Also currently only implemented for using MaxBasicBlocks
	private void generateInitialBlocks(List<IRInstruction> instructions) {
		IRPrinter debugPrinter = new IRPrinter(System.out);

		for (IRInstruction i : instructions) {
//			//// FOR DEBUG
//			System.out.print("[CFG.generateInitialBlocks] i:\t");
//			debugPrinter.printInstruction(i);
//			//// FOR DEBUG

			if ((i.isLeader || i.opCode == IRInstruction.OpCode.LABEL)
					&& i.belongsToBlock == null) {
				//// FOR DEBUG
				System.out.print("[CFG.generateInitialBlocks] i:\t");
				debugPrinter.printInstruction(i);
				//// FOR DEBUG

				BasicBlockBase newBlock;
				List<IRInstruction> instsToAdd = new ArrayList<>(Arrays.asList(i));

				// If inst is a label, add the very next instruction to the same new block with it
				if (i.opCode == IRInstruction.OpCode.LABEL) {
					IRInstruction nextInst = IRUtil.getInstructionAfterThis(this.f, i);
					if (nextInst != null) {
						instsToAdd.add(nextInst);
						// Account for stacked/consecutive label delcarations in source IR
						while (nextInst != null && nextInst.opCode == IRInstruction.OpCode.LABEL) {
							nextInst = IRUtil.getInstructionAfterThis(this.f, nextInst);	// , i);  <-- BUG FIX
							if (nextInst != null)
								instsToAdd.add(nextInst);
						}
					}
				}

				newBlock = new MaxBasicBlock(this.f, instsToAdd);
				// i.belongsToBlock = newBlock;		// <-- handled by the *BasicBlock constructor
/*
				// FOR DEBUG
				for (IRInstruction addedInst : instsToAdd) {
					System.out.print("[generateInitialBlocks] ADDING TO BLOCK "+newBlock.blocknum+":\t");
					debugPrinter.printInstruction(addedInst);
				}
				System.out.println();
				// FOR DEBUG
*/
				if (this.blocks.isEmpty()) {
					this.entryNode = newBlock;
					this.domTree = new DominatorTree(newBlock);
				}

				this.blocks.add(newBlock);
			}
		}
	}


	private boolean addEdge(BasicBlockBase from, BasicBlockBase to) {
		// TODO: Assert that neither from nor to is null
		if (from == null || to == null) {
			if (from == null)
				System.out.println("[CFG.addEdge] ERROR: BasicBlock vertex 'from' is null -- CFGEdge cancelled");
			if (to == null)
				System.out.println("[CFG.addEdge] ERROR: BasicBlock vertex 'to' is null -- CFGEdge cancelled");
			return false;
		}
		// Skip edges from block to itself
		if (from.equals(to)) return false;
		// Skip redundant edges
		for (CFGEdge edge : this.edges) {
			if (edge.start.equals(from) && edge.end.equals(to))
				return false;
		}
		CFGEdge newEdge = new CFGEdge(from, to);
		from.successors.add(to);
		from.successors.addAll(to.successors);
		to.predecessors.add(from);
		to.predecessors.addAll(from.predecessors);
		this.edges.add(newEdge);
		return true;
	}


	// NOTE: Also currently only implemented for using MaxBasicBlocks
	/*
		Step 1: Compute GEN and KILL for each basic block
		Step 2: For every basic block, make:
					OUT[B] = GEN[B]
		Step 3: While a fixed point is not found:
					IN[B] = ∪OUT[p]  where p is a predecessor of B
					OUT[B] = GEN[B] ∪ (IN[B] - KILL[B])
	 */
	public void generateReachingDefSets() {
	    if (!USE_MAXIMAL_BLOCKS) {
	        // Minimal blocks
	        MinBasicBlock basicBlock;
	        	        
	        for(BasicBlockBase b : this.blocks) {
	            basicBlock = (MinBasicBlock) b;
	            
	            // IN[bb] = ∪OUT[p] for p in the set of all predecessors of block bb
	            for (BasicBlockBase p : basicBlock.predecessors) {
	                basicBlock.in.addAll(p.out);
                }
	            
	            // OUT[bb] = GEN[bb] ∪ (IN[bb] - KILL[bb])
	            basicBlock.out = basicBlock.gen;		//// TODO (finish from here) ////
	        }
	        
	        return;
	    }

		MaxBasicBlock bb;
//		Set<IRInstruction> universalDefinitions = new LinkedHashSet<>();
		universalDefinitions.clear();
		/*	LinkedHashSet methods for basic set operations:
				INTERSECTION: set1.retainAll(set2)
					--> will remove any element from set1 if it is not in set2
					( SET1 = SET1 ∩ SET2 )
				SUBTRACTION: set1.removeAll(set2)
					--> will remove any element from set1 if it is in set2
					( SET1 = SET1 - SET2 )
				UNION: set1.addAll(set2)
					--> will add any element from set2 to set1 if it does not already exist in set1
					( SET1 = SET1 ∪ SET2 )
		 */
		for (BasicBlockBase block : this.blocks) {
			bb = (MaxBasicBlock)block;
			bb.gen.clear();
			bb.kill.clear();
			bb.in.clear();
			bb.out.clear();
			bb.operandDefs.clear();
			bb.operandUses.clear();

			bb.findAllDefsAndUses();	// Will populate GEN[bb]
			bb.out.addAll(bb.gen);		// Initialize OUT[bb] = GEN[bb]
			universalDefinitions.addAll(bb.gen);
		}

		// Next, need to populate the KILL set for each block
		for (BasicBlockBase block : this.blocks) {
			bb = (MaxBasicBlock)block;
			// For each lval definition in block, find all definitions
			// throughout entire program/function that write to lval
			// regardless of whether or not they reach the block/statement
			for (String lval : bb.operandDefs.keySet()) {
				for (IRInstruction def : universalDefinitions) {
					// First operand is the lval for all definitions
					if (lval.equals(((IRVariableOperand) def.operands[0]).getName())) {
						bb.kill.add(def);
					}
				}
			}

			bb.kill.removeAll(bb.gen);	// Unsure if generated defs within the block should be included or excluded from the KILL set...
		}

		// Initiate the iteration for computing each block's
		// IN and OUT sets for reaching definitions.
		boolean change = true;
		Set<IRInstruction> tempout = new HashSet<>();
		Set<IRInstruction> gen = new HashSet<>();
		Set<IRInstruction> inSubKill = new HashSet<>();

		// If there is a change after the iteration in any of
		// the OUT sets, then 'change' remains true
		while (change) {
			change = false;

			for (BasicBlockBase block : this.blocks) {
				bb = (MaxBasicBlock)block;
				bb.in.clear();

				// IN[bb] = ∪OUT[p] for p in the set of all predecessors of block bb
				for (BasicBlockBase p : bb.predecessors) {
					bb.in.addAll(p.out);
				}

				// OUT[bb] = GEN[bb] ∪ (IN[bb] - KILL[bb])
				tempout.clear();
				tempout.addAll(bb.out);
				bb.out.clear();

				inSubKill.clear();
				inSubKill.addAll(bb.in);
				inSubKill.removeAll(bb.kill);

				gen.clear();
				gen.addAll(bb.gen);
				gen.addAll(inSubKill);
				bb.out.addAll(gen);

				if (!(bb.out.containsAll(tempout) && tempout.containsAll(bb.out)))
					change = true;
			}
		}

	}

	public void printAllBasicBlocks() {
		IRPrinter blockPrinter = new IRPrinter(System.out);
		for (BasicBlockBase block : this.blocks) {
			System.out.println("________________________________________");
			if (USE_MAXIMAL_BLOCKS) {
				MaxBasicBlock bb = (MaxBasicBlock) block;
				System.out.println("\n____ BLOCK ["+bb.blocknum+"]: \""+bb.id+"\" ____");
				System.out.println("\nINSTRUCTIONS: { ");
				for (IRInstruction i : bb.instructions) {
					System.out.print("\t");
					blockPrinter.printInstruction(i);
				}
				System.out.print(" }\n\nPREDS: { ");
				for (BasicBlockBase p : bb.predecessors) {
					System.out.print(String.valueOf(p.blocknum) + ", ");
				}
				System.out.print(" }\nSUCCS: { ");
				for (BasicBlockBase s : bb.successors) {
					System.out.print(String.valueOf(s.blocknum) + ", ");
				}
				System.out.println(" }\n");
				System.out.println("PATHS FROM ROOT: {");
				for (List<BasicBlockBase> path : block.pathsFromRoot) {
					System.out.println("\t"+path);
				}
				System.out.println(" }\n");
				if (bb.iDom != null)
					System.out.println("IDOM("+bb.blocknum+") = "+bb.iDom.blocknum);
				System.out.print("DOMS: { ");
				for (BasicBlockBase d : bb.dom) {
					System.out.print(String.valueOf(d.blocknum) + ", ");
				}
				System.out.println(" }");
				System.out.println("\nGEN: { ");
				for (IRInstruction g : bb.gen) {
					System.out.print("\t");
					blockPrinter.printInstruction(g);
				}
				System.out.println("}\n\nKILL: { ");
				for (IRInstruction k : bb.kill) {
					System.out.print("\t");
					blockPrinter.printInstruction(k);
				}
				System.out.println("}\n\nIN: { ");
				for (IRInstruction in : bb.in) {
					System.out.print("\t");
					blockPrinter.printInstruction(in);
				}
				System.out.println("}\n\nOUT: { ");
				for (IRInstruction o : bb.out) {
					System.out.print("\t");
					blockPrinter.printInstruction(o);
				}
				System.out.print("}\n");
			}
			System.out.println("________________________________________");
		}
	}

	public int getBlockIndex(BasicBlockBase bb) {
		List<BasicBlockBase> blockList = new ArrayList<>(this.blocks);
		return blockList.indexOf(bb);
	}

	public BasicBlockBase getBlockByNumber(int blocknum) {
		for (BasicBlockBase bb : this.blocks) {
			if (bb.blocknum == blocknum)
				return bb;
		}
		return null;
	}

	public boolean blockHasSingleEntry(BasicBlockBase bb) {
		// boolean foundSingleEntry = false;
		if (bb.equals(this.entryNode)) return true;
		int occurrences = 0;
		for (CFGEdge edge : this.edges) {
			if (edge.end.equals(bb)) {
				// if (!foundSingleEntry) foundSingleEntry = true;
				// else return false;
				occurrences++;
			}
		}
		// return foundSingleEntry;
		return occurrences == 1;
	}

	public List<CFGEdge> getEdgesFromBlock(BasicBlockBase bb) {
		List<CFGEdge> adj = new ArrayList<>();
		for (CFGEdge edge : this.edges) {
			if (edge.start.equals(bb))
				adj.add(edge);
		}
		return adj;
	}

	public List<CFGEdge> getEdgesToBlock(BasicBlockBase bb) {
		List<CFGEdge> adj = new ArrayList<>();
		for (CFGEdge edge : this.edges) {
			if (edge.end.equals(bb))
				adj.add(edge);
		}
		return adj;
	}

	public Set<CFGEdge> getEdges() {
		return this.edges;
	}

	public Set<BasicBlockBase> getBlocks() {
		return this.blocks;
	}

	public IRFunction getFunction() {
		return this.f;
	}

	public Set<IRInstruction> getUniversalDefinitions() {
		return this.universalDefinitions;
	}

	public BasicBlockBase getEntryNode() {
		return this.entryNode;
	}

}







